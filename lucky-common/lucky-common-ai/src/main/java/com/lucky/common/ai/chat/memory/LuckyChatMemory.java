package com.lucky.common.ai.chat.memory;

import com.lucky.common.ai.chat.aggregator.LuckyMessageAggregator;
import com.lucky.common.ai.domain.dto.ChatMessageUpdateDTO;
import com.lucky.common.ai.domain.request.ChatRequest;
import org.springframework.ai.chat.messages.Message;

import java.util.List;

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
     * @param chatRequest 聊天请求
     * @param message     消息
     * @return 保存后的消息元数据（含存储库生成的编号与创建时间）
     */
    ChatMessageUpdateDTO addUserMessage(ChatRequest chatRequest, Message message);

    /**
     * 添加assistant消息
     *
     * @param chatRequest 聊天请求
     * @param usage       token使用情况
     * @param messages    消息列表
     * @return 保存后的消息元数据（含存储库生成的编号与创建时间、模型与 token 信息）
     */
    ChatMessageUpdateDTO addAssistantMessage(ChatRequest chatRequest, LuckyMessageAggregator.DefaultUsage usage, List<Message> messages);

    /**
     * 获取历史消息列表
     *
     * @param conversationId      会话id
     * @param historyMessageCount 携带历史消息数
     * @return 消息列表
     */
    List<Message> get(Long conversationId, Integer historyMessageCount);

}