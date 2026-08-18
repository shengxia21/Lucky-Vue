package com.lucky.common.core.enums;

import lombok.Getter;

/**
 * 删除标志
 *
 * @author lucky
 */
@Getter
public enum DeleteFlag {

    EXISTENCE("0", "存在"), DELETE("1", "删除");

    private final String code;
    private final String info;

    DeleteFlag(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
