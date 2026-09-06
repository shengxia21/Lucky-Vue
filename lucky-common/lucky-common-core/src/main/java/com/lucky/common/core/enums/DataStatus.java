package com.lucky.common.core.enums;

import com.lucky.common.core.enumeration.ArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 数据状态
 *
 * @author lucky
 */
@Getter
@RequiredArgsConstructor
public enum DataStatus implements ArrayValuable<String> {

    OK("0", "正常"), DISABLE("1", "停用");

    private final String code;
    private final String info;

    public static final String[] ARRAYS = Arrays.stream(values()).map(DataStatus::getCode).toArray(String[]::new);

    @Override
    public String[] array() {
        return ARRAYS;
    }

}
