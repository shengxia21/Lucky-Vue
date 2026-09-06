package com.lucky.common.ai.event;

/**
 * 内容增量事件负载（thinking / text 复用）
 *
 * @param delta 增量文本内容片段
 * @author lucky
 */
public record ContentEvent(String delta) {
}
