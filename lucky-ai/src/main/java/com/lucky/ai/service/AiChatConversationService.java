package com.lucky.ai.service;

import com.lucky.ai.controller.chat.vo.conversation.AiChatConversationCreateMyReqVO;
import com.lucky.ai.controller.chat.vo.conversation.AiChatConversationPageReqVO;
import com.lucky.ai.controller.chat.vo.conversation.AiChatConversationRespVO;
import com.lucky.ai.controller.chat.vo.conversation.AiChatConversationUpdateMyReqVO;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;

import java.util.List;

/**
 * AI 聊天对话Service接口
 *
 * @author lucky
 */
public interface AiChatConversationService {

    /**
     * 创建我的聊天对话
     *
     * @param userId 用户ID
     * @return 聊天对话ID
     */
    Long createChatConversationMy(AiChatConversationCreateMyReqVO createReqVO, Long userId);

    /**
     * 更新我的聊天对话
     *
     * @param updateReqVO 更新对象
     * @param userId      用户ID
     */
    int updateChatConversationMy(AiChatConversationUpdateMyReqVO updateReqVO, Long userId);

    /**
     * 获得我的聊天对话列表
     *
     * @param userId 用户ID
     * @return 聊天对话列表
     */
    List<AiChatConversationRespVO> getChatConversationListByUserId(Long userId);

    /**
     * 获得我的聊天对话
     *
     * @param id 对话ID
     * @return 聊天对话
     */
    AiChatConversation getChatConversationById(Long id);

    /**
     * 删除我的聊天对话
     *
     * @param id     对话ID
     * @param userId 用户ID
     * @return 是否成功
     */
    int deleteChatConversationMy(Long id, Long userId);

    /**
     * 删除我的未置顶聊天对话
     *
     * @param userId 用户ID
     * @return 是否成功
     */
    int deleteChatConversationMyByUserId(Long userId);

    /**
     * 获得对话分页列表
     *
     * @param pageQuery 分页查询对象
     * @param pageReqVO 查询参数
     * @return 对话分页列表
     */
    TableDataInfo<AiChatConversationRespVO> getChatConversationPage(PageQuery pageQuery, AiChatConversationPageReqVO pageReqVO);

    /**
     * 管理员删除对话
     *
     * @param id 对话ID
     * @return 是否成功
     */
    int deleteChatConversationById(Long id);

    /**
     * 校验对话是否存在
     *
     * @param id 对话ID
     * @return 对话
     */
    AiChatConversation validateChatConversationExists(Long id);

}
