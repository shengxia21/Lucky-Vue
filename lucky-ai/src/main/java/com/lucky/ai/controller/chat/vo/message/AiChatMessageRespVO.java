package com.lucky.ai.controller.chat.vo.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lucky.ai.core.websearch.AiWebSearchResponse;

import java.util.Date;
import java.util.List;

/**
 * AI 聊天消息响应VO
 *
 * @author lucky
 */
public class AiChatMessageRespVO {

    /**
     * 编号
     */
    private Long id;

    /**
     * 对话编号
     */
    private Long conversationId;

    /**
     * 回复消息编号
     */
    private Long replyId;

    /**
     * 消息类型
     */
    private String type; // 参见 MessageType 枚举类

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 角色编号
     */
    private Long roleId;

    /**
     * 模型标志
     */
    private String model;

    /**
     * 模型编号
     */
    private Long modelId;

    /**
     * 聊天内容
     */
    private String content;

    /**
     * 推理内容
     */
    private String reasoningContent;

    /**
     * 是否携带上下文
     */
    private Boolean useContext;

    /**
     * 知识库段落编号数组
     */
    private List<Long> segmentIds;

    /**
     * 知识库段落数组
     */
    private List<KnowledgeSegment> segments;

    /**
     * 联网搜索的网页内容数组
     */
    private List<AiWebSearchResponse.WebPage> webSearchPages;

    /**
     * 附件 URL 数组
     */
    private List<String> attachmentUrls;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    // ========== 仅在【对话管理】时加载 ==========

    /**
     * 角色名字
     */
    private String roleName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReasoningContent() {
        return reasoningContent;
    }

    public void setReasoningContent(String reasoningContent) {
        this.reasoningContent = reasoningContent;
    }

    public Boolean getUseContext() {
        return useContext;
    }

    public void setUseContext(Boolean useContext) {
        this.useContext = useContext;
    }

    public List<Long> getSegmentIds() {
        return segmentIds;
    }

    public void setSegmentIds(List<Long> segmentIds) {
        this.segmentIds = segmentIds;
    }

    public List<KnowledgeSegment> getSegments() {
        return segments;
    }

    public void setSegments(List<KnowledgeSegment> segments) {
        this.segments = segments;
    }

    public List<AiWebSearchResponse.WebPage> getWebSearchPages() {
        return webSearchPages;
    }

    public void setWebSearchPages(List<AiWebSearchResponse.WebPage> webSearchPages) {
        this.webSearchPages = webSearchPages;
    }

    public List<String> getAttachmentUrls() {
        return attachmentUrls;
    }

    public void setAttachmentUrls(List<String> attachmentUrls) {
        this.attachmentUrls = attachmentUrls;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * 知识库段落
     */
    public static class KnowledgeSegment {

        /**
         * 段落编号
         */
        private Long id;

        /**
         * 切片内容
         */
        private String content;

        /**
         * 文档编号
         */
        private Long documentId;

        /**
         * 文档名称
         */
        private String documentName;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Long getDocumentId() {
            return documentId;
        }

        public void setDocumentId(Long documentId) {
            this.documentId = documentId;
        }

        public String getDocumentName() {
            return documentName;
        }

        public void setDocumentName(String documentName) {
            this.documentName = documentName;
        }

    }

}
