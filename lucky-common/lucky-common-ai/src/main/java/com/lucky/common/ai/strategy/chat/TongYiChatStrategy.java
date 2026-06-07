package com.lucky.common.ai.strategy.chat;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.lucky.common.ai.domain.context.ChatContext;
import com.lucky.common.ai.enums.AiPlatformEnum;
import com.lucky.common.ai.strategy.ChatModelStrategy;
import com.lucky.common.core.utils.StringUtils;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Component;

/**
 * 通义千问策略
 *
 * @author lucky
 */
@Component
public class TongYiChatStrategy implements ChatModelStrategy {

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
    public ChatOptions buildChatOptions(ChatContext chatContext) {
        return DashScopeChatOptions.builder()
                .model(chatContext.getModel())
                .enableThinking(chatContext.getUseThinking())
                .enableSearch(chatContext.getUseSearch())
                .temperature(chatContext.getTemperature())
                .maxToken(chatContext.getMaxTokens())
                .build();
    }

    @Override
    public String extractChatResponseReasoningContent(ChatResponse response) {
        return (String) response.getResult().getOutput().getMetadata().getOrDefault("reasoningContent", "");
    }

    @Override
    public String getStrategyName() {
        return AiPlatformEnum.TONG_YI.getPlatform();
    }

}
