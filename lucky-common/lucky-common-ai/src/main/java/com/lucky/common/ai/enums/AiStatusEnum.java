package com.lucky.common.ai.enums;

import cn.hutool.core.util.ObjUtil;
import com.lucky.common.core.enumeration.ArrayValuable;
import lombok.Getter;

import java.util.Arrays;

/**
 * AI 状态枚举
 *
 * @author lucky
 */
@Getter
public enum AiStatusEnum implements ArrayValuable<Integer> {

    ENABLE(0, "开启"),
    DISABLE(1, "关闭");

    /**
     * 状态值
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String name;

    AiStatusEnum(Integer status, String name) {
        this.status = status;
        this.name = name;
    }

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(AiStatusEnum::getStatus).toArray(Integer[]::new);

    public static boolean isEnable(Integer status) {
        return ObjUtil.equal(ENABLE.status, status);
    }

    public static boolean isDisable(Integer status) {
        return ObjUtil.equal(DISABLE.status, status);
    }

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
