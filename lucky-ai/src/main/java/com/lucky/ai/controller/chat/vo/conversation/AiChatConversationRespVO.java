package com.lucky.ai.controller.chat.vo.conversation;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * AI 聊天对话响应VO
 *
 * @author lucky
 */
public class AiChatConversationRespVO {

    /**
     * 对话编号
     */
    private Long id;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 对话标题
     */
    private String title;

    /**
     * 是否置顶
     */
    private Boolean pinned;

    /**
     * 角色编号
     */
    private Long roleId;

    /**
     * 模型编号
     */
    private Long modelId;

    /**
     * 模型标志
     */
    private String model;

    /**
     * 模型名字
     */
    private String modelName;

    /**
     * 角色设定
     */
    private String systemMessage;

    /**
     * 温度参数
     */
    private Double temperature;

    /**
     * 单条回复的最大 Token 数量
     */
    private Integer maxTokens;

    /**
     * 上下文的最大 Message 数量
     */
    private Integer maxContexts;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    // ========== 关联 role 信息 ==========

    /**
     * 角色头像
     */
    private String roleAvatar;

    /**
     * 角色名字
     */
    private String roleName;

    // ========== 仅在【对话管理】时加载 ==========

    /**
     * 消息数量
     */
    private Integer messageCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getPinned() {
        return pinned;
    }

    public void setPinned(Boolean pinned) {
        this.pinned = pinned;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getSystemMessage() {
        return systemMessage;
    }

    public void setSystemMessage(String systemMessage) {
        this.systemMessage = systemMessage;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    public Integer getMaxContexts() {
        return maxContexts;
    }

    public void setMaxContexts(Integer maxContexts) {
        this.maxContexts = maxContexts;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRoleAvatar() {
        return roleAvatar;
    }

    public void setRoleAvatar(String roleAvatar) {
        this.roleAvatar = roleAvatar;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Integer messageCount) {
        this.messageCount = messageCount;
    }

}
