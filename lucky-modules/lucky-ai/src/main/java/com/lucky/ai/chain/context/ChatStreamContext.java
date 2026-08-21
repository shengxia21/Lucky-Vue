package com.lucky.ai.chain.context;

import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.AiChatRole;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.domain.query.chat.ChatQuery;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.List;

/**
 * 流式聊天责任链上下文
 *
 * @author lucky
 */
@Slf4j
@Data
public class ChatStreamContext {

    /**
     * 原始聊天请求参数
     */
    private final ChatQuery query;

    /**
     * 对话信息（对话校验处理器写入）
     */
    private AiChatConversation conversation;

    /**
     * 会话角色信息（角色校验处理器写入，未配置角色时为空对象）
     */
    private AiChatRole role;

    /**
     * 模型信息（模型校验处理器写入）
     */
    private AiModel model;

    /**
     * 模型密钥信息（apikey 校验处理器写入）
     */
    private AiApiKey apiKey;

    /**
     * 消息列表（消息构建处理器写入：系统消息 → 用户消息）
     */
    private List<Message> messages;

    /**
     * 最终结果流（LLM 调用处理器写入，链路在此短路返回）
     */
    private Flux<ServerSentEvent<String>> result;

    /**
     * 事件流（由链创建并注入：处理器执行中途推送的事件经此实时下发前端，
     * unicast 单订阅者，唯一订阅者为链出口处的 merge）
     */
    private Sinks.Many<ServerSentEvent<String>> eventSink;

    /**
     * 短路标志（处理器通过 shortCircuit 设置，链据此终止后续处理器执行）
     */
    private boolean shortCircuit = false;

    /**
     * 向流实时发送自定义事件
     *
     * @param eventName 事件类型（与前端约定，如 search、notice）
     * @param data      事件数据（文本内容）
     */
    public void sendEvent(String eventName, String data) {
        sendEvent(ServerSentEvent.<String>builder().event(eventName).data(data).build());
    }

    /**
     * 向流实时发送事件
     *
     * @param event SSE 事件
     */
    public void sendEvent(ServerSentEvent<String> event) {
        // tryEmitNext 即时发射：链执行期间事件实时流向前端
        Sinks.EmitResult emitResult = eventSink.tryEmitNext(event);
        // 客户端已断开或流已终结时推送失败：仅记录日志，不影响责任链继续执行
        if (emitResult.isFailure()) {
            /// TO DO 推送失败终结后续执行操作，并断开连接（某种原因可能已断开连接）
            log.debug("流事件推送失败[{}]: event={}", emitResult, event.event());
        }
    }

    /**
     * 动态短路：终止后续处理器的执行
     */
    public void shortCircuit() {
        this.shortCircuit = true;
    }

}
