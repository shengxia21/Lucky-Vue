package com.lucky.common.ai.chat.memory;

import com.lucky.common.ai.domain.dto.ChatMessageDTO;
import com.lucky.common.ai.domain.dto.ChatMessageUpdateDTO;

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
     * @return 保存后的消息元数据
     */
    ChatMessageUpdateDTO save(ChatMessageDTO chatMessage);

    /**
     * 通过会话id查询最近的消息列表
     *
     * @param conversationId 会话id
     * @param limit          最大查询条数
     * @return 最近 limit 条消息（按时间正序：旧 → 新，最新时间在下面）
     */
    List<ChatMessageDTO> findRecentByConversationId(Long conversationId, Integer limit);

}
