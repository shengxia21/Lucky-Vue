package com.lucky.common.ai.service.impl;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.api.DashScopeImageApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import com.lucky.common.ai.domain.request.ChatRequest;
import com.lucky.common.ai.domain.request.ImageRequest;
import com.lucky.common.ai.enums.AiPlatformEnum;
import com.lucky.common.ai.service.AbstractChatService;
import com.lucky.common.ai.service.AbstractImageService;
import com.lucky.common.core.utils.StringUtils;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptions;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 通义千问策略
 *
 * @author lucky
 */
@Component
public class TongYiServiceImpl implements AbstractChatService, AbstractImageService {

    @Override
    public ChatModel buildChatModel(String baseUrl, String apiKey) {
        DashScopeApi.Builder builder = DashScopeApi.builder().apiKey(apiKey);
        if (StringUtils.isNotBlank(baseUrl)) {
            builder.baseUrl(baseUrl);
        }
        DashScopeApi dashScopeApi = builder.build();
        return DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
    }

    @Override
    public ChatOptions buildChatOptions(ChatRequest chatRequest) {
        return DashScopeChatOptions.builder()
                .model(chatRequest.getModel())
                .enableThinking(chatRequest.getUseThinking())
                .enableSearch(chatRequest.getUseSearch())
                .temperature(chatRequest.getTemperature())
                .maxToken(chatRequest.getMaxTokens())
                .build();
    }

    @Override
    public String extractChatResponseReasoningContent(ChatResponse response) {
        return (String) response.getResult().getOutput().getMetadata().getOrDefault("reasoningContent", "");
    }

    @Override
    public ImageModel buildImageModel(String baseUrl, String apiKey) {
        DashScopeImageApi.Builder builder = DashScopeImageApi.builder().apiKey(apiKey);
        if (StringUtils.isNotBlank(baseUrl)) {
            builder.baseUrl(baseUrl);
        }
        DashScopeImageApi dashScopeApi = builder.build();
        return DashScopeImageModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
    }

    @Override
    public ImageOptions buildImageOptions(ImageRequest imageRequest) {
        Map<String, String> options = imageRequest.getOptions();
        return DashScopeImageOptions.builder()
                .model(imageRequest.getModel()).n(1)
                .height(imageRequest.getHeight()).width(imageRequest.getWidth())
                .promptExtend(Boolean.parseBoolean(options.getOrDefault("promptExtend", "false")))
                .negativePrompt(options.getOrDefault("negativePrompt", ""))
                .enableInterleave(true)
                .build();
    }

    @Override
    public String getProviderName() {
        return AiPlatformEnum.TONG_YI.getPlatform();
    }

}
