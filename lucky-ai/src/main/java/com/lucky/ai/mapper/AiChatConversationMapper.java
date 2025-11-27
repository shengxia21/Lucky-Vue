package com.lucky.ai.mapper;

import com.lucky.ai.controller.chat.vo.conversation.AiChatConversationPageReqVO;
import com.lucky.ai.controller.chat.vo.conversation.AiChatConversationRespVO;
import com.lucky.ai.domain.AiChatConversation;

import java.util.List;

/**
 * AI 聊天对话Mapper接口
 *
 * @author lucky
 */
public interface AiChatConversationMapper {

    /**
     * 根据用户ID查询聊天对话列表
     *
     * @param userId 用户ID
     * @return 聊天对话列表
     */
    List<AiChatConversation> selectListByUserId(Long userId);

    /**
     * 查询AI 聊天对话
     *
     * @param id AI 聊天对话主键
     * @return AI 聊天对话
     */
    AiChatConversation selectAiChatConversationById(Long id);

    /**
     * 新增AI 聊天对话
     *
     * @param aiChatConversation AI 聊天对话
     * @return 结果
     */
    int insertAiChatConversation(AiChatConversation aiChatConversation);

    /**
     * 修改AI 聊天对话
     *
     * @param aiChatConversation AI 聊天对话
     * @return 结果
     */
    int updateAiChatConversation(AiChatConversation aiChatConversation);

    /**
     * 删除AI 聊天对话
     *
     * @param id AI 聊天对话主键
     * @return 结果
     */
    int deleteAiChatConversationById(Long id);

    /**
     * 根据用户ID查询置顶/未置顶的聊天对话列表
     *
     * @param userId 用户ID
     * @param pinned 是否置顶
     * @return 聊天对话列表
     */
    List<AiChatConversation> selectListByUserIdAndPinned(Long userId, boolean pinned);

    /**
     * 根据ID列表删除AI 聊天对话
     *
     * @param ids ID列表
     * @return 结果
     */
    int deleteAiChatConversationByIds(List<Long> ids);

    /**
     * 查询AI 聊天对话分页列表
     *
     * @param pageReqVO 分页查询对象
     * @return 聊天对话分页列表
     */
    List<AiChatConversationRespVO> selectChatConversationPage(AiChatConversationPageReqVO pageReqVO);

}
