package com.lucky.ai.service;

import com.lucky.ai.domain.AiChatConversation;

import java.util.List;

/**
 * AI 聊天对话Service接口
 * 
 * @author lucky
 */
public interface IAiChatConversationService {

    /**
     * 查询AI 聊天对话
     * 
     * @param id AI 聊天对话主键
     * @return AI 聊天对话
     */
    AiChatConversation selectAiChatConversationById(Long id);

    /**
     * 查询AI 聊天对话列表
     * 
     * @param aiChatConversation AI 聊天对话
     * @return AI 聊天对话集合
     */
    List<AiChatConversation> selectAiChatConversationList(AiChatConversation aiChatConversation);

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
     * 批量删除AI 聊天对话
     * 
     * @param ids 需要删除的AI 聊天对话主键集合
     * @return 结果
     */
    int deleteAiChatConversationByIds(Long[] ids);

    /**
     * 删除AI 聊天对话信息
     * 
     * @param id AI 聊天对话主键
     * @return 结果
     */
    int deleteAiChatConversationById(Long id);

}
