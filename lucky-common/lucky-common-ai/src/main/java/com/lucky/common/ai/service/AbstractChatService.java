package com.lucky.common.ai.service;

import com.lucky.common.ai.domain.request.ChatRequest;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;

/**
 * 抽象聊天服务接口（策略）
 *
 * @author lucky
 */
public interface AbstractChatService {

    /**
     * 构建模型
     *
     * @param baseUrl 基础Url
     * @param apiKey  apiKey
     * @return 聊天模型
     */
    ChatModel buildChatModel(String baseUrl, String apiKey);

    /**
     * 构建选项
     *
     * @param chatRequest 聊天请求
     * @return 聊天选项
     */
    ChatOptions buildChatOptions(ChatRequest chatRequest);

    /**
     * 提取聊天响应内容
     *
     * @param response 聊天响应结果
     * @return 聊天响应内容
     */
    default String extractChatResponseContent(ChatResponse response) {
        return response.getResult().getOutput().getText();
    }

    /**
     * 提取聊天响应推理内容
     *
     * @param response 聊天响应结果
     * @return 聊天响应推理内容
     */
    String extractChatResponseReasoningContent(ChatResponse response);

    /**
     * 获取服务提供商名称
     *
     * @return 提供商名称
     */
    String getProviderName();

}
