package com.lucky.common.ai.service.chat.impl.provider;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.lucky.common.ai.domain.request.ChatRequest;
import com.lucky.common.ai.enums.AiPlatformEnum;
import com.lucky.common.ai.service.chat.AbstractChatService;
import com.lucky.common.core.utils.StringUtils;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Component;

/**
 * 通义千问策略
 *
 * @author lucky
 */
@Component
public class TongYiServiceImpl implements AbstractChatService {

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
    public String extractReasoningContent(AssistantMessage assistantMessage) {
        return (String) assistantMessage.getMetadata().getOrDefault("reasoningContent", "");
    }

    @Override
    public String getProviderName() {
        return AiPlatformEnum.TONG_YI.getPlatform();
    }

}
