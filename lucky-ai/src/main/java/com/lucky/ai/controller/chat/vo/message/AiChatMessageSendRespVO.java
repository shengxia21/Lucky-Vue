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
     * 发送消息
     */
    private Message send;

    /**
     * 接收消息
     */
    private Message receive;

    public Message getSend() {
        return send;
    }

    public AiChatMessageSendRespVO setSend(Message send) {
        this.send = send;
        return this;
    }

    public Message getReceive() {
        return receive;
    }

    public AiChatMessageSendRespVO setReceive(Message receive) {
        this.receive = receive;
        return this;
    }

    /**
     * 消息
     */
    public static class Message {

        /**
         * 编号
         */
        private Long id;

        /**
         * 消息类型
         */
        private String type; // 参见 MessageType 枚举类

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

        public Long getId() {
            return id;
        }

        public Message setId(Long id) {
            this.id = id;
            return this;
        }

        public String getType() {
            return type;
        }

        public Message setType(String type) {
            this.type = type;
            return this;
        }

        public String getContent() {
            return content;
        }

        public Message setContent(String content) {
            this.content = content;
            return this;
        }

        public String getReasoningContent() {
            return reasoningContent;
        }

        public Message setReasoningContent(String reasoningContent) {
            this.reasoningContent = reasoningContent;
            return this;
        }

        public List<Long> getSegmentIds() {
            return segmentIds;
        }

        public Message setSegmentIds(List<Long> segmentIds) {
            this.segmentIds = segmentIds;
            return this;
        }

        public List<AiChatMessageRespVO.KnowledgeSegment> getSegments() {
            return segments;
        }

        public Message setSegments(List<AiChatMessageRespVO.KnowledgeSegment> segments) {
            this.segments = segments;
            return this;
        }

        public List<AiWebSearchResponse.WebPage> getWebSearchPages() {
            return webSearchPages;
        }

        public Message setWebSearchPages(List<AiWebSearchResponse.WebPage> webSearchPages) {
            this.webSearchPages = webSearchPages;
            return this;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public Message setCreateTime(Date createTime) {
            this.createTime = createTime;
            return this;
        }

    }

}
