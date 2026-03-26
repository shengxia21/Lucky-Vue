package com.lucky.ai.service;

import com.lucky.ai.controller.chat.vo.message.AiChatMessagePageReqVO;
import com.lucky.ai.controller.chat.vo.message.AiChatMessageRespVO;
import com.lucky.ai.core.vo.chat.ChatMessageRequest;
import com.lucky.ai.core.vo.chat.ChatMessageResponse;
import com.lucky.ai.domain.AiChatMessage;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * AI 聊天消息Service接口
 *
 * @author lucky
 */
public interface IAiChatMessageService {

    /**
     * 发送消息（流式）
     *
     * @param sendReqVO 发送消息（流式）请求VO
     * @param userId    用户ID
     * @return 发送消息（流式）响应VO
     */
    Flux<ChatMessageResponse> sendChatMessageStream(ChatMessageRequest sendReqVO, Long userId);

    /**
     * 根据会话ID查询聊天消息列表
     *
     * @param conversationId 会话ID
     * @return 聊天消息列表
     */
    List<AiChatMessage> getChatMessageListByConversationId(Long conversationId);

    /**
     * 删除消息
     *
     * @param id     消息ID
     * @param userId 用户ID
     * @return 结果
     */
    int deleteChatMessage(Long id, Long userId);

    /**
     * 删除指定对话的消息
     *
     * @param conversationId 会话ID
     * @param userId         用户ID
     * @return 结果
     */
    int deleteChatMessageByConversationId(Long conversationId, Long userId);

    /**
     * 查询聊天消息分页列表
     *
     * @param pageReqVO 分页查询请求VO
     * @return 聊天消息分页列表
     */
    List<AiChatMessageRespVO> getChatMessagePage(AiChatMessagePageReqVO pageReqVO);

    /**
     * 删除消息（管理员）
     *
     * @param id 聊天消息ID
     * @return 结果
     */
    int deleteChatMessageByAdmin(Long id);

}