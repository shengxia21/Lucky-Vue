package com.lucky.common.ai.chat.memory;

import com.lucky.common.ai.domain.dto.ChatMessageDTO;

import java.util.List;

/**
 * Lucky 聊天记忆存储库
 *
 * @author lucky
 */
public interface LuckyChatMemoryRepository {

    /**
     * 保存方法
     *
     * @param chatMessage 聊天消息
     */
    void save(ChatMessageDTO chatMessage);

    /**
     * 通过会话id查询最近的消息列表（限制条数，按创建时间正序：旧 → 新）
     * <p>用于流式聊天上下文加载，避免长会话全量查询导致性能下降</p>
     *
     * @param conversationId 会话id
     * @param limit          最大查询条数
     * @return 消息列表（按时间正序：旧 → 新，最新时间在下面）
     */
    List<ChatMessageDTO> findByConversationId(Long conversationId, int limit);

}
