package com.lucky.common.core.enums;

import lombok.Getter;

/**
 * 系统状态
 *
 * @author lucky
 */
@Getter
public enum SystemStatus {

    OK("0", "正常"), DISABLE("1", "停用");

    private final String code;
    private final String info;

    SystemStatus(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
