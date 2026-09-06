package com.lucky.common.ai.factory;

import cn.hutool.core.util.StrUtil;
import com.lucky.common.ai.domain.dto.ChatMessageUpdateDTO;
import com.lucky.common.ai.enums.MessageRole;
import com.lucky.common.ai.enums.SseEventType;
import com.lucky.common.ai.event.ContentEvent;
import com.lucky.common.ai.event.DoneEvent;
import com.lucky.common.ai.event.ErrorEvent;
import com.lucky.common.ai.event.UpdateEvent;
import com.lucky.common.ai.service.chat.AbstractChatService;
import com.lucky.common.core.utils.JsonUtils;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.codec.ServerSentEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * AI 流式响应 SSE 事件工厂
 *
 * <ul>
 *   <li>thinking：{@code { "delta": "..." }} —— 思考内容增量</li>
 *   <li>text：{@code { "delta": "..." }} —— 正文内容增量</li>
 *   <li>update：{@code { "role": "user"|"assistant", "id": "...", "createTime": "...", ... }}
 *   <li>error：{@code { "message": "..." }} —— 错误消息</li>
 *   <li>done：{@code { "reason": "stop" }} —— 流结束</li>
 * </ul>
 *
 * @author lucky
 */
public final class SseEventFactory {

    private SseEventFactory() {
    }

    /**
     * 将单个 ChatResponse chunk 转为 SSE 命名事件列表
     *
     * @param response ChatResponse 流式响应
     * @param service  当前提供商的聊天服务（用于提取正文、思考内容）
     * @return SSE 事件列表（可能为空）
     */
    public static List<ServerSentEvent<String>> toEvents(ChatResponse response, AbstractChatService service) {
        List<ServerSentEvent<String>> events = new ArrayList<>();
        AssistantMessage output = response.getResult().getOutput();
        // 思考内容 → thinking 事件
        String reasoning = service.extractReasoningContent(output);
        if (StrUtil.isNotEmpty(reasoning)) {
            events.add(thinkingEvent(reasoning));
        }
        // 正文 → text 事件
        String text = service.extractTextContent(output);
        if (StrUtil.isNotEmpty(text)) {
            events.add(textEvent(text));
        }
        return events;
    }

    /**
     * 构造思考内容 SSE 事件（event=thinking, data={ "delta": "..." }）
     *
     * @param delta 思考内容增量
     * @return thinking SSE 事件
     */
    public static ServerSentEvent<String> thinkingEvent(String delta) {
        return dataEvent(SseEventType.THINKING, JsonUtils.toJSONString(new ContentEvent(delta)));
    }

    /**
     * 构造正文内容 SSE 事件（event=text, data={ "delta": "..." }）
     *
     * @param delta 正文内容增量
     * @return text SSE 事件
     */
    public static ServerSentEvent<String> textEvent(String delta) {
        return dataEvent(SseEventType.TEXT, JsonUtils.toJSONString(new ContentEvent(delta)));
    }

    /**
     * 构造消息更新 SSE 事件（event=update）
     *
     * @param role 消息角色（user / assistant）
     * @param meta 消息元数据（为 null 时返回 null，调用方跳过推送）
     * @return update SSE 事件
     */
    public static ServerSentEvent<String> updateEvent(MessageRole role, ChatMessageUpdateDTO meta) {
        if (meta == null) {
            return null;
        }
        return dataEvent(SseEventType.UPDATE, JsonUtils.toJSONString(UpdateEvent.from(role, meta)));
    }

    /**
     * 构造错误 SSE 事件（event=error, data={ "message": "..." }）
     *
     * @param errorMessage 异常信息
     * @return 错误 SSE 事件
     */
    public static ServerSentEvent<String> errorEvent(String errorMessage) {
        return dataEvent(SseEventType.ERROR, JsonUtils.toJSONString(new ErrorEvent(errorMessage)));
    }

    /**
     * 构造流结束 SSE 事件（event=done, data={ "reason": "stop" }）
     *
     * @return done SSE 事件
     */
    public static ServerSentEvent<String> doneEvent() {
        return dataEvent(SseEventType.DONE, JsonUtils.toJSONString(new DoneEvent("stop")));
    }

    /**
     * 构造数据 SSE 事件
     *
     * @param type 事件类型
     * @param data 事件负载（JSON 字符串）
     * @return SSE 事件
     */
    private static ServerSentEvent<String> dataEvent(SseEventType type, String data) {
        return ServerSentEvent.<String>builder()
                .event(type.getValue())
                .data(data)
                .build();
    }

}
