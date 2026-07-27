package com.lucky.common.ai.service.chat;

import com.lucky.common.ai.domain.request.ChatRequest;
import org.springframework.ai.chat.model.ChatResponse;
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
     * @param chatRequest 聊天请求
     * @return 流式响应
     */
    Flux<ChatResponse> chat(ChatRequest chatRequest);

}
