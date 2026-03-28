package com.lucky.ai.core.context;

import com.lucky.ai.core.vo.chat.ChatMessageRequest;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.AiModel;
import lombok.Data;

/**
 * 聊天上下文
 * 用于存储聊天相关的上下文信息，例如请求参数、会话信息、模型信息等
 *
 * @author lucky
 */
@Data
public class ChatContext {

    /**
     * 聊天请求参数
     */
    private ChatMessageRequest request;

    /**
     * 聊天会话信息
     */
    private AiChatConversation conversation;

    /**
     * 聊天模型信息
     */
    private AiModel model;

    /**
     * 聊天模型 API Key
     */
    private AiApiKey apiKey;

    /**
     * 用户 ID
     */
    private Long userId;

}