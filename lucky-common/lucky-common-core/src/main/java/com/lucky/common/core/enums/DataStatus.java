package com.lucky.common.core.enums;

import com.lucky.common.core.enumeration.ArrayValuable;
import lombok.Getter;

import java.util.Arrays;

/**
 * 数据状态
 *
 * @author lucky
 */
@Getter
public enum DataStatus implements ArrayValuable<String> {

    OK("0", "正常"), DISABLE("1", "停用");

    private final String code;
    private final String info;

    DataStatus(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static final String[] ARRAYS = Arrays.stream(values()).map(DataStatus::getCode).toArray(String[]::new);

    @Override
    public String[] array() {
        return ARRAYS;
    }

}
