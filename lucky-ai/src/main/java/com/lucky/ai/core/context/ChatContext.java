package com.lucky.ai.core.context;

import com.lucky.ai.controller.chat.vo.message.AiChatMessageSendReqVO;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.AiModel;

/**
 * 聊天上下文
 * 用于存储聊天相关的上下文信息，例如请求参数、会话信息、模型信息等
 *
 * @author lucky
 */
public class ChatContext {

    /**
     * 聊天请求参数
     */
    private AiChatMessageSendReqVO sendReqVO;

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

    public AiChatMessageSendReqVO getSendReqVO() {
        return sendReqVO;
    }

    public void setSendReqVO(AiChatMessageSendReqVO sendReqVO) {
        this.sendReqVO = sendReqVO;
    }

    public AiChatConversation getConversation() {
        return conversation;
    }

    public void setConversation(AiChatConversation conversation) {
        this.conversation = conversation;
    }

    public AiModel getModel() {
        return model;
    }

    public void setModel(AiModel model) {
        this.model = model;
    }

    public AiApiKey getApiKey() {
        return apiKey;
    }

    public void setApiKey(AiApiKey apiKey) {
        this.apiKey = apiKey;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
