package com.lucky.ai.controller.chat.vo.conversation;

import lombok.Data;

/**
 * 创建我的聊天对话请求VO
 *
 * @author lucky
 */
@Data
public class AiChatConversationCreateMyReqVO {

    /**
     * 聊天角色编号
     */
    private Long roleId;

    /**
     * 知识库编号
     */
    private Long knowledgeId;

}