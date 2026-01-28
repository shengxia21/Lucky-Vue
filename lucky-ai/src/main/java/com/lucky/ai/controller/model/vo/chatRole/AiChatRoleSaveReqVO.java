package com.lucky.ai.controller.model.vo.chatRole;

import com.lucky.ai.enums.CommonStatusEnum;
import com.lucky.common.validation.InEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * AI 聊天角色新增/修改请求VO
 *
 * @author lucky
 */
public class AiChatRoleSaveReqVO {

    /**
     * 角色编号
     */
    private Long id;

    /**
     * 模型编号
     */
    private Long modelId;

    /**
     * 角色名称
     */
    @NotEmpty(message = "角色名称不能为空")
    private String name;

    /**
     * 角色头像
     */
    @NotEmpty(message = "角色头像不能为空")
    private String avatar;

    /**
     * 角色类别
     */
    @NotEmpty(message = "角色类别不能为空")
    private String category;

    /**
     * 角色排序
     */
    @NotNull(message = "角色排序不能为空")
    private Integer sort;

    /**
     * 角色描述
     */
    @NotEmpty(message = "角色描述不能为空")
    private String description;

    /**
     * 角色设定
     */
    @NotEmpty(message = "角色设定不能为空")
    private String systemMessage;

    /**
     * 引用的知识库编号列表
     */
    private List<Long> knowledgeIds;

    /**
     * 引用的工具编号列表
     */
    private List<Long> toolIds;

    /**
     * 引用的 MCP Client 名字列表
     */
    private List<String> mcpClientNames;

    /**
     * 是否公开
     */
    @NotNull(message = "是否公开不能为空")
    private Boolean publicStatus;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空")
    @InEnum(CommonStatusEnum.class)
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}