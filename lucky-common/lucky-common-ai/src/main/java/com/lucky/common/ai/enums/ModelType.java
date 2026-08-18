package com.lucky.common.ai.enums;

import com.lucky.common.core.enumeration.ArrayValuable;
import lombok.Getter;

import java.util.Arrays;

/**
 * AI 模型类型枚举
 *
 * @author lucky
 */
@Getter
public enum ModelType implements ArrayValuable<Integer> {

    CHAT(1, "对话"),
    IMAGE(2, "图片"),
    VOICE(3, "语音"),
    VIDEO(4, "视频"),
    EMBEDDING(5, "向量"),
    RERANK(6, "重排序");

    private final Integer code;
    private final String info;

    ModelType(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(ModelType::getCode).toArray(Integer[]::new);

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
