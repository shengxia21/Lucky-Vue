package com.lucky.ai.chain.handler;

import com.lucky.ai.chain.context.ChatStreamContext;

/**
 * 流式聊天处理器接口（责任链）
 *
 * @author lucky
 */
public interface ChatStreamHandler {

    /**
     * 处理器名称（用于日志与错误定位）
     *
     * @return 处理器名称
     */
    String getName();

    /**
     * 执行处理器逻辑
     *
     * @param context 责任链上下文
     */
    void handle(ChatStreamContext context);

}
