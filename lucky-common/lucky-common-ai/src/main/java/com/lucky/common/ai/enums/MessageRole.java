package com.lucky.common.ai.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 消息角色枚举
 *
 * @author lucky
 */
@Getter
@RequiredArgsConstructor
public enum MessageRole {

    /**
     * 用户消息
     */
    USER("user"),

    /**
     * 助手消息
     */
    ASSISTANT("assistant");

    /**
     * 角色值（SSE 负载 role 字段取值）
     */
    private final String value;

}
