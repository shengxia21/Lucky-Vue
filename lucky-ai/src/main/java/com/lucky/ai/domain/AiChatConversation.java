package com.lucky.ai.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lucky.common.annotation.Excel;
import com.lucky.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * AI 聊天对话对象 ai_chat_conversation
 * 
 * @author lucky
 */
public class AiChatConversation extends BaseEntity {

    public static final String TITLE_DEFAULT = "新对话";

    private static final long serialVersionUID = 1L;

    /** ID 编号，自增 */
    private Long id;

    /** 用户编号 */
    @Excel(name = "用户编号")
    private Long userId;

    /** 对话标题 */
    @Excel(name = "对话标题")
    private String title;

    /** 是否置顶（0否 1是） */
    @Excel(name = "是否置顶", readConverterExp = "0=否,1=是")
    private Boolean pinned;

    /** 置顶时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "置顶时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date pinnedTime;

    /** 角色编号 */
    @Excel(name = "角色编号")
    private Long roleId;

    /** 模型编号 */
    @Excel(name = "模型编号")
    private Long modelId;

    /** 模型标志 */
    @Excel(name = "模型标志")
    private String model;

    /** 角色设定 */
    @Excel(name = "角色设定")
    private String systemMessage;

    /** 温度参数 */
    @Excel(name = "温度参数")
    private Double temperature;

    /** 单条回复的最大 Token 数量 */
    @Excel(name = "单条回复的最大 Token 数量")
    private Integer maxTokens;

    /** 上下文的最大 Message 数量 */
    @Excel(name = "上下文的最大 Message 数量")
    private Integer maxContexts;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setPinned(Boolean pinned) {
        this.pinned = pinned;
    }

    public Boolean getPinned() {
        return pinned;
    }

    public void setPinnedTime(Date pinnedTime) {
        this.pinnedTime = pinnedTime;
    }

    public Date getPinnedTime() {
        return pinnedTime;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public void setSystemMessage(String systemMessage) {
        this.systemMessage = systemMessage;
    }

    public String getSystemMessage() {
        return systemMessage;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxContexts(Integer maxContexts) {
        this.maxContexts = maxContexts;
    }

    public Integer getMaxContexts() {
        return maxContexts;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("title", getTitle())
            .append("pinned", getPinned())
            .append("pinnedTime", getPinnedTime())
            .append("roleId", getRoleId())
            .append("modelId", getModelId())
            .append("model", getModel())
            .append("systemMessage", getSystemMessage())
            .append("temperature", getTemperature())
            .append("maxTokens", getMaxTokens())
            .append("maxContexts", getMaxContexts())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }

}
