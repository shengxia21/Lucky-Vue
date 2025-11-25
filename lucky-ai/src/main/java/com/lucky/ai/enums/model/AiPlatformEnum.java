package com.lucky.ai.enums.model;

import java.util.Arrays;

/**
 * AI 模型平台
 *
 * @author lucky
 */
public enum AiPlatformEnum {

    TONG_YI("TongYi", "通义千问"), // 阿里
    YI_YAN("YiYan", "文心一言"), // 百度
    DEEP_SEEK("DeepSeek", "DeepSeek"), // DeepSeek
    ZHI_PU("ZhiPu", "智谱"), // 智谱 AI
    XING_HUO("XingHuo", "星火"), // 讯飞
    DOU_BAO("DouBao", "豆包"), // 字节
    HUN_YUAN("HunYuan", "混元"), // 腾讯
    SILICON_FLOW("SiliconFlow", "硅基流动"), // 硅基流动
    MINI_MAX("MiniMax", "MiniMax"), // 稀宇科技
    MOONSHOT("Moonshot", "月之暗灭"), // KIMI
    BAI_CHUAN("BaiChuan", "百川智能"), // 百川智能

    OPENAI("OpenAI", "OpenAI");

    /**
     * 平台
     */
    private final String platform;
    /**
     * 平台名
     */
    private final String name;

    AiPlatformEnum(String platform, String name) {
        this.platform = platform;
        this.name = name;
    }

    public String getPlatform() {
        return platform;
    }

    public String getName() {
        return name;
    }

    public static final String[] ARRAYS = Arrays.stream(values()).map(AiPlatformEnum::getPlatform).toArray(String[]::new);

    public static AiPlatformEnum validatePlatform(String platform) {
        for (AiPlatformEnum platformEnum : AiPlatformEnum.values()) {
            if (platformEnum.getPlatform().equals(platform)) {
                return platformEnum;
            }
        }
        throw new IllegalArgumentException("非法平台： " + platform);
    }

    public String[] array() {
        return ARRAYS;
    }

}
