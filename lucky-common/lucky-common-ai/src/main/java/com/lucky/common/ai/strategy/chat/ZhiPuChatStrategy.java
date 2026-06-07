package com.lucky.common.ai.strategy.chat;

import com.lucky.common.ai.domain.context.ChatContext;
import com.lucky.common.ai.enums.AiPlatformEnum;
import com.lucky.common.ai.strategy.ChatModelStrategy;
import com.lucky.common.core.utils.StringUtils;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.zhipuai.ZhiPuAiAssistantMessage;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.ai.zhipuai.api.ZhiPuAiApi;
import org.springframework.stereotype.Component;

/**
 * 智普策略
 *
 * @author lucky
 */
@Component
public class ZhiPuChatStrategy implements ChatModelStrategy {

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
    public ChatOptions buildChatOptions(ChatContext chatContext) {
        return ZhiPuAiChatOptions.builder()
                .model(chatContext.getModel())
                .thinking(chatContext.getUseThinking() ? ZhiPuAiApi.ChatCompletionRequest.Thinking.enabled() : ZhiPuAiApi.ChatCompletionRequest.Thinking.disabled())
                .temperature(chatContext.getTemperature())
                .maxTokens(chatContext.getMaxTokens())
                .build();
    }

    @Override
    public String extractChatResponseReasoningContent(ChatResponse response) {
        return ((ZhiPuAiAssistantMessage) (response.getResult().getOutput())).getReasoningContent();
    }

    @Override
    public String getStrategyName() {
        return AiPlatformEnum.ZHI_PU.getPlatform();
    }

}
