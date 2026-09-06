package com.lucky.common.ai.chat.memory.impl;

import com.lucky.common.ai.chat.memory.AbstractChatMemoryRepository;
import com.lucky.common.ai.domain.dto.ChatMessageDTO;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Lucky 内存聊天记忆存储库
 *
 * @author lucky
 */
public class LuckyInMemoryChatMemoryRepository extends AbstractChatMemoryRepository {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    Map<Long, List<ChatMessageDTO>> chatMemoryStore = new ConcurrentHashMap<>();

    @Override
    protected StoredMessageIdentity doSave(ChatMessageDTO chatMessage) {
        List<ChatMessageDTO> messages = this.chatMemoryStore.get(chatMessage.getConversationId());
        if (messages == null) {
            chatMemoryStore.put(chatMessage.getConversationId(), new ArrayList<>(List.of(chatMessage)));
        } else {
            messages.add(chatMessage);
        }
        // 内存存储：自增序列生成编号，当前时间作为创建时间
        return new StoredMessageIdentity(ID_GENERATOR.getAndIncrement(), LocalDateTime.now());
    }

    @Override
    public List<ChatMessageDTO> findRecentByConversationId(Long conversationId, Integer limit) {
        Assert.notNull(conversationId, "conversationId cannot be null");
        Assert.notNull(limit, "limit cannot be null");
        Assert.isTrue(limit > 0, "limit must be greater than 0");
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
