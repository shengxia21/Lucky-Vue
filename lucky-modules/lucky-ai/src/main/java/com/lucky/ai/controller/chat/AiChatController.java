package com.lucky.ai.controller.chat;

import com.lucky.ai.domain.query.chat.ChatQuery;
import com.lucky.ai.service.IAiChatService;
import com.lucky.common.ai.domain.vo.ChatResponseVO;
import com.lucky.common.mybatis.core.controller.BaseController;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
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
    private IAiChatService chatService;

    /**
     * 发送消息（流式 SSE）
     */
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponseVO> chatStream(@Validated @RequestBody ChatQuery query) {
        return chatService.chatStream(query);
    }

}
