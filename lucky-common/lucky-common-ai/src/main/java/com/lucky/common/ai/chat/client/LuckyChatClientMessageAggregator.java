package com.lucky.common.ai.chat.client;

import com.lucky.common.ai.chat.model.LuckyMessageAggregator;
import com.lucky.common.ai.service.chat.ResponseContentExtractor;
import org.springframework.ai.chat.client.ChatClientResponse;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Lucky 聊天客户端消息聚合器
 *
 * @author lucky
 */
public class LuckyChatClientMessageAggregator {

    public Flux<ChatClientResponse> aggregateChatClientResponse(Flux<ChatClientResponse> chatClientResponses, ResponseContentExtractor extractor, Consumer<ChatClientResponse> aggregationHandler) {
        AtomicReference<Map<String, Object>> context = new AtomicReference<>(new HashMap<>());
        return (new LuckyMessageAggregator()).aggregate(chatClientResponses.mapNotNull((chatClientResponse) -> {
            context.get().putAll(chatClientResponse.context());
            return chatClientResponse.chatResponse();
        }), extractor, (aggregatedChatResponse) -> {
            ChatClientResponse aggregatedChatClientResponse = ChatClientResponse.builder().chatResponse(aggregatedChatResponse).context(context.get()).build();
            aggregationHandler.accept(aggregatedChatClientResponse);
        }).map((chatResponse) -> ChatClientResponse.builder().chatResponse(chatResponse).context(context.get()).build());
    }

}
