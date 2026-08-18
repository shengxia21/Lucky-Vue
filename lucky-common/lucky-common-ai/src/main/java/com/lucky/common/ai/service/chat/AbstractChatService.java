package com.lucky.common.ai.service.chat;

import com.lucky.common.ai.domain.request.ChatRequest;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;

/**
 * 抽象聊天服务接口（策略）
 *
 * @author lucky
 */
public interface AbstractChatService extends ResponseContentExtractor {

    /**
     * 构建聊天模型
     *
     * @param baseUrl 基础Url
     * @param apiKey  apiKey
     * @return 聊天模型
     */
    ChatModel buildChatModel(String baseUrl, String apiKey);

    /**
     * 构建聊天选项
     *
     * @param chatRequest 聊天请求
     * @return 聊天选项
     */
    ChatOptions buildChatOptions(ChatRequest chatRequest);

    /**
     * 获取服务提供商名称
     *
     * @return 提供商名称
     */
    String getProviderName();

}
