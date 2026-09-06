package com.lucky.common.ai.event;

/**
 * 错误事件负载（event=error）
 *
 * @param message 错误消息（可直接作为前端提示）
 * @author lucky
 */
public record ErrorEvent(String message) {
}
