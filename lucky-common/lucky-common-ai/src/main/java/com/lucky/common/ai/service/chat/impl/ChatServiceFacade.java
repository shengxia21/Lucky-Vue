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
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 聊天服务外观类
 *
 * @author lucky
 */
@Slf4j
public class ChatServiceFacade implements ChatService {

    @Resource
    private ChatServiceFactory chatFactory;

    @Resource
    private LuckyChatMemory luckyChatMemory;

    @Resource
    private ChatModelCache chatModelCache;

    @Override
    public Flux<ServerSentEvent<String>> chat(ChatRequest chatRequest) {
        // 参数校验（attachmentUrls、persona、url 允许为 null）
        this.validateChatRequest(chatRequest);
        // 获取聊天服务
        AbstractChatService service = chatFactory.getOriginalService(chatRequest.getPlatform());
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

        // 添加系统消息（聊天角色）
        List<Message> messages = new ArrayList<>();
        if (StrUtil.isNotBlank(chatRequest.getPersona())) {
            messages.add(new SystemMessage(chatRequest.getPersona()));
        }
        // 添加用户消息
        messages.add(new UserMessage(chatRequest.getContent()));
        return chatClient.prompt()
                .messages(messages)
                .options(chatOptions)
                .advisors(a -> a.param(LuckyChatMemory.REQUEST, chatRequest))
                .stream()
                .chatResponse()
                // 将每个 ChatResponse chunk 转为 SSE 命名事件（思考内容 → thinking、正文 → text）
                .concatMapIterable(response -> SseEventFactory.toEvents(response, service))
                // 超时控制：模型 API 卡死时及时释放连接（默认 30 秒）
                .timeout(Duration.ofSeconds(30))
                // 客户端取消时记录日志（下游模型 HTTP 调用由 Reactor 自动取消）
                .doOnCancel(() -> log.warn("AI 流式聊天被客户端取消: conversationId={}", chatRequest.getConversationId()))
                // 异常兜底：直接向 SSE 推送 error 事件，前端可识别并提示用户
                .onErrorResume(error -> {
                    log.error("AI 流式聊天异常: conversationId={}, error={}", chatRequest.getConversationId(), error.getMessage());
                    return Flux.just(SseEventFactory.errorEvent(error.getMessage()));
                })
                // 流结束：推送 done 事件，标识本次流式传输正常结束
                .concatWithValues(SseEventFactory.doneEvent());
    }

    /**
     * 校验聊天请求参数
     * <p>除 attachmentUrls、persona、url 外，其余参数均不可为 null</p>
     *
     * @param chatRequest 聊天请求
     */
    private void validateChatRequest(ChatRequest chatRequest) {
        if (chatRequest == null) {
            throw new IllegalArgumentException("聊天请求参数不能为空");
        }
        if (chatRequest.getContent() == null) {
            throw new IllegalArgumentException("聊天内容(content)不能为空");
        }
        if (chatRequest.getUseThinking() == null) {
            throw new IllegalArgumentException("是否深度思考(useThinking)不能为空");
        }
        if (chatRequest.getUseSearch() == null) {
            throw new IllegalArgumentException("是否联网搜索(useSearch)不能为空");
        }
        if (chatRequest.getConversationId() == null) {
            throw new IllegalArgumentException("会话ID(conversationId)不能为空");
        }
        if (chatRequest.getHistoryMessageCount() == null) {
            throw new IllegalArgumentException("携带历史消息数(historyMessageCount)不能为空");
        }
        if (chatRequest.getModel() == null) {
            throw new IllegalArgumentException("模型(model)不能为空");
        }
        if (chatRequest.getPlatform() == null) {
            throw new IllegalArgumentException("平台(platform)不能为空");
        }
        if (chatRequest.getApiKey() == null) {
            throw new IllegalArgumentException("密钥(apiKey)不能为空");
        }
        if (chatRequest.getUserId() == null) {
            throw new IllegalArgumentException("用户ID(userId)不能为空");
        }
        if (chatRequest.getUserName() == null) {
            throw new IllegalArgumentException("用户名称(userName)不能为空");
        }
    }

}