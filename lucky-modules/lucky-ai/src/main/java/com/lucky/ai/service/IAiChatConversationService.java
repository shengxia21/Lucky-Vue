package com.lucky.ai.service;

import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.query.conversation.AiChatConversationCreateMyQuery;
import com.lucky.ai.domain.query.conversation.AiChatConversationQuery;
import com.lucky.ai.domain.query.conversation.AiChatConversationUpdateMyQuery;
import com.lucky.ai.domain.vo.conversation.AiChatConversationVO;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;

import java.util.List;

/**
 * AI 聊天对话Service接口
 *
 * @author lucky
 */
public interface IAiChatConversationService {

    /**
     * 获得【我的】聊天对话列表
     *
     * @return 聊天对话列表
     */
    List<AiChatConversationVO> selectMyChatConversationList();

    /**
     * 获得【我的】聊天对话
     *
     * @param id 对话ID
     * @return 聊天对话
     */
    AiChatConversationVO selectMyChatConversationById(Long id);

    /**
     * 创建【我的】聊天对话
     *
     * @param query 创建对象
     * @return 创建的对话id
     */
    Long insertMyChatConversation(AiChatConversationCreateMyQuery query);

    /**
     * 更新【我的】聊天对话
     *
     * @param query 更新对象
     * @return 是否成功
     */
    int updateMyChatConversation(AiChatConversationUpdateMyQuery query);

    /**
     * 删除【我的】聊天对话
     *
     * @param id 对话ID
     * @return 是否成功
     */
    int deleteMyChatConversationById(Long id);

    /**
     * 删除【我的】未置顶聊天对话
     *
     * @return 是否成功
     */
    int deleteMyUnpinnedChatConversation();

    /**
     * 获得对话分页列表
     *
     * @param pageQuery 分页查询对象
     * @param query     查询参数
     * @return 对话分页列表
     */
    TableDataInfo<AiChatConversationVO> selectChatConversationList(PageQuery pageQuery, AiChatConversationQuery query);

    /**
     * 校验对话是否存在
     *
     * @param id 对话ID
     * @return 对话
     */
    AiChatConversation validateChatConversationExists(Long id);

}
