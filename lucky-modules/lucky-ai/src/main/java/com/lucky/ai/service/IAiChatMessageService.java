package com.lucky.ai.service;

import com.lucky.ai.domain.query.message.AiChatMessageQuery;
import com.lucky.ai.domain.vo.message.AiChatMessageVO;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;

import java.util.List;

/**
 * AI 聊天消息Service接口
 *
 * @author lucky
 */
public interface IAiChatMessageService {

    /**
     * 查询【我的】指定对话的消息列表
     *
     * @param conversationId 会话ID
     * @return 聊天消息列表
     */
    List<AiChatMessageVO> selectMyChatMessageListByConversationId(Long conversationId);

    /**
     * 删除【我的】消息
     *
     * @param id 消息ID
     * @return 结果
     */
    int deleteMyChatMessageById(Long id);

    /**
     * 删除【我的】指定对话的消息
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
    TableDataInfo<AiChatMessageVO> selectChatMessageList(PageQuery pageQuery, AiChatMessageQuery query);

    /**
     * 删除消息
     *
     * @param ids 聊天消息ID数组
     * @return 结果
     */
    int deleteChatMessageByIds(Long[] ids);

}