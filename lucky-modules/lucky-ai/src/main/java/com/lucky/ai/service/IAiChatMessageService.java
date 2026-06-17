package com.lucky.ai.service;

import com.lucky.ai.domain.query.message.AiChatMessagePageQuery;
import com.lucky.ai.domain.vo.message.AiChatMessageVO;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * AI 聊天消息Service接口
 *
 * @author lucky
 */
public interface IAiChatMessageService {

    /**
     * 根据会话ID查询聊天消息列表
     *
     * @param conversationId 会话ID
     * @return 聊天消息列表
     */
    List<AiChatMessageVO> selectChatMessageListByConversationId(Long conversationId);

    /**
     * 删除我的消息
     *
     * @param id 消息ID
     * @return 结果
     */
    int deleteMyChatMessageById(Long id);

    /**
     * 删除我的指定对话的消息
     *
     * @param conversationId 会话ID
     * @return 结果
     */
    int deleteMyChatMessageByConversationId(Long conversationId);

    /**
     * 查询聊天消息分页列表
     *
     * @param pageQuery 分页查询参数
     * @param query     查询参数
     * @return 聊天消息分页列表
     */
    TableDataInfo<AiChatMessageVO> selectChatMessageList(PageQuery pageQuery, AiChatMessagePageQuery query);

    /**
     * 删除消息（管理员）
     *
     * @param ids 聊天消息ID数组
     * @return 结果
     */
    int deleteChatMessageByIds(Long[] ids);

    /**
     * 获得聊天对话的消息数量 Map
     *
     * @param conversationIds 对话编号数组
     * @return 消息数量 Map
     */
    Map<Long, Integer> selectChatMessageCountMap(Collection<Long> conversationIds);

}