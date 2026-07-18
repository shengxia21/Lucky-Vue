package com.lucky.common.ai.enums;

import lombok.Getter;

/**
 * AI 绘画状态枚举
 *
 * @author lucky
 */
@Getter
public enum AiImageStatusEnum {

    IN_PROGRESS(10, "进行中"),
    SUCCESS(20, "已完成"),
    FAIL(30, "已失败");

    /**
     * 状态
     */
    private final Integer status;

    /**
     * 状态名
     */
    private final String name;

    AiImageStatusEnum(Integer status, String name) {
        this.status = status;
        this.name = name;
    }

}
