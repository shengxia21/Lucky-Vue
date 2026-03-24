package com.lucky.ai.core.processor.chat.model;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.lucky.ai.core.processor.chat.AbstractChatProcessor;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.enums.model.AiPlatformEnum;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Component;

/**
 * 通义千问聊天处理器
 *
 * @author lucky
 */
@Component
public class TongYiChatProcessor extends AbstractChatProcessor {

    @Override
    protected ChatModel buildChatModel(String apiKey) {
        DashScopeApi dashScopeApi = DashScopeApi.builder().apiKey(apiKey).build();
        return DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
    }

    @Override
    protected ChatOptions buildChatOptions(AiModel model, AiChatConversation conversation) {
        return DashScopeChatOptions.builder()
                .model(model.getModel())
                .enableThinking(true)
                .temperature(conversation.getTemperature())
                .maxToken(conversation.getMaxTokens())
                .build();
    }

    @Override
    public String getProcessorName() {
        return AiPlatformEnum.TONG_YI.getPlatform();
    }

    @Override
    protected String extractChatResponseReasoningContent(ChatResponse response) {
        return (String) response.getResult().getOutput().getMetadata().getOrDefault("reasoningContent", "");
    }

}