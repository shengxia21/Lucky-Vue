package com.lucky.ai.controller.model.vo.chatRole;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * AI 聊天角色新增/修改【我的】请求VO
 *
 * @author lucky
 */
public class AiChatRoleSaveMyReqVO {

    /**
     * 角色编号
     */
    private Long id;

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

}