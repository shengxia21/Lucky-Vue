package com.lucky.ai.chain;

import com.lucky.ai.chain.context.ChatStreamContext;
import com.lucky.common.ai.domain.request.ChatRequest;
import com.lucky.common.ai.service.chat.ChatService;
import com.lucky.common.security.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * LLM 大模型调用执行器
 *
 * @author lucky
 */
@Component
public class LlmChatExecutor {

    @Resource
    private ChatService chatService;

    /**
     * 调用 LLM 大模型流式聊天
     *
     * @param context 责任链上下文
     * @return 结果流（SSE 事件流）
     */
    public Flux<ServerSentEvent<String>> chat(ChatStreamContext context) {
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
        // 返回流式聊天结果流
        return chatService.chat(chatRequest);
    }

}
