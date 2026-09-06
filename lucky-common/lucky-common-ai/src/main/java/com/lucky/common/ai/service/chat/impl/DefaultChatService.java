package com.lucky.common.ai.service.chat.impl;

import cn.hutool.core.util.StrUtil;
import com.lucky.common.ai.chat.emitter.ChatMessageUpdateEmitter;
import com.lucky.common.ai.domain.request.ChatRequest;
import com.lucky.common.ai.factory.ChatServiceFactory;
import com.lucky.common.ai.factory.SseEventFactory;
import com.lucky.common.ai.service.chat.AbstractChatService;
import com.lucky.common.ai.service.chat.ChatService;
import com.lucky.common.ai.service.chat.stream.ChatStreamAssembler;
import com.lucky.common.ai.service.chat.stream.ChatStreamPipeline;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

/**
 * 默认聊天服务（外观）
 *
 * @author lucky
 */
@Slf4j
public class DefaultChatService implements ChatService {

    @Resource
    private ChatServiceFactory chatFactory;

    @Resource
    private ChatStreamAssembler chatStreamAssembler;

    @Resource
    private ChatStreamPipeline chatStreamPipeline;

    @Override
    public Flux<ServerSentEvent<String>> chat(ChatRequest chatRequest) {
        AbstractChatService service;
        try {
            // 参数校验（attachmentUrls、persona、url 允许为 null/空）
            this.validateChatRequest(chatRequest);
            // 获取聊天服务
            service = chatFactory.getOriginalService(chatRequest.getProvider());
        } catch (Exception e) {
            // 前置逻辑异常：直接返回 error + done 事件
            log.error("AI 流式聊天前置逻辑异常: error={}", e.getMessage());
            return Flux.just(SseEventFactory.errorEvent(e.getMessage()), SseEventFactory.doneEvent());
        }

        // 内层 defer：每次订阅都创建独立的元数据收集器并重新装配/编排，保证返回的流可重复订阅
        return Flux.defer(() -> {
            // 元数据收集器（按请求创建）：记忆顾问落库后回调，元数据作为 update 事件显式流出
            ChatMessageUpdateEmitter updateEmitter = new ChatMessageUpdateEmitter();
            // 装配 ChatClient（含记忆顾问）
            ChatClient chatClient = chatStreamAssembler.assemble(chatRequest, service, updateEmitter);
            // 编排最终 SSE 事件流
            return chatStreamPipeline.stream(chatClient, chatRequest, service, updateEmitter);
        });
    }

    /**
     * 校验聊天请求参数
     * <p>String 类型参数校验非空字符（拦截 null、空串、纯空白），其余类型校验非 null;
     * 其中 attachmentUrls、persona、url 允许为 null/空</p>
     *
     * @param chatRequest 聊天请求
     */
    private void validateChatRequest(ChatRequest chatRequest) {
        if (chatRequest == null) {
            throw new IllegalArgumentException("聊天请求参数不能为空");
        }
        if (chatRequest.getMessages() == null || chatRequest.getMessages().isEmpty()) {
            throw new IllegalArgumentException("消息列表(messages)不能为空");
        }
        if (chatRequest.getEnableThinking() == null) {
            throw new IllegalArgumentException("是否开启深度思考(enableThinking)不能为空");
        }
        if (chatRequest.getEnableSearch() == null) {
            throw new IllegalArgumentException("是否开启联网搜索(enableSearch)不能为空");
        }
        if (chatRequest.getConversationId() == null) {
            throw new IllegalArgumentException("会话ID(conversationId)不能为空");
        }
        if (chatRequest.getHistoryMessageCount() == null || chatRequest.getHistoryMessageCount() <= 0) {
            throw new IllegalArgumentException("携带历史消息数(historyMessageCount)不能为空并且必须大于0");
        }
        if (StrUtil.isBlank(chatRequest.getModel())) {
            throw new IllegalArgumentException("模型(model)不能为空");
        }
        if (StrUtil.isBlank(chatRequest.getProvider())) {
            throw new IllegalArgumentException("提供商(provider)不能为空");
        }
        if (StrUtil.isBlank(chatRequest.getApiKey())) {
            throw new IllegalArgumentException("密钥(apiKey)不能为空");
        }
        if (chatRequest.getUserId() == null) {
            throw new IllegalArgumentException("用户ID(userId)不能为空");
        }
        if (StrUtil.isBlank(chatRequest.getUserName())) {
            throw new IllegalArgumentException("用户名称(userName)不能为空");
        }
    }

}
