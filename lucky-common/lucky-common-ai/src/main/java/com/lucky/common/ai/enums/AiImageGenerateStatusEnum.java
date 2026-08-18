package com.lucky.common.ai.enums;

import lombok.Getter;

/**
 * AI 图片生成状态枚举
 *
 * @author lucky
 */
@Getter
public enum AiImageGenerateStatusEnum {

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

    AiImageGenerateStatusEnum(Integer status, String name) {
        this.status = status;
        this.name = name;
    }

}
