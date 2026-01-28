package com.lucky.ai.controller.model.vo.chatRole;

/**
 * AI 聊天角色分页请求VO
 *
 * @author lucky
 */
public class AiChatRolePageReqVO {

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色类别
     */
    private String category;

    /**
     * 是否公开
     */
    private Boolean publicStatus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getPublicStatus() {
        return publicStatus;
    }

    public void setPublicStatus(Boolean publicStatus) {
        this.publicStatus = publicStatus;
    }

}