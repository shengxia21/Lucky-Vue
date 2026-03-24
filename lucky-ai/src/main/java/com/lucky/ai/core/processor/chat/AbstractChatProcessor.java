package com.lucky.ai.core.processor.chat;

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
import com.lucky.common.core.domain.AjaxResult;
import com.lucky.common.utils.DateUtils;
import com.lucky.common.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

        // 插入用户消息
        AiChatMessage userMessage = createChatMessage(conversation.getId(), null, model,
                userId, conversation.getRoleId(), MessageType.USER, sendReqVO.getContent(), sendReqVO.getUseContext(),
                sendReqVO.getAttachmentUrls());
        // 插入助手消息（占位）
        AiChatMessage assistantMessage = createChatMessage(conversation.getId(), userMessage.getId(), model,
                userId, conversation.getRoleId(), MessageType.ASSISTANT, "", sendReqVO.getUseContext(),
                null);

        // 构建Prompt
        Prompt prompt = buildPrompt(conversation, model, sendReqVO);
        // 获取ChatModel
        ChatModel chatModel = buildChatModel(apiKey.getApiKey());
        // 流式调用
        Flux<ChatResponse> streamResponse = chatModel.stream(prompt);

        // 文本内容
        StringBuffer contentBuffer = new StringBuffer();
        StringBuffer reasoningContentBuffer = new StringBuffer();
        AiChatMessageSendRespVO respVO = new AiChatMessageSendRespVO();
        AjaxResult success = AjaxResult.success();
        return streamResponse.map(chunk -> {
            // 提取响应内容
            String content = extractChatResponseContent(chunk);
            String reasoningContent = extractChatResponseReasoningContent(chunk);
            if (StrUtil.isNotEmpty(content)) {
                contentBuffer.append(content);
            }
            if (StrUtil.isNotEmpty(reasoningContent)) {
                reasoningContentBuffer.append(reasoningContent);
            }
            respVO.setId(assistantMessage.getId());
            respVO.setContent(content);
            respVO.setReasoningContent(reasoningContent);
            respVO.setCreateTime(assistantMessage.getCreateTime());
            respVO.setSendId(userMessage.getId());
            respVO.setSendContent(userMessage.getContent());
            respVO.setSendTime(userMessage.getCreateTime());
            return success.put(AjaxResult.DATA_TAG, respVO);
        }).doOnComplete(() -> {
            // 流式响应完成时触发
            updateAssistantMessage(assistantMessage.getId(), contentBuffer.toString(), reasoningContentBuffer.toString());
        }).doOnCancel(() -> {
            // 用户取消请求时触发
            log.info("流式响应 - [userId({}) model({}) 用户取消请求]", userId, model.getModel());
            handleMessage(assistantMessage.getId(), contentBuffer.toString(), reasoningContentBuffer.toString());
        }).doOnError(throwable -> {
            // LLM大模型返回错误信息时触发
            log.error("流式响应 - [模型标识({}) 发生异常: {}]", model.getModel(), throwable.getMessage());
            handleMessage(assistantMessage.getId(), contentBuffer.toString(), reasoningContentBuffer.toString());
        }).onErrorResume(error -> {
            // 流式响应过程中触发异常（包含LLM大模型返回的错误信息）
            log.warn("流式响应 - [触发异常返回]");
            return Flux.just(AjaxResult.error(error.getMessage()));
        });
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
     * @param model        模型信息
     * @param conversation 对话信息
     * @return ChatOptions实例
     */
    protected abstract ChatOptions buildChatOptions(AiModel model, AiChatConversation conversation);

    /**
     * 提取ChatResponse的内容
     *
     * @param response ChatResponse对象
     * @return 响应内容
     */
    protected String extractChatResponseContent(ChatResponse response) {
        return response.getResult().getOutput().getText();
    }

    /**
     * 提取ChatResponse的推理内容
     *
     * @param response ChatResponse对象
     * @return 推理内容
     */
    protected abstract String extractChatResponseReasoningContent(ChatResponse response);

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

    private void handleMessage(Long assistantMessageId, String content, String reasoningContent) {
        // 如果有内容，则更新内容
        if (StrUtil.isNotEmpty(content) || StrUtil.isNotEmpty(reasoningContent)) {
            updateAssistantMessage(assistantMessageId, content, reasoningContent);
        } else {
            // 否则，则进行删除
            aiChatMessageMapper.deleteAiChatMessageById(assistantMessageId);
        }
    }

    private void updateAssistantMessage(Long assistantMessageId, String content, String reasoningContent) {
        // 更新消息内容
        Date nowDate = DateUtils.getNowDate();
        AiChatMessage message = new AiChatMessage();
        message.setId(assistantMessageId);
        message.setContent(content);
        message.setReasoningContent(reasoningContent);
        message.setCreateTime(nowDate);
        aiChatMessageMapper.updateAiChatMessage(message);
    }

    /**
     * 构建Prompt
     */
    private Prompt buildPrompt(AiChatConversation conversation, AiModel model, AiChatMessageSendReqVO sendReqVO) {
        List<Message> chatMessages = new ArrayList<>();
        //  添加角色设定
        if (StrUtil.isNotBlank(conversation.getSystemMessage())) {
            chatMessages.add(new SystemMessage(conversation.getSystemMessage()));
        }
        // 添加历史消息
        chatMessages.addAll(buildHistoryMessages(conversation, sendReqVO));
        // 添加发送消息
        chatMessages.add(new UserMessage(sendReqVO.getContent()));

        // 构建 ChatOptions 对象
        ChatOptions chatOptions = buildChatOptions(model, conversation);
        return new Prompt(chatMessages, chatOptions);
    }

    private List<Message> buildHistoryMessages(AiChatConversation conversation, AiChatMessageSendReqVO sendReqVO) {
        if (conversation.getMaxContexts() == null || conversation.getMaxContexts() <= 0 || ObjUtil.notEqual(sendReqVO.getUseContext(), Boolean.TRUE)) {
            return Collections.emptyList();
        }
        List<AiChatMessage> contextMessages = new ArrayList<>(conversation.getMaxContexts() * 2);
        List<AiChatMessage> historyMessages = aiChatMessageMapper.selectListByConversationId(conversation.getId());
        for (int i = historyMessages.size() - 1; i >= 0; i--) {
            AiChatMessage assistantMessage = CollUtil.get(historyMessages, i);
            if (assistantMessage == null || assistantMessage.getReplyId() == null) {
                continue;
            }
            AiChatMessage userMessage = CollUtil.get(historyMessages, i - 1);
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
        return contextMessages.stream().map(this::convertMessage).toList();
    }

    private Message convertMessage(AiChatMessage message) {
        if (MessageType.USER.getValue().equals(message.getType())) {
            return new UserMessage(message.getContent());
        }
        if (MessageType.ASSISTANT.getValue().equals(message.getType())) {
            return new AssistantMessage(message.getContent());
        }
        if (MessageType.SYSTEM.getValue().equals(message.getType())) {
            return new SystemMessage(message.getContent());
        }
        if (MessageType.TOOL.getValue().equals(message.getType())) {
            throw new UnsupportedOperationException("暂不支持 tool 消息：" + message.getContent());
        }
        throw new IllegalArgumentException(StrUtil.format("未知消息类型({})", message.getType()));
    }

}