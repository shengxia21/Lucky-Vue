package com.lucky.common.ai.enums;

import com.lucky.common.core.enumeration.ArrayValuable;
import lombok.Getter;

import java.util.Arrays;

/**
 * AI 状态枚举
 *
 * @author lucky
 */
@Getter
public enum AiStatus implements ArrayValuable<Integer> {

    ENABLE(0, "开启"),
    DISABLE(1, "关闭");

    private final Integer code;
    private final String info;

    AiStatus(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(AiStatus::getCode).toArray(Integer[]::new);

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
