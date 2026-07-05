package com.lucky.common.ai.service.impl;

import com.lucky.common.ai.domain.request.ChatRequest;
import com.lucky.common.ai.enums.AiPlatformEnum;
import com.lucky.common.ai.service.AbstractChatService;
import com.lucky.common.core.utils.StringUtils;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.deepseek.DeepSeekAssistantMessage;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.deepseek.api.DeepSeekApi;
import org.springframework.stereotype.Component;

/**
 * DeepSeek聊天策略
 *
 * @author lucky
 */
@Component
public class DeepSeekServiceImpl implements AbstractChatService {

    @Override
    public ChatModel buildChatModel(String baseUrl, String apiKey) {
        DeepSeekApi.Builder builder = DeepSeekApi.builder().apiKey(apiKey);
        if (StringUtils.isNotBlank(baseUrl)) {
            builder.baseUrl(baseUrl);
        }
        DeepSeekApi deepSeekApi = builder.build();
        return DeepSeekChatModel.builder()
                .deepSeekApi(deepSeekApi)
                .build();
    }

    @Override
    public ChatOptions buildChatOptions(ChatRequest chatRequest) {
        return DeepSeekChatOptions.builder()
                .model(chatRequest.getModel())
                .temperature(chatRequest.getTemperature())
                .maxTokens(chatRequest.getMaxTokens())
                .build();
    }

    @Override
    public String extractReasoningContent(AssistantMessage assistantMessage) {
        return ((DeepSeekAssistantMessage) assistantMessage).getReasoningContent();
    }

    @Override
    public String getProviderName() {
        return AiPlatformEnum.DEEP_SEEK.getPlatform();
    }

}
