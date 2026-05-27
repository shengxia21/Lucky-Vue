package com.lucky.ai.controller.chat;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.lucky.ai.core.vo.chat.ChatMessageRequest;
import com.lucky.ai.core.vo.chat.ChatMessageResponse;
import com.lucky.ai.service.AiChatService;
import com.lucky.common.mybatis.core.controller.BaseController;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
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
    private AiChatService chatService;

    /**
     * 发送消息（流式 SSE）
     */
    @SaIgnore
    @PostMapping(value = "/send-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatMessageResponse> sendChatStream(@RequestBody ChatMessageRequest query) {
        // 手动校验登录状态
        if (!StpUtil.isLogin()) {
            ChatMessageResponse errorResponse = new ChatMessageResponse();
            errorResponse.setContent("请登录后操作");
            return Flux.just(errorResponse);
        }
        return chatService.sendChatStream(query, getUserId(), getUserName());
    }

}
