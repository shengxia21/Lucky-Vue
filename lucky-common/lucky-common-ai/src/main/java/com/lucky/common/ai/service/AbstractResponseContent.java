package com.lucky.common.ai.service;

import org.springframework.ai.chat.model.ChatResponse;

/**
 * 抽象响应内容接口类
 *
 * @author lucky
 */
public interface AbstractResponseContent {

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

}
