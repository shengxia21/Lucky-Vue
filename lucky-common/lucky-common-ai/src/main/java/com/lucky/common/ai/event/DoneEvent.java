package com.lucky.common.ai.event;

/**
 * 流结束事件负载（event=done）
 *
 * @param reason 结束原因（当前固定为 stop，预留扩展）
 * @author lucky
 */
public record DoneEvent(String reason) {
}
