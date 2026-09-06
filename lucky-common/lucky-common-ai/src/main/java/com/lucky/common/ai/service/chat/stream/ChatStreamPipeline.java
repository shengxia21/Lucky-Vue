package com.lucky.common.ai.service.chat.stream;

import com.lucky.common.ai.chat.emitter.ChatMessageUpdateEmitter;
import com.lucky.common.ai.chat.memory.LuckyChatMemory;
import com.lucky.common.ai.domain.request.ChatRequest;
import com.lucky.common.ai.factory.SseEventFactory;
import com.lucky.common.ai.service.chat.AbstractChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/**
 * 聊天流编排器
 *
 * @author lucky
 */
@Slf4j
public class ChatStreamPipeline {

    /**
     * 首 token 超时：模型排队 / 预处理可能需要较长时间，单独放宽，避免刚发起请求就被判定超时
     */
    private static final Duration FIRST_TOKEN_TIMEOUT = Duration.ofSeconds(60);

    /**
     * 间隔超时：首 token 到达后，相邻两个响应的最大间隔（模型 API 卡死时及时释放连接）
     */
    private static final Duration INTERVAL_TIMEOUT = Duration.ofSeconds(15);

    /**
     * 编排聊天流
     *
     * @param chatClient     装配完成的 ChatClient
     * @param chatRequest    聊天请求
     * @param service        当前提供商的聊天服务（用于提取思考 / 正文内容）
     * @param updateEmitter  请求作用域的消息元数据收集器
     * @return 编排后的 SSE 事件流
     */
    public Flux<ServerSentEvent<String>> stream(ChatClient chatClient, ChatRequest chatRequest, AbstractChatService service, ChatMessageUpdateEmitter updateEmitter) {
        // defer：每次订阅都重新执行编排，保证返回的流可重复订阅
        return Flux.defer(() -> {
            // 调用 LLM 大模型流式请求
            Flux<ServerSentEvent<String>> llmStream = chatClient.prompt()
                    .messages(chatRequest.getMessages())
                    .advisors(a -> a.param(LuckyChatMemory.REQUEST, chatRequest))
                    .stream()
                    .chatResponse()
                    // 超时控制：区分首 token 与后续间隔
                    .timeout(Mono.delay(FIRST_TOKEN_TIMEOUT, Schedulers.parallel()), item -> Mono.delay(INTERVAL_TIMEOUT, Schedulers.parallel()))
                    // 客户端取消时记录日志（下游模型 HTTP 调用由 Reactor 自动取消）
                    .doOnCancel(() -> log.warn("AI 流式聊天被客户端取消: conversationId={}", chatRequest.getConversationId()))
                    // 将每个 ChatResponse chunk 转为 SSE 命名事件（思考内容 → thinking、正文 → text）
                    .concatMapIterable(response -> SseEventFactory.toEvents(response, service))
                    // 流式请求异常兜底：直接向 SSE 推送 error 事件，前端可识别并提示用户
                    .onErrorResume(error -> {
                        log.error("AI 流式聊天异常: conversationId={}, error={}", chatRequest.getConversationId(), error.getMessage());
                        return Flux.just(SseEventFactory.errorEvent(error.getMessage()));
                    })
                    // 流结束：推送 done 事件
                    .concatWith(Flux.just(SseEventFactory.doneEvent()))
                    // 出口流终结（完成/异常/取消）时关闭元数据流，保证 merge 整体完成
                    .doFinally(signal -> updateEmitter.complete());

            // 元数据流在前、LLM 流在后合并订阅：
            // 1. 用户消息元数据在 t0（流开始）即下发，不必等到流末尾，断连也不丢
            // 2. 客户端断连时 merge 取消 LLM 流订阅，上游连接随之释放
            return updateEmitter.asFlux().mergeWith(llmStream);
        });
    }

}
