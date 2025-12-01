package com.lucky.ai.controller.chat.vo.message;

import java.util.HashMap;
import java.util.Map;

/**
 * AI 聊天消息分页查询VO
 *
 * @author lucky
 */
public class AiChatMessagePageReqVO {

    /**
     * 对话编号
     */
    private Long conversationId;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 查询参数（开始时间、结束时间）
     */
    private Map<String, Object> params;

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

}
