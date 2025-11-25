package com.lucky.ai.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.enums.model.AiModelTypeEnum;
import com.lucky.ai.mapper.AiChatConversationMapper;
import com.lucky.ai.service.IAiChatConversationService;
import com.lucky.ai.service.IAiModelService;
import com.lucky.common.constant.AiErrorConstants;
import com.lucky.common.exception.ServiceException;
import com.lucky.common.utils.DateUtils;
import com.lucky.common.utils.SecurityUtils;
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

    @Autowired
    private IAiModelService aiModelService;

    /**
     * 创建我的聊天对话
     *
     * @param userId 用户ID
     * @return 聊天对话ID
     */
    @Override
    public Long createChatConversationMy(Long userId) {
        AiModel model = aiModelService.getRequiredDefaultModel(AiModelTypeEnum.CHAT.getType());
        Assert.notNull(model, "必须找到默认模型");
        validateChatModel(model);
        AiChatConversation conversation = new AiChatConversation();
        conversation.setUserId(userId);
        conversation.setPinned(false);
        conversation.setModelId(model.getId());
        conversation.setModel(model.getModel());
        conversation.setTemperature(model.getTemperature());
        conversation.setMaxTokens(model.getMaxTokens());
        conversation.setMaxContexts(model.getMaxContexts());
        conversation.setTitle(AiChatConversation.TITLE_DEFAULT);
        conversation.setCreateTime(DateUtils.getNowDate());
        conversation.setCreateBy(SecurityUtils.getUsername());
        aiChatConversationMapper.insertAiChatConversation(conversation);
        return conversation.getId();
    }

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

    private void validateChatModel(AiModel model) {
        if (ObjectUtil.isAllNotEmpty(model.getTemperature(), model.getMaxTokens(), model.getMaxContexts())) {
            return;
        }
        Assert.equals(model.getType(), AiModelTypeEnum.CHAT.getType(), "模型类型不正确：" + model);
        throw new ServiceException(AiErrorConstants.CHAT_CONVERSATION_MODEL_ERROR);
    }

}
