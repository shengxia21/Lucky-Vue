package com.lucky.ai.core.strategy.chat;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.lucky.ai.core.context.ChatContext;
import com.lucky.ai.core.strategy.ChatModelStrategy;
import com.lucky.ai.core.vo.chat.ChatMessageRequest;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.enums.model.AiPlatformEnum;
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
        ChatMessageRequest request = chatContext.getRequest();
        AiChatConversation conversation = chatContext.getConversation();
        AiModel model = chatContext.getModel();
        return DashScopeChatOptions.builder()
                .model(model.getModel())
                .enableThinking(request.getUseThinking())
                .enableSearch(request.getUseSearch())
                .temperature(conversation.getTemperature())
                .maxToken(conversation.getMaxTokens())
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
