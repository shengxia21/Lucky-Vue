package com.lucky.ai.domain.vo.message;

import lombok.Data;

/**
 * 聊天消息数量 Response VO
 *
 * @author lucky
 */
@Data
public class ChatMessageCountVO {

    /**
     * 会话ID
     */
    private Long conversationId;

    /**
     * 消息数量
     */
    private Integer count;

}
