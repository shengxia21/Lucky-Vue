package com.lucky.ai.controller.chat.vo.conversation;

/**
 * 创建我的聊天对话请求VO
 *
 * @author lucky
 */
public class AiChatConversationCreateMyReqVO {

    /**
     * 聊天角色编号
     */
    private Long roleId;

    /**
     * 知识库编号
     */
    private Long knowledgeId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(Long knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

}
