package com.lucky.ai.domain.query.conversation;

import lombok.Data;

/**
 * 创建我的聊天对话请求对象
 *
 * @author lucky
 */
@Data
public class AiChatConversationCreateMyQuery {

    /**
     * 聊天角色编号
     */
    private Long roleId;

    /**
     * 聊天内容
     */
    private String content;

}
