package com.lucky.ai.factory;

import cn.hutool.core.lang.Singleton;
import cn.hutool.core.lang.func.Func0;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.api.DashScopeImageApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import com.lucky.ai.enums.model.AiPlatformEnum;
import com.lucky.common.utils.spring.SpringUtils;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.deepseek.api.DeepSeekApi;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.ai.zhipuai.ZhiPuAiImageModel;
import org.springframework.ai.zhipuai.api.ZhiPuAiApi;
import org.springframework.ai.zhipuai.api.ZhiPuAiImageApi;

/**
 * AI Model 模型工厂的实现类
 *
 * @author lucky
 */
public class AiModelFactoryImpl implements AiModelFactory {

    @Override
    public ChatModel getOrCreateChatModel(AiPlatformEnum platform, String apiKey, String url) {
        String cacheKey = buildClientCacheKey(ChatModel.class, platform, apiKey, url);
        return Singleton.get(cacheKey, (Func0<ChatModel>) () -> switch (platform) {
            case TONG_YI -> buildTongYiChatModel(apiKey);
            case DEEP_SEEK -> buildDeepSeekChatModel(apiKey);
            case ZHI_PU -> buildZhiPuChatModel(apiKey);
            default -> throw new IllegalArgumentException(StrUtil.format("未知平台({})", platform));
        });
    }

    @Override
    public ChatModel getDefaultChatModel(AiPlatformEnum platform) {
        return switch (platform) {
            case TONG_YI -> SpringUtils.getBean(DashScopeChatModel.class);
            case DEEP_SEEK -> SpringUtils.getBean(DeepSeekChatModel.class);
            case ZHI_PU -> SpringUtils.getBean(ZhiPuAiChatModel.class);
            default -> throw new IllegalArgumentException(StrUtil.format("未知平台({})", platform));
        };
    }

    @Override
    public ImageModel getDefaultImageModel(AiPlatformEnum platform) {
        return switch (platform) {
            case TONG_YI -> SpringUtils.getBean(DashScopeImageModel.class);
            case ZHI_PU -> SpringUtils.getBean(ZhiPuAiImageModel.class);
            default -> throw new IllegalArgumentException(StrUtil.format("未知平台({})", platform));
        };
    }

    @Override
    public ImageModel getOrCreateImageModel(AiPlatformEnum platform, String apiKey, String url) {
        return switch (platform) {
            case TONG_YI -> buildTongYiImagesModel(apiKey);
            case ZHI_PU -> buildZhiPuAiImageModel(apiKey);
            default -> throw new IllegalArgumentException(StrUtil.format("未知平台({})", platform));
        };
    }

    /**
     * 构建客户端缓存 Key
     *
     * @param clazz  客户端类
     * @param params 客户端参数
     * @return 客户端缓存 Key
     */
    private static String buildClientCacheKey(Class<?> clazz, Object... params) {
        if (ArrayUtil.isEmpty(params)) {
            return clazz.getName();
        }
        return StrUtil.format("{}#{}", clazz.getName(), ArrayUtil.join(params, "_"));
    }

    /**
     * 构建通义千问模型
     *
     * @param key 通义千问 API Key
     * @return 通义千问模型
     */
    private static DashScopeChatModel buildTongYiChatModel(String key) {
        DashScopeApi dashScopeApi = DashScopeApi.builder().apiKey(key).build();
        DashScopeChatOptions options = DashScopeChatOptions.builder().withModel(DashScopeApi.DEFAULT_CHAT_MODEL)
                .withTemperature(0.7).build();
        return DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .defaultOptions(options)
                .build();
    }

    /**
     * 构建通义千问图片模型
     *
     * @param key 通义千问 API Key
     * @return 通义千问图片模型
     */
    private static DashScopeImageModel buildTongYiImagesModel(String key) {
        DashScopeImageApi dashScopeImageApi = DashScopeImageApi.builder().apiKey(key).build();
        return DashScopeImageModel.builder()
                .dashScopeApi(dashScopeImageApi)
                .build();
    }

    /**
     * 构建 DeepSeek 模型
     *
     * @param apiKey DeepSeek API Key
     * @return DeepSeek 模型
     */
    private static DeepSeekChatModel buildDeepSeekChatModel(String apiKey) {
        DeepSeekApi deepSeekApi = DeepSeekApi.builder().apiKey(apiKey).build();
        DeepSeekChatOptions options = DeepSeekChatOptions.builder().model(DeepSeekApi.DEFAULT_CHAT_MODEL)
                .temperature(0.7).build();
        return DeepSeekChatModel.builder()
                .deepSeekApi(deepSeekApi)
                .defaultOptions(options)
                .build();
    }

    /**
     * 构建智普ai模型
     *
     * @param apiKey 智普ai API Key
     * @return 智普ai模型
     */
    private ZhiPuAiChatModel buildZhiPuChatModel(String apiKey) {
        ZhiPuAiApi zhiPuApi = ZhiPuAiApi.builder().apiKey(apiKey).build();
        ZhiPuAiChatOptions options = ZhiPuAiChatOptions.builder().model(ZhiPuAiApi.DEFAULT_CHAT_MODEL)
                .temperature(0.7).build();
        return new ZhiPuAiChatModel(zhiPuApi, options);
    }

    /**
     * 构建智普ai图片模型
     *
     * @param apiKey 智普ai API Key
     * @return 智普ai图片模型
     */
    private ZhiPuAiImageModel buildZhiPuAiImageModel(String apiKey) {
        ZhiPuAiImageApi zhiPuAiApi = new ZhiPuAiImageApi(apiKey);
        return new ZhiPuAiImageModel(zhiPuAiApi);
    }

}
