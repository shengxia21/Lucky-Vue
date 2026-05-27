package com.lucky.ai.core.facade;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.lucky.ai.core.ChatService;
import com.lucky.ai.core.context.ChatContext;
import com.lucky.ai.core.strategy.ChatModelStrategy;
import com.lucky.ai.core.vo.chat.ChatMessageRequest;
import com.lucky.ai.core.vo.chat.ChatMessageResponse;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.AiChatMessage;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.factory.AsyncAiFactory;
import com.lucky.ai.factory.ChatModelFactory;
import com.lucky.ai.mapper.AiChatMessageMapper;
import com.lucky.ai.util.SpringAiUtils;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.web.manager.AsyncManager;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 聊天服务外观类
 *
 * @author lucky
 */
@Slf4j
@Service
public class ChatServiceFacade implements ChatService {

    @Resource
    private AiChatMessageMapper chatMessageMapper;

    @Resource
    private ChatModelFactory chatFactory;

    @Override
    public Flux<ChatMessageResponse> chat(ChatContext chatContext) {
        // 创建聊天消息
        Long assistantId = createChatMessage(chatContext);
        // 获取聊天模型策略
        ChatModelStrategy strategy = chatFactory.getOriginalStrategy(chatContext.getModel().getPlatform());
        // 构建聊天选项
        ChatOptions chatOptions = strategy.buildChatOptions(chatContext);
        // 构建聊天消息
        List<Message> chatMessages = buildChatMessages(chatContext);
        // 构建Prompt
        Prompt prompt = new Prompt(chatMessages, chatOptions);
        // 构建模型
        ChatModel chatModel = strategy.buildChatModel(chatContext.getApiKey().getApiKey());
        // 流式处理
        Flux<ChatResponse> responseFlux = chatModel.stream(prompt);

        // 文本内容
        StringBuffer contentBuffer = new StringBuffer();
        StringBuffer reasoningContentBuffer = new StringBuffer();
        ChatMessageResponse response = new ChatMessageResponse();
        return responseFlux.map(chatResponse -> {
            // 提取响应内容
            String content = strategy.extractChatResponseContent(chatResponse);
            String reasoningContent = strategy.extractChatResponseReasoningContent(chatResponse);
            if (StrUtil.isNotEmpty(content)) {
                contentBuffer.append(content);
            }
            if (StrUtil.isNotEmpty(reasoningContent)) {
                reasoningContentBuffer.append(reasoningContent);
            }
            response.setContent(content);
            response.setReasoningContent(reasoningContent);
            return response;
        }).doOnComplete(() -> {
            // 流式响应完成时触发, 异步更新消息
            AsyncManager.me().execute(AsyncAiFactory.updateAssistantMessage(assistantId, chatContext.getUserName(), contentBuffer.toString(), reasoningContentBuffer.toString()));
        }).doOnCancel(() -> {
            // 用户取消请求时触发
            log.warn("流式响应 - [userId({}) 用户取消请求]", chatContext.getUserId());
            // 异步更新assistant聊天消息
            AsyncManager.me().execute(AsyncAiFactory.updateAssistantMessage(assistantId, chatContext.getUserName(), contentBuffer.toString(), reasoningContentBuffer.toString()));
        }).onErrorResume(error -> {
            // 流式响应过程中触发异常（包含LLM大模型返回的错误信息）
            log.error("流式响应 - [模型标识({}) 请求过程中发生异常: {}]", chatContext.getModel().getModel(), error.getMessage());
            // 异步删除创建的assistant聊天消息
            AsyncManager.me().execute(AsyncAiFactory.deleteAssistantMessage(assistantId));
            // 将异常信息设置为响应内容(有些错误可能是用户的配置问题,需要提示用户)
            response.setContent(error.getMessage());
            return Flux.just(response);
        });
    }

    /**
     * 创建聊天消息
     *
     * @param chatContext 聊天上下文
     * @return assistantId
     */
    private Long createChatMessage(ChatContext chatContext) {
        ChatMessageRequest request = chatContext.getRequest();
        AiChatConversation conversation = chatContext.getConversation();
        AiModel model = chatContext.getModel();

        AiChatMessage message = new AiChatMessage();
        message.setConversationId(conversation.getId());
        message.setReplyId(null);
        message.setModel(model.getModel());
        message.setModelId(model.getId());
        message.setUserId(chatContext.getUserId());
        message.setRoleId(conversation.getRoleId());
        message.setType(MessageType.USER.getValue());
        message.setContent(request.getContent());
        message.setUseContext(request.getUseContext());
        message.setAttachmentUrls(request.getAttachmentUrls());
        chatMessageMapper.insert(message);
        // userMessageId
        message.setReplyId(message.getId());
        message.setId(null);
        message.setType(MessageType.ASSISTANT.getValue());
        message.setContent(StringUtils.EMPTY);
        message.setAttachmentUrls(request.getAttachmentUrls());
        chatMessageMapper.insert(message);
        // assistantMessageId
        return message.getId();
    }

    /**
     * 构建聊天消息
     *
     * @param chatContext 聊天上下文
     * @return 聊天消息列表
     */
    private List<Message> buildChatMessages(ChatContext chatContext) {
        ChatMessageRequest request = chatContext.getRequest();
        AiChatConversation conversation = chatContext.getConversation();

        // 构建聊天消息列表
        List<Message> chatMessages = new ArrayList<>();
        //  添加角色设定
        if (StrUtil.isNotBlank(conversation.getSystemMessage())) {
            chatMessages.add(new SystemMessage(conversation.getSystemMessage()));
        }
        // 添加历史消息
        chatMessages.addAll(buildHistoryMessages(request, conversation));
        // 添加发送消息
        chatMessages.add(new UserMessage(request.getContent()));
        return chatMessages;
    }

    /**
     * 构建历史消息
     *
     * @param request      聊天请求VO
     * @param conversation 会话VO
     * @return 历史消息列表
     */
    private List<Message> buildHistoryMessages(ChatMessageRequest request, AiChatConversation conversation) {
        if (conversation.getMaxContexts() == null || conversation.getMaxContexts() <= 0 || ObjUtil.notEqual(request.getUseContext(), Boolean.TRUE)) {
            return Collections.emptyList();
        }
        List<AiChatMessage> historyMessages = chatMessageMapper.selectListByConversationId(conversation.getId());
        if (historyMessages.isEmpty()) {
            return Collections.emptyList();
        }
        List<AiChatMessage> contextMessages = new ArrayList<>(conversation.getMaxContexts() * 2);
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
        return contextMessages.stream().map(SpringAiUtils::convertMessage).toList();
    }

}