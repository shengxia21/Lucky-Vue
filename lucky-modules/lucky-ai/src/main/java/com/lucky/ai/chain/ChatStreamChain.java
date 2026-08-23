package com.lucky.ai.chain;

import com.lucky.ai.chain.context.ChatStreamContext;
import com.lucky.ai.chain.handler.ChatStreamHandler;
import com.lucky.ai.domain.query.chat.ChatQuery;
import com.lucky.common.ai.factory.SseEventFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.List;

/**
 * 流式聊天责任链
 *
 * @author lucky
 */
@Slf4j
@Component
public class ChatStreamChain {

    @Resource
    private List<ChatStreamHandler> handlers;

    @Resource
    private LlmChatExecutor llmChatExecutor;

    /**
     * 执行责任链
     *
     * @param query 聊天请求参数
     * @return SSE 事件流：处理器中途推送的实时事件 + 出口流
     */
    public Flux<ServerSentEvent<String>> execute(ChatQuery query) {
        // 外层 defer：每次订阅创建独立的事件流与上下文，保证返回的流可重复订阅
        return Flux.defer(() -> {
            // 事件流：unicast 单订阅者（唯一订阅者为下方 merge），订阅前推送的事件先缓冲、订阅后回放
            Sinks.Many<ServerSentEvent<String>> eventSink = Sinks.many().unicast().onBackpressureBuffer();
            ChatStreamContext context = new ChatStreamContext(query, eventSink);
            // 出口流：责任链延迟到 merge 订阅时才同步执行，处理器中途推送的事件得以实时下发
            Flux<ServerSentEvent<String>> outlet = Flux.defer(() -> runChain(context))
                    // 出口流终结（完成/异常/取消）时关闭事件流，保证 merge 整体完成
                    .doFinally(signal -> eventSink.tryEmitComplete());
            // 事件流在前、出口流在后合并订阅：
            // 1. 处理器中途推送的事件实时到达前端，出口流（LLM 结果）随后
            // 2. 客户端断连时 merge 取消出口流订阅，LLM 上游连接随之释放
            return eventSink.asFlux().mergeWith(outlet);
        });
    }

    /**
     * 同步执行责任链，返回出口流
     *
     * @param context 责任链上下文
     * @return 出口流：正常完成为 LLM 结果流；处理器异常为 error + done 事件
     */
    private Flux<ServerSentEvent<String>> runChain(ChatStreamContext context) {
        ChatQuery query = context.getQuery();
        // 处理器：校验与准备，任一异常即终止链路
        for (ChatStreamHandler handler : handlers) {
            try {
                handler.handle(context);
            } catch (Exception e) {
                log.error("责任链处理器[{}]执行异常: conversationId={}, 原因={}", handler.getName(), query.getConversationId(), e.getMessage());
                return Flux.just(SseEventFactory.errorEvent(e.getMessage()), SseEventFactory.doneEvent());
            }
        }
        // 调用 LLM 大模型，结果流作为出口流返回
        return llmChatExecutor.chat(context);
    }

}
