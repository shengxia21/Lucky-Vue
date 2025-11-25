package com.lucky.ai.enums.model;

import java.util.Arrays;

/**
 * AI 模型类型的枚举
 *
 * @author lucky
 */
public enum AiModelTypeEnum {

    CHAT(1, "对话"),
    IMAGE(2, "图片"),
    VOICE(3, "语音"),
    VIDEO(4, "视频"),
    EMBEDDING(5, "向量"),
    RERANK(6, "重排序");

    /**
     * 类型
     */
    private final Integer type;
    /**
     * 类型名
     */
    private final String name;

    AiModelTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(AiModelTypeEnum::getType).toArray(Integer[]::new);

    public Integer[] array() {
        return ARRAYS;
    }

}
