package com.lucky.ai.chain.handler.impl;

import com.lucky.ai.chain.context.ChatStreamContext;
import com.lucky.ai.chain.handler.ChatStreamHandler;
import com.lucky.common.ai.domain.request.ChatRequest;
import com.lucky.common.ai.service.chat.ChatService;
import com.lucky.common.security.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * LLM 大模型调用处理器（链尾处理器）
 *
 * @author lucky
 */
@Component
@Order(1000)
public class LlmChatHandler implements ChatStreamHandler {

    @Resource
    private ChatService chatService;

    @Override
    public String getName() {
        return "LLM大模型调用处理器";
    }

    @Override
    public void handle(ChatStreamContext context) {
        // 构建聊天请求
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setUseThinking(context.getQuery().getUseThinking());
        chatRequest.setUseSearch(context.getQuery().getUseSearch());
        chatRequest.setAttachmentUrls(context.getQuery().getAttachmentUrls());
        chatRequest.setConversationId(context.getConversation().getId());
        chatRequest.setHistoryMessageCount(context.getConversation().getHistoryMessageCount());
        chatRequest.setPersona(context.getRole().getPersona());
        chatRequest.setProvider(context.getModel().getProvider());
        chatRequest.setModel(context.getModel().getModel());
        chatRequest.setApiKey(context.getApiKey().getApiKey());
        chatRequest.setUrl(context.getApiKey().getUrl());
        chatRequest.setMessages(context.getMessages());
        chatRequest.setUserId(SecurityUtils.getUserId());
        chatRequest.setUserName(SecurityUtils.getUserName());
        chatRequest.setDeptId(SecurityUtils.getDeptId());
        // 调用处理器处理流式聊天，将结果流写入上下文
        context.setResult(chatService.chat(chatRequest));
    }

}
