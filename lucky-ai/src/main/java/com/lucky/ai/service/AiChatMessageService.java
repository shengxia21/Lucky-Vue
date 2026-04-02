package com.lucky.ai.service;

import com.lucky.ai.core.vo.chat.ChatMessageRequest;
import com.lucky.ai.core.vo.chat.ChatMessageResponse;
import com.lucky.ai.domain.query.message.AiChatMessagePageQuery;
import com.lucky.ai.domain.vo.message.AiChatMessageVO;
import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * AI 聊天消息Service接口
 *
 * @author lucky
 */
public interface AiChatMessageService {

    /**
     * 发送消息（流式）
     *
     * @param query 发送消息（流式）请求VO
     * @param userId    用户ID
     * @return 发送消息（流式）响应VO
     */
    Flux<ChatMessageResponse> sendChatMessageStream(ChatMessageRequest query, Long userId);

    /**
     * 根据会话ID查询聊天消息列表
     *
     * @param conversationId 会话ID
     * @return 聊天消息列表
     */
    List<AiChatMessageVO> getChatMessageListByConversationId(Long conversationId);

    /**
     * 删除消息
     *
     * @param id     消息ID
     * @param userId 用户ID
     * @return 结果
     */
    int deleteChatMessageByIdAndUserId(Long id, Long userId);

    /**
     * 删除指定对话的消息
     *
     * @param conversationId 会话ID
     * @param userId         用户ID
     * @return 结果
     */
    int deleteChatMessageByConversationIdAndUserId(Long conversationId, Long userId);

    /**
     * 查询聊天消息分页列表
     *
     * @param pageQuery 分页查询参数
     * @param query 查询参数
     * @return 聊天消息分页列表
     */
    TableDataInfo<AiChatMessageVO> getChatMessagePage(PageQuery pageQuery, AiChatMessagePageQuery query);

    /**
     * 删除消息（管理员）
     *
     * @param id 聊天消息ID
     * @return 结果
     */
    int deleteChatMessageById(Long id);

    /**
     * 获得聊天对话的消息数量 Map
     *
     * @param conversationIds 对话编号数组
     * @return 消息数量 Map
     */
    Map<Long, Integer> getChatMessageCountMap(Collection<Long> conversationIds);

}