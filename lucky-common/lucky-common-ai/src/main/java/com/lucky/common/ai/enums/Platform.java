package com.lucky.common.ai.enums;

import com.lucky.common.core.enumeration.ArrayValuable;
import lombok.Getter;

import java.util.Arrays;

/**
 * AI 平台枚举
 *
 * @author lucky
 */
@Getter
public enum Platform implements ArrayValuable<String> {

    TONG_YI("TongYi", "通义千问"), // 阿里
    DEEP_SEEK("DeepSeek", "DeepSeek"), // DeepSeek
    ZHI_PU("ZhiPu", "智谱"), // 智谱 AI
    DOU_BAO("DouBao", "豆包"), // 字节
    HUN_YUAN("HunYuan", "混元"), // 腾讯
    SILICON_FLOW("SiliconFlow", "硅基流动"), // 硅基流动
    MOONSHOT("Moonshot", "月之暗灭"); // KIMI

    private final String code;
    private final String info;

    Platform(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static final String[] ARRAYS = Arrays.stream(values()).map(Platform::getCode).toArray(String[]::new);

    @Override
    public String[] array() {
        return ARRAYS;
    }

}
