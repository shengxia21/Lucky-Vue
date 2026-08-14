package com.lucky.common.ai.service.chat.impl.provider;

import com.lucky.common.ai.domain.request.ChatRequest;
import com.lucky.common.ai.enums.AiPlatformEnum;
import com.lucky.common.ai.service.chat.AbstractChatService;
import com.lucky.common.core.utils.StringUtils;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
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
public class ZhiPuServiceImpl implements AbstractChatService {

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
                .build();
    }

    @Override
    public String extractReasoningContent(AssistantMessage assistantMessage) {
        return ((ZhiPuAiAssistantMessage) assistantMessage).getReasoningContent();
    }

    @Override
    public String getProviderName() {
        return AiPlatformEnum.ZHI_PU.getPlatform();
    }

}
