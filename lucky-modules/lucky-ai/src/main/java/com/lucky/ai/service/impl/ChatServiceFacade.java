package com.lucky.ai.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.lucky.ai.domain.AiChatMessage;
import com.lucky.ai.factory.AsyncAiFactory;
import com.lucky.ai.mapper.AiChatMessageMapper;
import com.lucky.ai.util.SpringAiUtils;
import com.lucky.common.ai.chat.ChatService;
import com.lucky.common.ai.domain.request.ChatRequest;
import com.lucky.common.ai.domain.vo.ChatResponseVO;
import com.lucky.common.ai.factory.ChatServiceFactory;
import com.lucky.common.ai.service.AbstractChatService;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.security.utils.SecurityUtils;
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
    private ChatServiceFactory chatFactory;

    @Override
    public Flux<ChatResponseVO> chat(ChatRequest chatRequest) {
        // 创建聊天消息
        Long assistantId = createChatMessage(chatRequest);
        // 获取聊天模型策略
        AbstractChatService service = chatFactory.getOriginalService(chatRequest.getPlatform());
        // 构建聊天选项
        ChatOptions chatOptions = service.buildChatOptions(chatRequest);
        // 构建聊天消息
        List<Message> chatMessages = buildChatMessages(chatRequest);
        // 构建Prompt
        Prompt prompt = new Prompt(chatMessages, chatOptions);
        // 构建模型
        ChatModel chatModel = service.buildChatModel(chatRequest.getUrl(), chatRequest.getApiKey());
        // 流式处理
        Flux<ChatResponse> response = chatModel.stream(prompt);

        Long userId = SecurityUtils.getUserId();
        String userName = SecurityUtils.getUserName();

        // 文本内容
        StringBuffer contentBuffer = new StringBuffer();
        StringBuffer reasoningContentBuffer = new StringBuffer();
        ChatResponseVO responseVo = new ChatResponseVO();
        return response.map(chatResponse -> {
            // 提取响应内容
            String content = service.extractChatResponseContent(chatResponse);
            String reasoningContent = service.extractChatResponseReasoningContent(chatResponse);
            if (StrUtil.isNotEmpty(content)) {
                contentBuffer.append(content);
            }
            if (StrUtil.isNotEmpty(reasoningContent)) {
                reasoningContentBuffer.append(reasoningContent);
            }
            responseVo.setContent(content);
            responseVo.setReasoningContent(reasoningContent);
            return responseVo;
        }).doOnComplete(() -> {
            // 流式响应完成时触发, 异步更新消息
            AsyncManager.me().execute(AsyncAiFactory.updateAssistantMessage(assistantId, userName, contentBuffer.toString(), reasoningContentBuffer.toString()));
        }).doOnCancel(() -> {
            // 用户取消请求时触发
            log.warn("流式响应 - [userId({}) 用户取消请求]", userId);
            // 异步更新assistant聊天消息
            AsyncManager.me().execute(AsyncAiFactory.updateAssistantMessage(assistantId, userName, contentBuffer.toString(), reasoningContentBuffer.toString()));
        }).onErrorResume(error -> {
            // 流式响应过程中触发异常（包含LLM大模型返回的错误信息）
            log.error("流式响应 - [模型标识({}) 请求过程中发生异常: {}]", chatRequest.getModel(), error.getMessage());
            // 异步删除创建的assistant聊天消息
            AsyncManager.me().execute(AsyncAiFactory.deleteAssistantMessage(assistantId));
            // 将异常信息设置为响应内容(有些错误可能是用户的配置问题,需要提示用户)
            responseVo.setContent(error.getMessage());
            return Flux.just(responseVo);
        });
    }

    /**
     * 创建聊天消息
     *
     * @param chatRequest 聊天请求
     * @return assistantId
     */
    private Long createChatMessage(ChatRequest chatRequest) {
        AiChatMessage message = new AiChatMessage();
        message.setConversationId(chatRequest.getConversationId());
        message.setReplyId(null);
        message.setModel(chatRequest.getModel());
        message.setModelId(chatRequest.getModelId());
        message.setUserId(SecurityUtils.getUserId());
        message.setRoleId(chatRequest.getRoleId());
        message.setType(MessageType.USER.getValue());
        message.setContent(chatRequest.getContent());
        message.setAttachmentUrls(chatRequest.getAttachmentUrls());
        chatMessageMapper.insert(message);
        // userMessageId
        message.setReplyId(message.getId());
        message.setId(null);
        message.setType(MessageType.ASSISTANT.getValue());
        message.setContent(StringUtils.EMPTY);
        chatMessageMapper.insert(message);
        // assistantMessageId
        return message.getId();
    }

    /**
     * 构建聊天消息
     *
     * @param chatRequest 聊天请求
     * @return 聊天消息列表
     */
    private List<Message> buildChatMessages(ChatRequest chatRequest) {
        // 构建聊天消息列表
        List<Message> chatMessages = new ArrayList<>();
        //  添加角色设定
        if (StrUtil.isNotBlank(chatRequest.getSystemMessage())) {
            chatMessages.add(new SystemMessage(chatRequest.getSystemMessage()));
        }
        // 添加历史消息
        chatMessages.addAll(buildHistoryMessages(chatRequest));
        // 添加发送消息
        chatMessages.add(new UserMessage(chatRequest.getContent()));
        return chatMessages;
    }

    /**
     * 构建历史消息
     *
     * @param chatRequest 聊天请求
     * @return 历史消息列表
     */
    private List<Message> buildHistoryMessages(ChatRequest chatRequest) {
        if (chatRequest.getMaxContexts() == null || chatRequest.getMaxContexts() <= 0) {
            return Collections.emptyList();
        }
        List<AiChatMessage> historyMessages = chatMessageMapper.selectListByConversationId(chatRequest.getConversationId());
        if (historyMessages.isEmpty()) {
            return Collections.emptyList();
        }
        List<AiChatMessage> contextMessages = new ArrayList<>(chatRequest.getMaxContexts() * 2);
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
            if (contextMessages.size() >= chatRequest.getMaxContexts() * 2) {
                break;
            }
        }
        Collections.reverse(contextMessages);
        return contextMessages.stream().map(SpringAiUtils::convertMessage).toList();
    }

}