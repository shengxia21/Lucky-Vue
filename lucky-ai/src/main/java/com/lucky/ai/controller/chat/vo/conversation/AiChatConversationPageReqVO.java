package com.lucky.ai.controller.chat.vo.conversation;

import java.util.HashMap;
import java.util.Map;

/**
 * 聊天对话分页查询请求VO
 *
 * @author lucky
 */
public class AiChatConversationPageReqVO {

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 对话标题
     */
    private String title;

    /**
     * 查询参数（开始时间、结束时间）
     */
    private Map<String, Object> params;

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
