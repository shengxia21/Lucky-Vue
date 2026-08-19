package com.lucky.common.ai.factory;

import cn.hutool.core.util.StrUtil;
import com.lucky.common.ai.service.chat.AbstractChatService;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.codec.ServerSentEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * AI 流式响应 SSE 事件工厂
 *
 * <p>负责将 Spring AI 的 ChatResponse 流转换为前端约定的 SSE 命名事件，统一收口事件类型与事件构造。</p>
 *
 * <p>事件协议（与前端约定）：</p>
 * <ul>
 *   <li>thinking：思考内容</li>
 *   <li>text：正文内容</li>
 *   <li>error：错误消息</li>
 *   <li>done：流结束（data=[DONE]）</li>
 * </ul>
 *
 * @author lucky
 */
public final class SseEventFactory {

    /**
     * 事件类型：思考内容
     */
    public static final String EVENT_THINKING = "thinking";
    /**
     * 事件类型：正文内容
     */
    public static final String EVENT_TEXT = "text";
    /**
     * 事件类型：错误
     */
    public static final String EVENT_ERROR = "error";
    /**
     * 事件类型：流结束
     */
    public static final String EVENT_DONE = "done";

    /**
     * 流结束事件数据
     */
    private static final String DONE_DATA = "[DONE]";

    private SseEventFactory() {
        // 工具类禁止实例化
    }

    /**
     * 将单个 ChatResponse chunk 转为 SSE 命名事件列表
     * <p>思考内容 → thinking 事件；正文 → text 事件；同一 chunk 可能同时包含两者，按思考优先顺序推送</p>
     *
     * @param response ChatResponse 流式响应
     * @param service  当前提供商的聊天服务（用于提取思考内容）
     * @return SSE 事件列表（可能为空）
     */
    public static List<ServerSentEvent<String>> toEvents(ChatResponse response, AbstractChatService service) {
        List<ServerSentEvent<String>> events = new ArrayList<>();
        AssistantMessage output = response.getResult().getOutput();
        // 思考内容 → thinking 事件
        String reasoning = service.extractReasoningContent(output);
        if (StrUtil.isNotBlank(reasoning)) {
            events.add(thinkingEvent(reasoning));
        }
        // 正文 → text 事件
        String text = service.extractTextContent(output);
        if (StrUtil.isNotBlank(text)) {
            events.add(textEvent(text));
        }
        return events;
    }

    /**
     * 构造思考内容 SSE 事件（event=thinking）
     *
     * @param reasoning 思考内容
     * @return thinking SSE 事件
     */
    public static ServerSentEvent<String> thinkingEvent(String reasoning) {
        return dataEvent(EVENT_THINKING, reasoning);
    }

    /**
     * 构造正文内容 SSE 事件（event=text）
     *
     * @param text 正文内容
     * @return text SSE 事件
     */
    public static ServerSentEvent<String> textEvent(String text) {
        return dataEvent(EVENT_TEXT, text);
    }

    /**
     * 构造错误 SSE 事件（event=error）
     * <p>错误消息通过 data 传递，前端收到 error 事件后提示用户并结束本次流</p>
     *
     * @param errorMessage 异常信息
     * @return 错误 SSE 事件
     */
    public static ServerSentEvent<String> errorEvent(String errorMessage) {
        return dataEvent(EVENT_ERROR, errorMessage);
    }

    /**
     * 构造流结束 SSE 事件（event=done, data=[DONE]）
     *
     * @return done SSE 事件
     */
    public static ServerSentEvent<String> doneEvent() {
        return dataEvent(EVENT_DONE, DONE_DATA);
    }

    /**
     * 构造数据 SSE 事件（thinking/text/error/done）
     *
     * @param event 事件类型
     * @param data  事件数据（文本内容）
     * @return SSE 事件
     */
    private static ServerSentEvent<String> dataEvent(String event, String data) {
        return ServerSentEvent.<String>builder()
                .event(event)
                .data(data)
                .build();
    }

}
