package com.lucky.common.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 删除标志
 *
 * @author lucky
 */
@Getter
@RequiredArgsConstructor
public enum DeleteFlag {

    EXISTENCE("0", "存在"), DELETE("1", "删除");

    private final String code;
    private final String info;

}
