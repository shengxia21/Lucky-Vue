package com.lucky.ai.domain;

import com.lucky.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * AI 聊天消息对象 ai_chat_message
 * 
 * @author lucky
 */
public class AiChatMessage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 对话编号 */
    private Long conversationId;

    /** 回复消息编号 */
    private Long replyId;

    /** 消息类型 */
    private String type;

    /** 用户编号 */
    private Long userId;

    /** 角色编号 */
    private Long roleId;

    /** 模型标志 */
    private String model;

    /** 模型编号 */
    private Long modelId;

    /** 聊天内容 */
    private String content;

    /** 推理内容 */
    private String reasoningContent;

    /** 是否携带上下文（0否 1是） */
    private Integer useContext;

    /** 知识库段落编号数组 */
    private String segmentIds;

    /** 联网搜索的网页内容数组 */
    private String webSearchPages;

    /** 附件 URL 数组 */
    private String attachmentUrls;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public Long getReplyId() {
        return replyId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setReasoningContent(String reasoningContent) {
        this.reasoningContent = reasoningContent;
    }

    public String getReasoningContent() {
        return reasoningContent;
    }

    public void setUseContext(Integer useContext) {
        this.useContext = useContext;
    }

    public Integer getUseContext() {
        return useContext;
    }

    public void setSegmentIds(String segmentIds) {
        this.segmentIds = segmentIds;
    }

    public String getSegmentIds() {
        return segmentIds;
    }

    public void setWebSearchPages(String webSearchPages) {
        this.webSearchPages = webSearchPages;
    }

    public String getWebSearchPages() {
        return webSearchPages;
    }

    public void setAttachmentUrls(String attachmentUrls) {
        this.attachmentUrls = attachmentUrls;
    }

    public String getAttachmentUrls() {
        return attachmentUrls;
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
            .append("conversationId", getConversationId())
            .append("replyId", getReplyId())
            .append("type", getType())
            .append("userId", getUserId())
            .append("roleId", getRoleId())
            .append("model", getModel())
            .append("modelId", getModelId())
            .append("content", getContent())
            .append("reasoningContent", getReasoningContent())
            .append("useContext", getUseContext())
            .append("segmentIds", getSegmentIds())
            .append("webSearchPages", getWebSearchPages())
            .append("attachmentUrls", getAttachmentUrls())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }

}
