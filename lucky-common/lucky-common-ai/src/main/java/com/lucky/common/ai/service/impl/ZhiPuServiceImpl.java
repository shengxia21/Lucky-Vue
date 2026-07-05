package com.lucky.common.ai.service.impl;

import com.lucky.common.ai.domain.request.ChatRequest;
import com.lucky.common.ai.domain.request.ImageRequest;
import com.lucky.common.ai.enums.AiPlatformEnum;
import com.lucky.common.ai.service.AbstractChatService;
import com.lucky.common.ai.service.AbstractImageService;
import com.lucky.common.core.utils.StringUtils;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.zhipuai.*;
import org.springframework.ai.zhipuai.api.ZhiPuAiApi;
import org.springframework.ai.zhipuai.api.ZhiPuAiImageApi;
import org.springframework.stereotype.Component;

/**
 * 智普策略
 *
 * @author lucky
 */
@Component
public class ZhiPuServiceImpl implements AbstractChatService, AbstractImageService {

    @Override
    public ChatModel buildChatModel(String baseUrl, String apiKey) {
        ZhiPuAiApi.Builder builder = ZhiPuAiApi.builder().apiKey(apiKey);
        if (StringUtils.isNotBlank(baseUrl)) {
            builder.baseUrl(baseUrl);
        }
        ZhiPuAiApi zhiPuApi = builder.build();
        return new ZhiPuAiChatModel(zhiPuApi);
    }

    @Override
    public ChatOptions buildChatOptions(ChatRequest chatRequest) {
        return ZhiPuAiChatOptions.builder()
                .model(chatRequest.getModel())
                .thinking(chatRequest.getUseThinking() ? ZhiPuAiApi.ChatCompletionRequest.Thinking.enabled() : ZhiPuAiApi.ChatCompletionRequest.Thinking.disabled())
                .temperature(chatRequest.getTemperature())
                .maxTokens(chatRequest.getMaxTokens())
                .build();
    }

    @Override
    public String extractReasoningContent(AssistantMessage assistantMessage) {
        return ((ZhiPuAiAssistantMessage) assistantMessage).getReasoningContent();
    }

    @Override
    public ImageModel buildImageModel(String baseUrl, String apiKey) {
        ZhiPuAiImageApi zhiPuAiImageApi = new ZhiPuAiImageApi(apiKey);
        return new ZhiPuAiImageModel(zhiPuAiImageApi);
    }

    @Override
    public ImageOptions buildImageOptions(ImageRequest imageRequest) {
        return ZhiPuAiImageOptions.builder()
                .model(imageRequest.getModel())
                .build();
    }

    @Override
    public String getProviderName() {
        return AiPlatformEnum.ZHI_PU.getPlatform();
    }

}
