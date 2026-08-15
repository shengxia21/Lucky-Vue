package com.lucky.ai.service;

import com.lucky.ai.domain.query.chat.ChatQuery;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

/**
 * AI 聊天Service接口
 *
 * @author lucky
 */
public interface IAiChatService {

    /**
     * 发送消息（流式）
     * <p>返回按 SSE 命名事件封装的流：思考内容 → thinking、正文 → text、错误 → error、流结束 → done</p>
     *
     * @param query 聊天参数
     * @return SSE 事件流式响应
     */
    Flux<ServerSentEvent<String>> chatStream(ChatQuery query);

}
