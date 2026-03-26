package com.lucky.ai.core.strategy.chat;

import com.lucky.ai.core.context.ChatContext;
import com.lucky.ai.core.strategy.ChatModelStrategy;
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
 * DeepSeek聊天策略
 *
 * @author lucky
 */
@Component
public class DeepSeekChatStrategy implements ChatModelStrategy {

    @Override
    public ChatModel buildChatModel(String apiKey) {
        DeepSeekApi deepSeekApi = DeepSeekApi.builder().apiKey(apiKey).build();
        return DeepSeekChatModel.builder()
                .deepSeekApi(deepSeekApi)
                .build();
    }

    @Override
    public ChatOptions buildChatOptions(ChatContext chatContext) {
        AiChatConversation conversation = chatContext.getConversation();
        AiModel model = chatContext.getModel();
        return DeepSeekChatOptions.builder()
                .model(model.getModel())
                .temperature(conversation.getTemperature())
                .maxTokens(conversation.getMaxTokens())
                .build();
    }

    @Override
    public String extractChatResponseReasoningContent(ChatResponse response) {
        return ((DeepSeekAssistantMessage) (response.getResult().getOutput())).getReasoningContent();
    }

    @Override
    public String getStrategyName() {
        return AiPlatformEnum.DEEP_SEEK.getPlatform();
    }

}
