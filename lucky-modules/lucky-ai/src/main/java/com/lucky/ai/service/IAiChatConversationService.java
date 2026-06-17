package com.lucky.ai.service;

import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.query.conversation.AiChatConversationCreateMyQuery;
import com.lucky.ai.domain.query.conversation.AiChatConversationPageQuery;
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
     * 创建我的聊天对话
     *
     * @return 聊天对话ID
     */
    Long insertMyChatConversation(AiChatConversationCreateMyQuery query);

    /**
     * 更新我的聊天对话
     *
     * @param query 更新对象
     * @return 是否成功
     */
    int updateMyChatConversation(AiChatConversationUpdateMyQuery query);

    /**
     * 获得我的聊天对话列表
     *
     * @return 聊天对话列表
     */
    List<AiChatConversationVO> selectMyChatConversationList();

    /**
     * 获得我的聊天对话
     *
     * @param id 对话ID
     * @return 聊天对话
     */
    AiChatConversationVO selectChatConversationById(Long id);

    /**
     * 删除我的聊天对话
     *
     * @param id 对话ID
     * @return 是否成功
     */
    int deleteMyChatConversationById(Long id);

    /**
     * 删除我的未置顶聊天对话
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
    TableDataInfo<AiChatConversationVO> selectChatConversationList(PageQuery pageQuery, AiChatConversationPageQuery query);

    /**
     * 管理员删除对话
     *
     * @param ids 对话ID数组
     * @return 是否成功
     */
    int deleteChatConversationByIds(Long[] ids);

    /**
     * 校验对话是否存在
     *
     * @param id 对话ID
     * @return 对话
     */
    AiChatConversation validateChatConversationExists(Long id);

}
