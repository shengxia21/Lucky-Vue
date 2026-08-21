package com.lucky.common.ai.chat.memory.impl;

import com.lucky.common.ai.chat.memory.LuckyChatMemoryRepository;
import com.lucky.common.ai.domain.dto.ChatMessageDTO;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lucky 内存聊天存储库
 *
 * @author lucky
 */
public class LuckyInMemoryChatMemoryRepository implements LuckyChatMemoryRepository {

    Map<Long, List<ChatMessageDTO>> chatMemoryStore = new ConcurrentHashMap<>();

    @Override
    public void save(ChatMessageDTO chatMessage) {
        Long conversationId = chatMessage.getConversationId();
        Assert.notNull(chatMessage, "chatMessage cannot be null");
        Assert.notNull(conversationId, "conversationId cannot be null");
        List<ChatMessageDTO> messages = this.chatMemoryStore.get(conversationId);
        if (messages == null || messages.isEmpty()) {
            chatMemoryStore.put(conversationId, new ArrayList<>(List.of(chatMessage)));
        } else {
            messages.add(chatMessage);
            chatMemoryStore.put(conversationId, messages);
        }
    }

    @Override
    public List<ChatMessageDTO> findRecentByConversationId(Long conversationId, int limit) {
        Assert.notNull(conversationId, "conversationId cannot be null");
        List<ChatMessageDTO> messages = this.chatMemoryStore.get(conversationId);
        if (messages == null || messages.isEmpty()) {
            return List.of();
        }
        // 内存场景：取尾部 limit 条（最新的），保持正序
        int size = messages.size();
        if (size <= limit) {
            return new ArrayList<>(messages);
        }
        return new ArrayList<>(messages.subList(size - limit, size));
    }

}
