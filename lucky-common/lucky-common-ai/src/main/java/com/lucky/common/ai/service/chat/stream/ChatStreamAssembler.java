package com.lucky.common.ai.service.chat.stream;

import com.lucky.common.ai.cache.ChatModelCache;
import com.lucky.common.ai.chat.advisor.LuckyMessageChatMemoryAdvisor;
import com.lucky.common.ai.chat.emitter.ChatMessageUpdateEmitter;
import com.lucky.common.ai.chat.memory.LuckyChatMemory;
import com.lucky.common.ai.domain.request.ChatRequest;
import com.lucky.common.ai.service.chat.AbstractChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;

/**
 * 聊天流装配器
 *
 * @author lucky
 */
public class ChatStreamAssembler {

    private final ChatModelCache chatModelCache;

    private final LuckyChatMemory luckyChatMemory;

    public ChatStreamAssembler(ChatModelCache chatModelCache, LuckyChatMemory luckyChatMemory) {
        this.chatModelCache = chatModelCache;
        this.luckyChatMemory = luckyChatMemory;
    }

    /**
     * 装配 ChatClient
     *
     * @param chatRequest   聊天请求
     * @param service       当前提供商的聊天服务（用于构建模型与选项）
     * @param updateEmitter 请求作用域的消息元数据收集器（注入记忆顾问，落库后回调）
     * @return 装配完成的 ChatClient
     */
    public ChatClient assemble(ChatRequest chatRequest, AbstractChatService service, ChatMessageUpdateEmitter updateEmitter) {
        // 构建聊天选项
        ChatOptions chatOptions = service.buildChatOptions(chatRequest);
        // 构建聊天模型（优先从缓存复用，避免每次请求重建 HTTP 客户端与连接池）
        ChatModel chatModel = chatModelCache.getOrCreate(
                service.getProviderName(),
                chatRequest.getUrl(),
                chatRequest.getApiKey(),
                () -> service.buildChatModel(chatRequest.getUrl(), chatRequest.getApiKey())
        );
        // 记忆顾问：落库产生的元数据经 updateEmitter 回调流出
        LuckyMessageChatMemoryAdvisor memoryAdvisor = LuckyMessageChatMemoryAdvisor
                .builder(luckyChatMemory, service)
                .updateEmitter(updateEmitter)
                .build();
        return ChatClient.builder(chatModel)
                .defaultOptions(chatOptions)
                .defaultAdvisors(memoryAdvisor)
                .build();
    }

}
