package com.lucky.common.ai.chat.memory.impl;

import com.lucky.common.ai.chat.aggregator.LuckyMessageAggregator;
import com.lucky.common.ai.chat.memory.LuckyChatMemory;
import com.lucky.common.ai.chat.memory.LuckyChatMemoryRepository;
import com.lucky.common.ai.domain.dto.ChatMessageDTO;
import com.lucky.common.ai.domain.request.ChatRequest;
import com.lucky.common.ai.util.SpringAiUtils;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    public void addUserMessage(ChatRequest chatRequest, Message message) {
        Assert.notNull(chatRequest, "chatRequest cannot be null");
        Assert.notNull(message, "message cannot be null");
        ChatMessageDTO messageDTO = new ChatMessageDTO();
        messageDTO.setUserId(chatRequest.getUserId());
        messageDTO.setConversationId(chatRequest.getConversationId());
        messageDTO.setProvider(chatRequest.getProvider());
        messageDTO.setModel(chatRequest.getModel());
        messageDTO.setType(MessageType.USER.getValue());
        messageDTO.setSystemMessage(chatRequest.getPersona());
        messageDTO.setContent(message.getText());
        messageDTO.setAttachmentUrls(chatRequest.getAttachmentUrls());
        messageDTO.setDeptId(chatRequest.getDeptId());
        messageDTO.setUserName(chatRequest.getUserName());
        luckyChatMemoryRepository.save(messageDTO);
    }

    @Override
    public void addAssistantMessage(ChatRequest chatRequest, LuckyMessageAggregator.DefaultUsage usage, List<Message> messages) {
        Assert.notNull(chatRequest, "context cannot be null");
        Assert.notNull(messages, "messages cannot be null");
        Assert.noNullElements(messages, "messages cannot contain null elements");
        Message assistant = this.findFirstAssistantOrElseEmpty(messages);
        ChatMessageDTO messageDTO = new ChatMessageDTO();
        messageDTO.setUserId(chatRequest.getUserId());
        messageDTO.setConversationId(chatRequest.getConversationId());
        messageDTO.setProvider(chatRequest.getProvider());
        messageDTO.setModel(chatRequest.getModel());
        messageDTO.setType(MessageType.ASSISTANT.getValue());
        messageDTO.setSystemMessage(chatRequest.getPersona());
        messageDTO.setContent(assistant.getText());
        String reasoningContent = assistant.getMetadata().getOrDefault("reasoningContent", "").toString();
        messageDTO.setReasoningContent(reasoningContent);
        if (usage != null) {
            messageDTO.setPromptTokens(usage.getPromptTokens());
            messageDTO.setCompletionTokens(usage.getCompletionTokens());
            messageDTO.setTotalTokens(usage.getTotalTokens());
        }
        messageDTO.setDeptId(chatRequest.getDeptId());
        messageDTO.setUserName(chatRequest.getUserName());
        luckyChatMemoryRepository.save(messageDTO);
    }

    @Override
    public List<Message> get(Long conversationId, Integer historyMessageCount) {
        Assert.notNull(conversationId, "conversationId cannot be null");
        Assert.notNull(historyMessageCount, "historyMessageCount cannot be null");
        if (historyMessageCount <= 0) {
            return Collections.emptyList();
        }
        /// TO DO 前置保证：messages 中数据严格 user+assistant 成对、顺序规整
        int limit = historyMessageCount * 2;
        List<ChatMessageDTO> messages = luckyChatMemoryRepository.findRecentByConversationId(conversationId, limit);
        if (messages.isEmpty()) {
            return Collections.emptyList();
        }
        return messages.stream()
                .map(SpringAiUtils::convertMessage)
                .toList();
    }

    private Message findFirstAssistantOrElseEmpty(List<Message> messages) {
        Optional<Message> optional = messages.stream().filter(f -> f.getMessageType().equals(MessageType.ASSISTANT)).findFirst();
        return optional.orElseGet(() -> new AssistantMessage(""));
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
