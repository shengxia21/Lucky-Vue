package com.lucky.ai.service;

import com.lucky.ai.domain.query.chat.ChatQuery;
import com.lucky.common.ai.domain.vo.ChatResponseVO;
import reactor.core.publisher.Flux;

/**
 * AI 聊天Service接口
 *
 * @author lucky
 */
public interface IAiChatService {

    /**
     * 发送消息（流式）
     *
     * @param query    发送消息（流式）请求VO
     * @param userId   用户ID
     * @param userName 用户名
     * @return Flux流式响应
     */
    Flux<ChatResponseVO> chatStream(ChatQuery query, Long userId, String userName);

}
