package com.lucky.common.ai.enums;

import lombok.Getter;

/**
 * AI 图片生成状态枚举
 *
 * @author lucky
 */
@Getter
public enum ImageGenerateStatus {

    IN_PROGRESS(10, "进行中"),
    SUCCESS(20, "已完成"),
    FAIL(30, "已失败");

    private final Integer code;
    private final String info;

    ImageGenerateStatus(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

}
