package com.lucky.common.ai.chat.memory;

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
        Assert.notNull(chatMessage, "chatMessage cannot be null");
        Long conversationId = chatMessage.getConversationId();
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
    public List<ChatMessageDTO> findByConversationId(Long conversationId) {
        Assert.hasText(conversationId.toString(), "conversationId cannot be null or empty");
        List<ChatMessageDTO> messages = this.chatMemoryStore.get(conversationId);
        return (messages != null ? messages : List.of());
    }

}
