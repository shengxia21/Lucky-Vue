package com.lucky.ai.core.processor.chat.model;

import com.lucky.ai.core.processor.chat.AbstractChatProcessor;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.enums.model.AiPlatformEnum;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.ai.zhipuai.api.ZhiPuAiApi;
import org.springframework.stereotype.Component;

/**
 * 智谱AI聊天处理器
 *
 * @author lucky
 */
@Component
public class ZhiPuChatProcessor extends AbstractChatProcessor {

    @Override
    protected ChatModel buildChatModel(String apiKey) {
        ZhiPuAiApi zhiPuApi = ZhiPuAiApi.builder().apiKey(apiKey).build();
        return new ZhiPuAiChatModel(zhiPuApi);
    }

    @Override
    protected ChatOptions buildChatOptions(AiModel model, AiChatConversation conversation) {
        return ZhiPuAiChatOptions.builder()
                .model(model.getModel())
                .temperature(conversation.getTemperature())
                .maxTokens(conversation.getMaxTokens())
                .build();
    }

    @Override
    public String getProcessorName() {
        return AiPlatformEnum.ZHI_PU.getPlatform();
    }

}