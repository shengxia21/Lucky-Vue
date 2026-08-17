package com.lucky.common.ai.chat.advisor;

import com.lucky.common.ai.chat.aggregator.LuckyChatClientMessageAggregator;
import com.lucky.common.ai.chat.aggregator.LuckyMessageAggregator;
import com.lucky.common.ai.chat.memory.LuckyChatMemory;
import com.lucky.common.ai.domain.request.ChatRequest;
import com.lucky.common.ai.service.chat.ResponseContentExtractor;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Lucky 聊天记忆顾问
 *
 * @author lucky
 */
public class LuckyMessageChatMemoryAdvisor implements BaseAdvisor {

    private final LuckyChatMemory chatMemory;
    private final int order;
    private final Scheduler scheduler;
    private final ResponseContentExtractor extractor;

    private LuckyMessageChatMemoryAdvisor(LuckyChatMemory chatMemory, ResponseContentExtractor extractor, int order, Scheduler scheduler) {
        Assert.notNull(chatMemory, "chatMemory cannot be null");
        Assert.notNull(extractor, "extractor cannot be null");
        Assert.notNull(scheduler, "scheduler cannot be null");
        this.chatMemory = chatMemory;
        this.extractor = extractor;
        this.order = order;
        this.scheduler = scheduler;
    }

    private ChatRequest getRequest(Map<String, Object> context) {
        Assert.notNull(context, "context cannot be null");
        Assert.noNullElements(context.keySet().toArray(), "context cannot contain null keys");
        Assert.notNull(context.get(LuckyChatMemory.REQUEST), "chat_request cannot be null");
        return (ChatRequest) context.get(LuckyChatMemory.REQUEST);
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public Scheduler getScheduler() {
        return this.scheduler;
    }

    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        ChatRequest request = this.getRequest(chatClientRequest.context());
        List<Message> memoryMessages = this.chatMemory.get(request.getConversationId(), request.getHistoryMessageCount());
        List<Message> processedMessages = new ArrayList<>(memoryMessages);
        processedMessages.addAll(chatClientRequest.prompt().getInstructions());

        for (int i = 0; i < processedMessages.size(); ++i) {
            if (processedMessages.get(i) instanceof SystemMessage) {
                Message systemMessage = processedMessages.remove(i);
                processedMessages.add(0, systemMessage);
                break;
            }
        }

        ChatClientRequest processedChatClientRequest = chatClientRequest.mutate().prompt(chatClientRequest.prompt().mutate().messages(processedMessages).build()).build();
        Message userMessage = processedChatClientRequest.prompt().getLastUserOrToolResponseMessage();
        this.chatMemory.addUserMessage(chatClientRequest.context(), userMessage);
        return processedChatClientRequest;
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        List<Message> assistantMessages = new ArrayList<>();
        LuckyMessageAggregator.DefaultUsage usage = null;
        if (chatClientResponse.chatResponse() != null) {
            assistantMessages = chatClientResponse.chatResponse().getResults().stream().map((g) -> (Message) g.getOutput()).toList();
            usage = (LuckyMessageAggregator.DefaultUsage) chatClientResponse.chatResponse().getMetadata().getUsage();
        }

        this.chatMemory.addAssistantMessage(chatClientResponse.context(), usage, assistantMessages);
        return chatClientResponse;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        Scheduler scheduler = this.getScheduler();
        Mono<ChatClientRequest> var10000 = Mono.just(chatClientRequest).publishOn(scheduler).map((request) -> this.before(request, streamAdvisorChain));
        Objects.requireNonNull(streamAdvisorChain);
        return var10000.flatMapMany(streamAdvisorChain::nextStream).transform((flux) -> (new LuckyChatClientMessageAggregator()).aggregateChatClientResponse(flux, this.extractor, (response) -> this.after(response, streamAdvisorChain)));
    }

    public static Builder builder(LuckyChatMemory chatMemory, ResponseContentExtractor extractor) {
        return new Builder(chatMemory, extractor);
    }

    public static final class Builder {

        private int order = Advisor.DEFAULT_CHAT_MEMORY_PRECEDENCE_ORDER;
        private Scheduler scheduler = Schedulers.boundedElastic();
        private final LuckyChatMemory chatMemory;
        private final ResponseContentExtractor extractor;

        private Builder(LuckyChatMemory chatMemory, ResponseContentExtractor extractor) {
            Assert.notNull(chatMemory, "chatMemory cannot be null");
            Assert.notNull(extractor, "extractor cannot be null");
            this.chatMemory = chatMemory;
            this.extractor = extractor;
        }

        public Builder order(int order) {
            this.order = order;
            return this;
        }

        public Builder scheduler(Scheduler scheduler) {
            this.scheduler = scheduler;
            return this;
        }

        public LuckyMessageChatMemoryAdvisor build() {
            return new LuckyMessageChatMemoryAdvisor(this.chatMemory, this.extractor, this.order, this.scheduler);
        }

    }

}
