package com.lucky.common.ai.chat.emitter;

import com.lucky.common.ai.domain.dto.ChatMessageUpdateDTO;
import com.lucky.common.ai.enums.MessageRole;
import com.lucky.common.ai.factory.SseEventFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * 聊天消息更新事件发射器（请求作用域）
 *
 * <p>底层为 unicast + 背压缓冲的单订阅者 sink：发射器尚未被订阅时（如流水线上游先落库）
 * 事件先缓冲，订阅后回放，不会丢失。</p>
 *
 * @author lucky
 */
@Slf4j
public class ChatMessageUpdateEmitter {

    private final Sinks.Many<ServerSentEvent<String>> sink = Sinks.many().unicast().onBackpressureBuffer();

    /**
     * 消息落库完成回调：经 {@code update} SSE 事件显式流出（以 role 区分用户/助手消息），
     * 前端即时获取真实编号与创建时间，流中途断连 / 超时 / 报错也不会丢失。
     *
     * @param role 消息角色（user / assistant）
     * @param meta 消息元数据（为 null 时跳过推送）
     */
    public void onMessageSaved(MessageRole role, ChatMessageUpdateDTO meta) {
        this.emit(role, SseEventFactory.updateEvent(role, meta));
    }

    /**
     * 更新事件流（应先于 LLM 流订阅，保证 t0 事件不漏）
     *
     * @return 更新 SSE 事件流
     */
    public Flux<ServerSentEvent<String>> asFlux() {
        return this.sink.asFlux();
    }

    /**
     * 终结更新事件流（出口流终结时调用，保证 merge 整体完成）
     */
    public void complete() {
        this.sink.tryEmitComplete();
    }

    /**
     * 推送消息更新事件
     *
     * @param role  消息角色（仅用于日志）
     * @param event 更新事件（元信息为 null 时工厂返回 null，直接跳过）
     */
    private void emit(MessageRole role, ServerSentEvent<String> event) {
        if (event == null) {
            return;
        }
        Sinks.EmitResult emitResult = this.sink.tryEmitNext(event);
        // 客户端已断开或流已终结时推送失败：仅记录日志，不影响消息落库结果
        if (emitResult.isFailure()) {
            log.warn("消息更新事件推送失败[{}]: {}", role, emitResult);
        }
    }

}
