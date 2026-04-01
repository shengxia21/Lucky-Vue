package com.lucky.ai.domain.query.conversation;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 聊天对话分页查询请求VO
 *
 * @author lucky
 */
@Data
public class ChatConversationPageQuery {

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

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

}