package com.lucky.common.ai.chat.memory;

import com.lucky.common.ai.chat.aggregator.LuckyMessageAggregator;
import com.lucky.common.ai.domain.dto.ChatMessageDTO;
import com.lucky.common.ai.domain.request.ChatRequest;
import com.lucky.common.ai.util.SpringAiUtils;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.util.Assert;

import java.util.*;

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
    public void addUserMessage(Map<String, Object> context, Message message) {
        Assert.notNull(context, "context cannot be null");
        Assert.notNull(message, "message cannot be null");
        ChatRequest chatRequest = (ChatRequest) context.get(LuckyChatMemory.REQUEST);
        ChatMessageDTO messageDTO = new ChatMessageDTO();
        messageDTO.setConversationId(chatRequest.getConversationId());
        messageDTO.setType(MessageType.USER.getValue());
        messageDTO.setUserId(chatRequest.getUserId());
        messageDTO.setPlatform(chatRequest.getPlatform());
        messageDTO.setModel(chatRequest.getModel());
        messageDTO.setContent(message.getText());
        messageDTO.setAttachmentUrls(chatRequest.getAttachmentUrls());
        messageDTO.setDeptId(chatRequest.getDeptId());
        messageDTO.setUserName(chatRequest.getUserName());
        luckyChatMemoryRepository.save(messageDTO);
    }

    @Override
    public void addAssistantMessage(Map<String, Object> context, LuckyMessageAggregator.DefaultUsage usage, List<Message> messages) {
        Assert.notNull(context, "context cannot be null");
        Assert.notNull(messages, "messages cannot be null");
        Assert.noNullElements(messages, "messages cannot contain null elements");
        ChatRequest chatRequest = (ChatRequest) context.get(LuckyChatMemory.REQUEST);
        Message assistant = this.findFirstAssistantOrElseEmpty(messages);
        ChatMessageDTO messageDTO = new ChatMessageDTO();
        messageDTO.setConversationId(chatRequest.getConversationId());
        messageDTO.setType(MessageType.ASSISTANT.getValue());
        messageDTO.setUserId(chatRequest.getUserId());
        messageDTO.setPlatform(chatRequest.getPlatform());
        messageDTO.setModel(chatRequest.getModel());
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
        // historyMessageCount 实际为对话轮数（user+assistant 一对算一轮），每轮最多 2 条
        int limit = historyMessageCount * 2;
        List<ChatMessageDTO> messages = luckyChatMemoryRepository.findByConversationId(conversationId, limit);
        if (messages.isEmpty()) {
            return Collections.emptyList();
        }
        // 从后往前遍历，以用户+助手消息为一组，收集到 historyMessageCount 组即停止，避免无效遍历
        List<List<ChatMessageDTO>> groups = new ArrayList<>();
        List<ChatMessageDTO> currentGroup = new ArrayList<>();
        for (int i = messages.size() - 1; i >= 0; i--) {
            ChatMessageDTO msg = messages.get(i);
            if (MessageType.USER.getValue().equals(msg.getType())) {
                // 遇到用户消息，当前组收集完毕（用户 -> 助手 或 仅用户）
                currentGroup.add(msg);
                Collections.reverse(currentGroup);
                groups.add(currentGroup);
                currentGroup = new ArrayList<>();
            } else if (MessageType.ASSISTANT.getValue().equals(msg.getType())) {
                // 助手消息暂存，等待对应的用户消息
                currentGroup.add(msg);
            }
            if (groups.size() >= historyMessageCount) {
                break;
            }
        }
        // groups 为从新到旧排列，反转为从旧到新
        Collections.reverse(groups);
        // 展平并转换为 Spring AI Message
        return groups.stream()
                .flatMap(List::stream)
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
