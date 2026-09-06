package com.lucky.common.ai.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * AI 流式响应 SSE 事件类型枚举
 *
 * @author lucky
 */
@Getter
@RequiredArgsConstructor
public enum SseEventType {

    /**
     * 思考内容（深度思考模型的中间推理过程）
     */
    THINKING("thinking"),

    /**
     * 正文内容
     */
    TEXT("text"),

    /**
     * 消息更新（携带真实编号、创建时间等元数据，用于就地补齐前端占位消息；以 role 区分用户/助手消息）
     */
    UPDATE("update"),

    /**
     * 错误
     */
    ERROR("error"),

    /**
     * 流结束
     */
    DONE("done");

    /**
     * 事件名（SSE event 字段取值）
     */
    private final String value;

}
