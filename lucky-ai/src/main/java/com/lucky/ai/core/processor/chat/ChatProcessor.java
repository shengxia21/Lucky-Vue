package com.lucky.ai.core.processor.chat;

import com.lucky.ai.core.context.ChatContext;
import com.lucky.common.core.domain.AjaxResult;
import reactor.core.publisher.Flux;

/**
 * 聊天处理器接口
 * 负责处理不同AI平台的聊天逻辑
 * [策略模式核心类]
 *
 * @author lucky
 */
public interface ChatProcessor {

    /**
     * 处理流式聊天消息
     *
     * @param chatContext 聊天上下文
     * @return 流式响应
     */
    Flux<AjaxResult> processStream(ChatContext chatContext);

    /**
     * 获取处理器名称
     *
     * @return 处理器名称
     */
    String getProcessorName();

}