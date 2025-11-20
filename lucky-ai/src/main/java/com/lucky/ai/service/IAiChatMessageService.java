package com.lucky.ai.service;

import com.lucky.ai.domain.AiChatMessage;

import java.util.List;

/**
 * AI 聊天消息Service接口
 * 
 * @author lucky
 */
public interface IAiChatMessageService {

    /**
     * 查询AI 聊天消息
     * 
     * @param id AI 聊天消息主键
     * @return AI 聊天消息
     */
    AiChatMessage selectAiChatMessageById(Long id);

    /**
     * 查询AI 聊天消息列表
     * 
     * @param aiChatMessage AI 聊天消息
     * @return AI 聊天消息集合
     */
    List<AiChatMessage> selectAiChatMessageList(AiChatMessage aiChatMessage);

    /**
     * 新增AI 聊天消息
     * 
     * @param aiChatMessage AI 聊天消息
     * @return 结果
     */
    int insertAiChatMessage(AiChatMessage aiChatMessage);

    /**
     * 修改AI 聊天消息
     * 
     * @param aiChatMessage AI 聊天消息
     * @return 结果
     */
    int updateAiChatMessage(AiChatMessage aiChatMessage);

    /**
     * 批量删除AI 聊天消息
     * 
     * @param ids 需要删除的AI 聊天消息主键集合
     * @return 结果
     */
    int deleteAiChatMessageByIds(Long[] ids);

    /**
     * 删除AI 聊天消息信息
     * 
     * @param id AI 聊天消息主键
     * @return 结果
     */
    int deleteAiChatMessageById(Long id);

}
