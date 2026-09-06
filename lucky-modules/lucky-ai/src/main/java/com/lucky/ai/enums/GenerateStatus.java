package com.lucky.ai.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 生成状态枚举
 *
 * @author lucky
 */
@Getter
@RequiredArgsConstructor
public enum GenerateStatus {

    IN_PROGRESS("1", "进行中"),
    SUCCESS("2", "已完成"),
    FAIL("3", "已失败");

    private final String code;
    private final String info;

}
