package com.lucky.common.ai.service.chat.impl;

import cn.hutool.core.util.StrUtil;
import com.lucky.common.ai.cache.ChatModelCache;
import com.lucky.common.ai.chat.advisor.LuckyMessageChatMemoryAdvisor;
import com.lucky.common.ai.chat.memory.LuckyChatMemory;
import com.lucky.common.ai.domain.request.ChatRequest;
import com.lucky.common.ai.factory.ChatServiceFactory;
import com.lucky.common.ai.factory.SseEventFactory;
import com.lucky.common.ai.service.chat.AbstractChatService;
import com.lucky.common.ai.service.chat.ChatService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

import java.time.Duration;

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
    private LuckyChatMemory luckyChatMemory;

    @Resource
    private ChatModelCache chatModelCache;

    @Override
    public Flux<ServerSentEvent<String>> chat(ChatRequest chatRequest) {
        // 前置逻辑产物
        AbstractChatService service;
        try {
            // 参数校验（attachmentUrls、persona、url 允许为 null/空）
            this.validateChatRequest(chatRequest);
            // 获取聊天服务
            service = chatFactory.getOriginalService(chatRequest.getProvider());
        } catch (Exception e) {
            // 前置逻辑异常：直接返回 error + done 事件
            log.error("AI 流式聊天前置逻辑异常: conversationId={}, error={}", chatRequest.getConversationId(), e.getMessage());
            return Flux.just(SseEventFactory.errorEvent(e.getMessage()), SseEventFactory.doneEvent());
        }

        // 构建聊天选项
        ChatOptions chatOptions = service.buildChatOptions(chatRequest);
        // 构建聊天模型（优先从缓存复用，避免每次请求重建 HTTP 客户端与连接池）
        ChatModel chatModel = chatModelCache.getOrCreate(
                service.getProviderName(),
                chatRequest.getUrl(),
                chatRequest.getApiKey(),
                () -> service.buildChatModel(chatRequest.getUrl(), chatRequest.getApiKey())
        );
        // 构建聊天客户端
        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(LuckyMessageChatMemoryAdvisor.builder(luckyChatMemory, service).build())
                .build();

        // 调用 LLM 大模型流式请求
        return chatClient.prompt()
                .options(chatOptions)
                .messages(chatRequest.getMessages())
                .advisors(a -> a.param(LuckyChatMemory.REQUEST, chatRequest))
                .stream()
                .chatResponse()
                // 超时控制：模型 API 卡死时及时释放连接（默认 15 秒）
                .timeout(Duration.ofSeconds(15))
                // 客户端取消时记录日志（下游模型 HTTP 调用由 Reactor 自动取消）
                .doOnCancel(() -> log.warn("AI 流式聊天被客户端取消: conversationId={}", chatRequest.getConversationId()))
                // 将每个 ChatResponse chunk 转为 SSE 命名事件（思考内容 → thinking、正文 → text）
                .concatMapIterable(response -> SseEventFactory.toEvents(response, service))
                // 流式请求异常兜底：直接向 SSE 推送 error 事件，前端可识别并提示用户
                .onErrorResume(error -> {
                    log.error("AI 流式聊天异常: conversationId={}, error={}", chatRequest.getConversationId(), error.getMessage());
                    return Flux.just(SseEventFactory.errorEvent(error.getMessage()));
                })
                // 流结束：推送 done 事件，标识本次流式传输正常结束
                .concatWithValues(SseEventFactory.doneEvent());
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