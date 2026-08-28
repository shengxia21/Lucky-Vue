package com.lucky.common.core.enums;

import com.lucky.common.core.enumeration.ArrayValuable;
import lombok.Getter;

import java.util.Arrays;

/**
 * 系统是否枚举
 *
 * @author lucky
 */
@Getter
public enum YesNo implements ArrayValuable<String> {

    YES("Y", "是"), NO("N", "否");

    private final String code;
    private final String info;

    YesNo(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static final String[] ARRAYS = Arrays.stream(values()).map(YesNo::getCode).toArray(String[]::new);

    @Override
    public String[] array() {
        return ARRAYS;
    }

}
