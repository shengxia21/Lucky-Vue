package com.lucky.ai.core.processor.chat;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.lucky.ai.controller.chat.vo.message.AiChatMessageSendReqVO;
import com.lucky.ai.controller.chat.vo.message.AiChatMessageSendRespVO;
import com.lucky.ai.core.context.ChatContext;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.AiChatMessage;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.mapper.AiChatMessageMapper;
import com.lucky.ai.util.AiUtils;
import com.lucky.common.constant.AiErrorConstants;
import com.lucky.common.core.domain.AjaxResult;
import com.lucky.common.utils.DateUtils;
import com.lucky.common.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 抽象聊天处理器
 * 提供通用的聊天处理逻辑，子类只需实现特定平台的逻辑
 * [模板方法核心类]
 *
 * @author lucky
 */
public abstract class AbstractChatProcessor implements ChatProcessor {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    protected AiChatMessageMapper aiChatMessageMapper;

    @Override
    public Flux<AjaxResult> processStream(ChatContext chatContext) {
        // 从上下文获取参数
        AiChatMessageSendReqVO sendReqVO = chatContext.getSendReqVO();
        AiChatConversation conversation = chatContext.getConversation();
        AiModel model = chatContext.getModel();
        AiApiKey apiKey = chatContext.getApiKey();
        Long userId = chatContext.getUserId();

        // 获取ChatModel
        ChatModel chatModel = buildChatModel(apiKey.getApiKey());

        // 插入用户消息
        AiChatMessage userMessage = createChatMessage(conversation.getId(), null, model,
                userId, conversation.getRoleId(), MessageType.USER, sendReqVO.getContent(), sendReqVO.getUseContext(),
                sendReqVO.getAttachmentUrls());

        // 插入助手消息（占位）
        AiChatMessage assistantMessage = createChatMessage(conversation.getId(), userMessage.getId(), model,
                userId, conversation.getRoleId(), MessageType.ASSISTANT, "", sendReqVO.getUseContext(),
                null);

        // 获取历史消息列表
        List<AiChatMessage> historyMessages = aiChatMessageMapper.selectListByConversationId(conversation.getId());

        // 构建Prompt
        Prompt prompt = buildPrompt(conversation, historyMessages, model, sendReqVO);

        // 流式调用
        Flux<ChatResponse> streamResponse = chatModel.stream(prompt);

        String username = SecurityUtils.getUsername();

        // 流式返回
        StringBuffer contentBuffer = new StringBuffer();
        StringBuffer reasoningContentBuffer = new StringBuffer();
        return streamResponse.map(chunk -> {
            // 响应结果
            String newContent = AiUtils.getChatResponseContent(chunk);
            String newReasoningContent = AiUtils.getChatResponseReasoningContent(chunk);
            if (StrUtil.isNotEmpty(newContent)) {
                contentBuffer.append(newContent);
            }
            if (StrUtil.isNotEmpty(newReasoningContent)) {
                reasoningContentBuffer.append(newReasoningContent);
            }
            AiChatMessageSendRespVO respVO = new AiChatMessageSendRespVO()
                    .setSend(BeanUtil.toBean(userMessage, AiChatMessageSendRespVO.Message.class))
                    .setReceive(BeanUtil.toBean(assistantMessage, AiChatMessageSendRespVO.Message.class)
                            .setContent(StrUtil.nullToDefault(newContent, "")) // 避免 null 的 情况
                            .setReasoningContent(StrUtil.nullToDefault(newReasoningContent, "")) // 避免 null 的 情况
                    );
            return AjaxResult.success(respVO);
        }).doOnComplete(() -> updateOrCreateAssistantMessage(assistantMessage, contentBuffer.toString(), reasoningContentBuffer.toString(), username))
        .doOnError(throwable -> {
            log.error("[processStream][userId({}) sendReqVO({}) 发生异常]", userId, sendReqVO, throwable);
            handleStreamMessage(assistantMessage, contentBuffer.toString(), reasoningContentBuffer.toString(), username);
        }).doOnCancel(() -> {
            log.info("[processStream][userId({}) sendReqVO({}) 取消请求]", userId, sendReqVO);
            handleStreamMessage(assistantMessage, contentBuffer.toString(), reasoningContentBuffer.toString(), username);
        }).onErrorResume(error -> Flux.just(AjaxResult.error(AiErrorConstants.CHAT_STREAM_ERROR)));
    }

    /**
     * 获取特定平台的ChatModel
     *
     * @param apiKey API密钥
     * @return ChatModel实例
     */
    protected abstract ChatModel buildChatModel(String apiKey);

    /**
     * 构建特定平台的ChatOptions
     *
     * @param model 模型信息
     * @param conversation 对话信息
     * @return ChatOptions实例
     */
    protected abstract ChatOptions buildChatOptions(AiModel model, AiChatConversation conversation);

    /**
     * 创建聊天消息
     */
    private AiChatMessage createChatMessage(Long conversationId, Long replyId,
                                              AiModel model, Long userId, Long roleId,
                                              MessageType messageType, String content,
                                              Boolean useContext, List<String> attachmentUrls) {
        AiChatMessage message = new AiChatMessage();
        message.setConversationId(conversationId);
        message.setReplyId(replyId);
        message.setModel(model.getModel());
        message.setModelId(model.getId());
        message.setUserId(userId);
        message.setRoleId(roleId);
        message.setType(messageType.getValue());
        message.setContent(content);
        message.setUseContext(useContext);
        message.setAttachmentUrls(attachmentUrls);
        message.setCreateBy(SecurityUtils.getUsername());
        message.setCreateTime(DateUtils.getNowDate());
        aiChatMessageMapper.insertAiChatMessage(message);
        return message;
    }

    /**
     * 构建Prompt
     */
    private Prompt buildPrompt(AiChatConversation conversation, List<AiChatMessage> messages,
                                 AiModel model, AiChatMessageSendReqVO sendReqVO) {
        List<Message> chatMessages = new ArrayList<>();
        // System Context 角色设定
        if (StrUtil.isNotBlank(conversation.getSystemMessage())) {
            chatMessages.add(new SystemMessage(conversation.getSystemMessage()));
        }

        // 历史 history message 历史消息
        List<AiChatMessage> contextMessages = filterContextMessages(messages, conversation, sendReqVO);
        contextMessages.forEach(message -> chatMessages.add(AiUtils.buildMessage(message.getType(), message.getContent())));

        // 当前 user message 新发送消息
        chatMessages.add(new UserMessage(sendReqVO.getContent()));

        // 构建 ChatOptions 对象
        ChatOptions chatOptions = buildChatOptions(model, conversation);
        return new Prompt(chatMessages, chatOptions);
    }

    private List<AiChatMessage> filterContextMessages(List<AiChatMessage> messages,
                                                      AiChatConversation conversation,
                                                      AiChatMessageSendReqVO sendReqVO) {
        if (conversation.getMaxContexts() == null || conversation.getMaxContexts() <= 0 || ObjUtil.notEqual(sendReqVO.getUseContext(), Boolean.TRUE)) {
            return Collections.emptyList();
        }
        List<AiChatMessage> contextMessages = new ArrayList<>(conversation.getMaxContexts() * 2);
        for (int i = messages.size() - 1; i >= 0; i--) {
            AiChatMessage assistantMessage = CollUtil.get(messages, i);
            if (assistantMessage == null || assistantMessage.getReplyId() == null) {
                continue;
            }
            AiChatMessage userMessage = CollUtil.get(messages, i - 1);
            if (userMessage == null
                    || ObjUtil.notEqual(assistantMessage.getReplyId(), userMessage.getId())
                    || StrUtil.isEmpty(assistantMessage.getContent())) {
                continue;
            }
            // 由于后续要 reverse 反转，所以先添加 assistantMessage
            contextMessages.add(assistantMessage);
            contextMessages.add(userMessage);
            // 超过最大上下文，结束
            if (contextMessages.size() >= conversation.getMaxContexts() * 2) {
                break;
            }
        }
        Collections.reverse(contextMessages);
        return contextMessages;
    }

    private void handleStreamMessage(AiChatMessage assistantMessage, String content, String reasoningContent, String username) {
        // 如果有内容，则更新内容
        if (StrUtil.isNotEmpty(content)) {
            updateOrCreateAssistantMessage(assistantMessage, content, reasoningContent, username);
        } else {
            // 否则，则进行删除
            aiChatMessageMapper.deleteAiChatMessageById(assistantMessage.getId());
        }
    }

    private void updateOrCreateAssistantMessage(AiChatMessage assistantMessage, String content, String reasoningContent, String username) {
        // 更新消息内容
        AiChatMessage message = new AiChatMessage();
        message.setId(assistantMessage.getId());
        message.setContent(content);
        message.setReasoningContent(reasoningContent);
        message.setUpdateBy(username);
        message.setUpdateTime(DateUtils.getNowDate());
        aiChatMessageMapper.updateAiChatMessage(message);
    }

}