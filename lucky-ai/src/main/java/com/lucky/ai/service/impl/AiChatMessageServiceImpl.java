package com.lucky.ai.service.impl;

import com.lucky.ai.domain.AiChatMessage;
import com.lucky.ai.mapper.AiChatMessageMapper;
import com.lucky.ai.service.IAiChatMessageService;
import com.lucky.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI 聊天消息Service业务层处理
 * 
 * @author lucky
 */
@Service
public class AiChatMessageServiceImpl implements IAiChatMessageService {

    @Autowired
    private AiChatMessageMapper aiChatMessageMapper;

    /**
     * 查询AI 聊天消息
     * 
     * @param id AI 聊天消息主键
     * @return AI 聊天消息
     */
    @Override
    public AiChatMessage selectAiChatMessageById(Long id) {
        return aiChatMessageMapper.selectAiChatMessageById(id);
    }

    /**
     * 查询AI 聊天消息列表
     * 
     * @param aiChatMessage AI 聊天消息
     * @return AI 聊天消息
     */
    @Override
    public List<AiChatMessage> selectAiChatMessageList(AiChatMessage aiChatMessage) {
        return aiChatMessageMapper.selectAiChatMessageList(aiChatMessage);
    }

    /**
     * 新增AI 聊天消息
     * 
     * @param aiChatMessage AI 聊天消息
     * @return 结果
     */
    @Override
    public int insertAiChatMessage(AiChatMessage aiChatMessage) {
        aiChatMessage.setCreateTime(DateUtils.getNowDate());
        return aiChatMessageMapper.insertAiChatMessage(aiChatMessage);
    }

    /**
     * 修改AI 聊天消息
     * 
     * @param aiChatMessage AI 聊天消息
     * @return 结果
     */
    @Override
    public int updateAiChatMessage(AiChatMessage aiChatMessage) {
        aiChatMessage.setUpdateTime(DateUtils.getNowDate());
        return aiChatMessageMapper.updateAiChatMessage(aiChatMessage);
    }

    /**
     * 批量删除AI 聊天消息
     * 
     * @param ids 需要删除的AI 聊天消息主键
     * @return 结果
     */
    @Override
    public int deleteAiChatMessageByIds(Long[] ids) {
        return aiChatMessageMapper.deleteAiChatMessageByIds(ids);
    }

    /**
     * 删除AI 聊天消息信息
     * 
     * @param id AI 聊天消息主键
     * @return 结果
     */
    @Override
    public int deleteAiChatMessageById(Long id) {
        return aiChatMessageMapper.deleteAiChatMessageById(id);
    }

}
