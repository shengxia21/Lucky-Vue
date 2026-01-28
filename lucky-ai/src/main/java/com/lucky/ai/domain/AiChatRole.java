package com.lucky.ai.domain;

import com.lucky.common.core.domain.BaseEntity;

import java.util.List;

/**
 * AI 聊天角色 ai_chat_role
 *
 * @author lucky
 */
public class AiChatRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 角色名称 */
    private String name;

    /** 角色头像 */
    private String avatar;

    /** 角色分类 */
    private String category;

    /** 角色描述 */
    private String description;

    /** 角色设定 */
    private String systemMessage;

    /** 用户编号 */
    private Long userId;

    /** 模型编号 */
    private Long modelId;

    /** 引用的知识库编号列表 */
    private List<Long> knowledgeIds;

    /** 引用的工具编号列表 */
    private List<Long> toolIds;

    /** 引用的 MCP Client 名字列表 */
    private List<String> mcpClientNames;

    /** 是否公开 */
    private Boolean publicStatus;

    /** 排序值 */
    private Integer sort;

    /** 状态 */
    private Integer status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSystemMessage() {
        return systemMessage;
    }

    public void setSystemMessage(String systemMessage) {
        this.systemMessage = systemMessage;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public List<Long> getKnowledgeIds() {
        return knowledgeIds;
    }

    public void setKnowledgeIds(List<Long> knowledgeIds) {
        this.knowledgeIds = knowledgeIds;
    }

    public List<Long> getToolIds() {
        return toolIds;
    }

    public void setToolIds(List<Long> toolIds) {
        this.toolIds = toolIds;
    }

    public List<String> getMcpClientNames() {
        return mcpClientNames;
    }

    public void setMcpClientNames(List<String> mcpClientNames) {
        this.mcpClientNames = mcpClientNames;
    }

    public Boolean getPublicStatus() {
        return publicStatus;
    }

    public void setPublicStatus(Boolean publicStatus) {
        this.publicStatus = publicStatus;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

}
