package com.lucky.common.ai.chat.memory.impl;

import com.lucky.common.ai.chat.aggregator.LuckyMessageAggregator;
import com.lucky.common.ai.chat.memory.LuckyChatMemory;
import com.lucky.common.ai.chat.memory.LuckyChatMemoryRepository;
import com.lucky.common.ai.domain.dto.ChatMessageDTO;
import com.lucky.common.ai.domain.dto.ChatMessageUpdateDTO;
import com.lucky.common.ai.domain.request.ChatRequest;
import com.lucky.common.ai.util.SpringAiUtils;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

/**
 * Lucky 消息窗口聊天记忆实现
 *
 * @author lucky
 */
public class LuckyMessageWindowChatMemory implements LuckyChatMemory {

    private final LuckyChatMemoryRepository luckyChatMemoryRepository;

    private LuckyMessageWindowChatMemory(LuckyChatMemoryRepository luckyChatMemoryRepository) {
        Assert.notNull(luckyChatMemoryRepository, "luckyChatMemoryRepository cannot be null");
        this.luckyChatMemoryRepository = luckyChatMemoryRepository;
    }

    @Override
    public ChatMessageUpdateDTO addUserMessage(ChatRequest chatRequest, Message message) {
        Assert.notNull(chatRequest, "chatRequest cannot be null");
        Assert.notNull(message, "message cannot be null");
        // 依据用户消息与请求上下文构建消息 DTO
        ChatMessageDTO messageDTO = ChatMessageDTO.builder()
                .userId(chatRequest.getUserId())
                .conversationId(chatRequest.getConversationId())
                .provider(chatRequest.getProvider())
                .model(chatRequest.getModel())
                .type(MessageType.USER.getValue())
                .systemMessage(chatRequest.getPersona())
                .content(message.getText())
                .attachmentUrls(chatRequest.getAttachmentUrls())
                .deptId(chatRequest.getDeptId())
                .userName(chatRequest.getUserName())
                .build();
        return luckyChatMemoryRepository.save(messageDTO);
    }

    @Override
    public ChatMessageUpdateDTO addAssistantMessage(ChatRequest chatRequest, LuckyMessageAggregator.DefaultUsage usage, List<Message> messages) {
        Assert.notNull(chatRequest, "context cannot be null");
        Assert.notNull(messages, "messages cannot be null");
        Assert.noNullElements(messages, "messages cannot contain null elements");
        Message assistant = SpringAiUtils.findFirstAssistantOrElseEmpty(messages);
        String reasoningContent = assistant.getMetadata().getOrDefault("reasoningContent", "").toString();
        // 依据助手消息、请求上下文与聚合用量构建消息 DTO
        ChatMessageDTO messageDTO = ChatMessageDTO.builder()
                .userId(chatRequest.getUserId())
                .conversationId(chatRequest.getConversationId())
                .provider(chatRequest.getProvider())
                .model(chatRequest.getModel())
                .type(MessageType.ASSISTANT.getValue())
                .systemMessage(chatRequest.getPersona())
                .content(assistant.getText())
                .reasoningContent(reasoningContent)
                .promptTokens(usage == null ? null : usage.getPromptTokens())
                .completionTokens(usage == null ? null : usage.getCompletionTokens())
                .totalTokens(usage == null ? null : usage.getTotalTokens())
                .deptId(chatRequest.getDeptId())
                .userName(chatRequest.getUserName())
                .build();
        return luckyChatMemoryRepository.save(messageDTO);
    }

    @Override
    public List<Message> get(Long conversationId, Integer historyMessageCount) {
        Assert.notNull(conversationId, "conversationId cannot be null");
        Assert.notNull(historyMessageCount, "historyMessageCount cannot be null");
        if (historyMessageCount <= 0) {
            return Collections.emptyList();
        }
        /// TODO 前置保证：messages 中数据严格 user+assistant 成对、顺序规整
        int limit = historyMessageCount * 2;
        List<ChatMessageDTO> messages = luckyChatMemoryRepository.findRecentByConversationId(conversationId, limit);
        if (messages.isEmpty()) {
            return Collections.emptyList();
        }
        return messages.stream()
                .map(SpringAiUtils::convertMessage)
                .toList();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private LuckyChatMemoryRepository luckyChatMemoryRepository;

        private Builder() {
        }

        public Builder luckyChatMemoryRepository(LuckyChatMemoryRepository luckyChatMemoryRepository) {
            this.luckyChatMemoryRepository = luckyChatMemoryRepository;
            return this;
        }

        public LuckyMessageWindowChatMemory build() {
            if (this.luckyChatMemoryRepository == null) {
                this.luckyChatMemoryRepository = new LuckyInMemoryChatMemoryRepository();
            }
            return new LuckyMessageWindowChatMemory(this.luckyChatMemoryRepository);
        }

    }

}
