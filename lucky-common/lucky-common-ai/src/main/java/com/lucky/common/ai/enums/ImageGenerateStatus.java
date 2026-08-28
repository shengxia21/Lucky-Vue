package com.lucky.common.ai.enums;

import lombok.Getter;

/**
 * AI 图片生成状态枚举
 *
 * @author lucky
 */
@Getter
public enum ImageGenerateStatus {

    IN_PROGRESS("1", "进行中"),
    SUCCESS("2", "已完成"),
    FAIL("3", "已失败");

    private final String code;
    private final String info;

    ImageGenerateStatus(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
