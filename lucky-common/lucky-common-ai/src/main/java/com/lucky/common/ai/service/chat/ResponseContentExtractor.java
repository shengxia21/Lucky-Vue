package com.lucky.common.ai.service.chat;

import org.springframework.ai.chat.messages.AssistantMessage;

/**
 * 响应内容提取器
 *
 * @author lucky
 */
public interface ResponseContentExtractor {

    /**
     * 提取文本内容
     *
     * @param assistantMessage 聊天响应
     * @return 响应文本内容
     */
    default String extractTextContent(AssistantMessage assistantMessage) {
        return assistantMessage.getText();
    }

    /**
     * 提取思考内容
     *
     * @param assistantMessage 聊天响应
     * @return 响应思考内容
     */
    String extractReasoningContent(AssistantMessage assistantMessage);

}
