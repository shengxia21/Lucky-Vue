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
     * 通过会话id查询消息列表
     *
     * @param conversationId 会话id
     * @return 消息列表
     */
    List<ChatMessageDTO> findByConversationId(Long conversationId);

}
