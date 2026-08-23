package com.lucky.ai.controller.chat;

import com.lucky.ai.chain.ChatStreamChain;
import com.lucky.ai.domain.query.chat.ChatQuery;
import com.lucky.common.mybatis.core.controller.BaseController;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * AI 聊天控制器
 *
 * @author lucky
 */
@RestController
@RequestMapping("/ai/chat")
public class AiChatController extends BaseController {

    @Resource
    private ChatStreamChain chatStreamChain;

    /**
     * 发送消息（流式 SSE）
     */
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> chatStream(@RequestBody ChatQuery query) {
        return chatStreamChain.execute(query);
    }

}
