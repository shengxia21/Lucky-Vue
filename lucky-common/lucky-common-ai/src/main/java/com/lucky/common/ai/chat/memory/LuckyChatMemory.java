package com.lucky.common.ai.chat.memory;

import com.lucky.common.ai.chat.aggregator.LuckyMessageAggregator;
import org.springframework.ai.chat.messages.Message;

import java.util.List;
import java.util.Map;

/**
 * Lucky 聊天记忆接口
 *
 * @author lucky
 */
public interface LuckyChatMemory {

    String REQUEST = "chat_request";

    /**
     * 添加用户消息
     *
     * @param context 上下文
     * @param message 消息
     */
    void addUserMessage(Map<String, Object> context, Message message);

    /**
     * 添加assistant消息
     *
     * @param context  上下文
     * @param usage    token使用情况
     * @param messages 消息列表
     */
    void addAssistantMessage(Map<String, Object> context, LuckyMessageAggregator.DefaultUsage usage, List<Message> messages);

    /**
     * 获取历史消息列表
     *
     * @param conversationId      会话id
     * @param historyMessageCount 携带历史消息数
     * @return 消息列表
     */
    List<Message> get(Long conversationId, Integer historyMessageCount);

}
