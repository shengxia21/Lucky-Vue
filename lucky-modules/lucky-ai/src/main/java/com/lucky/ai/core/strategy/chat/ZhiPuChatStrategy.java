package com.lucky.ai.core.strategy.chat;

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
        AiChatConversation conversation = chatContext.getConversation();
        AiModel model = chatContext.getModel();
        ChatMessageRequest request = chatContext.getRequest();
        return ZhiPuAiChatOptions.builder()
                .model(model.getModel())
                .thinking(request.getUseThinking() ? ZhiPuAiApi.ChatCompletionRequest.Thinking.enabled() : ZhiPuAiApi.ChatCompletionRequest.Thinking.disabled())
                .temperature(conversation.getTemperature())
                .maxTokens(conversation.getMaxTokens())
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
