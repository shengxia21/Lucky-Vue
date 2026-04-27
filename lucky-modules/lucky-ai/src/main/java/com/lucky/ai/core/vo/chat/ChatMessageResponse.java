package com.lucky.ai.core.vo.chat;

import lombok.Data;

/**
 * 聊天消息响应
 *
 * @author lucky
 */
@Data
public class ChatMessageResponse {

    /**
     * 聊天内容
     */
    private String content;

    /**
     * 推理内容
     */
    private String reasoningContent;

}
