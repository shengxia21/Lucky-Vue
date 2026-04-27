package com.lucky.ai.core.strategy;

import com.lucky.ai.core.context.ChatContext;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;

/**
 * 聊天模型策略
 *
 * @author lucky
 */
public interface ChatModelStrategy {

    /**
     * 构建模型
     *
     * @param apiKey apiKey
     * @return 聊天模型
     */
    ChatModel buildChatModel(String apiKey);

    /**
     * 构建选项
     *
     * @param chatContext 聊天上下文
     * @return 聊天选项
     */
    ChatOptions buildChatOptions(ChatContext chatContext);

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
     * 获取策略名称
     *
     * @return 策略名称
     */
    String getStrategyName();

}
