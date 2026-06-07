package com.lucky.common.ai.service;

import com.lucky.common.ai.domain.context.ChatContext;
import com.lucky.common.ai.domain.response.ChatMessageResponse;
import reactor.core.publisher.Flux;

/**
 * 聊天服务接口
 *
 * @author lucky
 */
public interface ChatService {

    /**
     * 处理流式聊天消息
     *
     * @param chatContext 聊天上下文
     * @return 流式响应
     */
    Flux<ChatMessageResponse> chat(ChatContext chatContext);

}
