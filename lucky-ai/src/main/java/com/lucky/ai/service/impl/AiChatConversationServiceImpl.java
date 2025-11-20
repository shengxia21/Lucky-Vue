package com.lucky.ai.service.impl;

import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.mapper.AiChatConversationMapper;
import com.lucky.ai.service.IAiChatConversationService;
import com.lucky.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI 聊天对话Service业务层处理
 * 
 * @author lucky
 */
@Service
public class AiChatConversationServiceImpl implements IAiChatConversationService {

    @Autowired
    private AiChatConversationMapper aiChatConversationMapper;

    /**
     * 查询AI 聊天对话
     * 
     * @param id AI 聊天对话主键
     * @return AI 聊天对话
     */
    @Override
    public AiChatConversation selectAiChatConversationById(Long id) {
        return aiChatConversationMapper.selectAiChatConversationById(id);
    }

    /**
     * 查询AI 聊天对话列表
     * 
     * @param aiChatConversation AI 聊天对话
     * @return AI 聊天对话
     */
    @Override
    public List<AiChatConversation> selectAiChatConversationList(AiChatConversation aiChatConversation) {
        return aiChatConversationMapper.selectAiChatConversationList(aiChatConversation);
    }

    /**
     * 新增AI 聊天对话
     * 
     * @param aiChatConversation AI 聊天对话
     * @return 结果
     */
    @Override
    public int insertAiChatConversation(AiChatConversation aiChatConversation) {
        aiChatConversation.setCreateTime(DateUtils.getNowDate());
        return aiChatConversationMapper.insertAiChatConversation(aiChatConversation);
    }

    /**
     * 修改AI 聊天对话
     * 
     * @param aiChatConversation AI 聊天对话
     * @return 结果
     */
    @Override
    public int updateAiChatConversation(AiChatConversation aiChatConversation) {
        aiChatConversation.setUpdateTime(DateUtils.getNowDate());
        return aiChatConversationMapper.updateAiChatConversation(aiChatConversation);
    }

    /**
     * 批量删除AI 聊天对话
     * 
     * @param ids 需要删除的AI 聊天对话主键
     * @return 结果
     */
    @Override
    public int deleteAiChatConversationByIds(Long[] ids) {
        return aiChatConversationMapper.deleteAiChatConversationByIds(ids);
    }

    /**
     * 删除AI 聊天对话信息
     * 
     * @param id AI 聊天对话主键
     * @return 结果
     */
    @Override
    public int deleteAiChatConversationById(Long id) {
        return aiChatConversationMapper.deleteAiChatConversationById(id);
    }

}
