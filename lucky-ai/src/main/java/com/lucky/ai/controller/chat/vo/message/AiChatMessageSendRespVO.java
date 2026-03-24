package com.lucky.ai.controller.chat.vo.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lucky.ai.core.websearch.AiWebSearchResponse;

import java.util.Date;
import java.util.List;

/**
 * AI 聊天消息发送响应VO
 *
 * @author lucky
 */
public class AiChatMessageSendRespVO {

    /**
     * 消息id
     */
    private Long id;

    /**
     * 聊天内容
     */
    private String content;

    /**
     * 推理内容
     */
    private String reasoningContent;

    /**
     * 知识库段落编号数组
     */
    private List<Long> segmentIds;

    /**
     * 知识库段落数组
     */
    private List<AiChatMessageRespVO.KnowledgeSegment> segments;

    /**
     * 联网搜索的网页内容数组
     */
    private List<AiWebSearchResponse.WebPage> webSearchPages;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 发送消息id
     */
    private Long sendId;

    /**
     * 发送内容
     */
    private String sendContent;

    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

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

    public String getReasoningContent() {
        return reasoningContent;
    }

    public void setReasoningContent(String reasoningContent) {
        this.reasoningContent = reasoningContent;
    }

    public List<Long> getSegmentIds() {
        return segmentIds;
    }

    public void setSegmentIds(List<Long> segmentIds) {
        this.segmentIds = segmentIds;
    }

    public List<AiChatMessageRespVO.KnowledgeSegment> getSegments() {
        return segments;
    }

    public void setSegments(List<AiChatMessageRespVO.KnowledgeSegment> segments) {
        this.segments = segments;
    }

    public List<AiWebSearchResponse.WebPage> getWebSearchPages() {
        return webSearchPages;
    }

    public void setWebSearchPages(List<AiWebSearchResponse.WebPage> webSearchPages) {
        this.webSearchPages = webSearchPages;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getSendId() {
        return sendId;
    }

    public void setSendId(Long sendId) {
        this.sendId = sendId;
    }

    public String getSendContent() {
        return sendContent;
    }

    public void setSendContent(String sendContent) {
        this.sendContent = sendContent;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

}
