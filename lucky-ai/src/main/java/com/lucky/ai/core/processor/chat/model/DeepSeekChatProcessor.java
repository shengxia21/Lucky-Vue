package com.lucky.ai.core.processor.chat.model;

import com.lucky.ai.core.processor.chat.AbstractChatProcessor;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.enums.model.AiPlatformEnum;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.deepseek.DeepSeekAssistantMessage;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.deepseek.api.DeepSeekApi;
import org.springframework.stereotype.Component;

/**
 * DeepSeek聊天处理器
 *
 * @author lucky
 */
@Component
public class DeepSeekChatProcessor extends AbstractChatProcessor {

    @Override
    protected ChatModel buildChatModel(String apiKey) {
        DeepSeekApi deepSeekApi = DeepSeekApi.builder().apiKey(apiKey).build();
        return DeepSeekChatModel.builder()
                .deepSeekApi(deepSeekApi)
                .build();
    }

    @Override
    protected ChatOptions buildChatOptions(AiModel model, AiChatConversation conversation) {
        return DeepSeekChatOptions.builder()
                .model(model.getModel())
                .temperature(conversation.getTemperature())
                .maxTokens(conversation.getMaxTokens())
                .build();
    }

    @Override
    public String getProcessorName() {
        return AiPlatformEnum.DEEP_SEEK.getPlatform();
    }

    @Override
    protected String extractChatResponseReasoningContent(ChatResponse response) {
        return ((DeepSeekAssistantMessage) (response.getResult().getOutput())).getReasoningContent();
    }

}