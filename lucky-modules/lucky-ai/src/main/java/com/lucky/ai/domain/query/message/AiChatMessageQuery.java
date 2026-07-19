package com.lucky.ai.domain.query.message;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * AI 聊天消息分页查询对象
 *
 * @author lucky
 */
@Data
public class AiChatMessageQuery {

    /**
     * 对话编号
     */
    @NotNull(message = "对话编号不能为空")
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

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

}