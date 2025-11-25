package com.lucky.ai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.lucky.ai.controller.chat.vo.conversation.AiChatConversationUpdateMyReqVO;
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
        // 1 获得 AiModel 聊天模型
        AiModel model = aiModelService.getRequiredDefaultModel(AiModelTypeEnum.CHAT.getType());
        Assert.notNull(model, "必须找到默认模型");
        validateChatModel(model);

        // 2. 创建 AiChatConversation 聊天对话
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
     * 更新我的聊天对话
     *
     * @param updateReqVO 更新对象
     * @param userId      用户ID
     */
    @Override
    public void updateChatConversationMy(AiChatConversationUpdateMyReqVO updateReqVO, Long userId) {
        // 1.1 校验对话是否存在
        AiChatConversation conversation = validateChatConversationExists(updateReqVO.getId());
        if (ObjUtil.notEqual(conversation.getUserId(), userId)) {
            throw new ServiceException(AiErrorConstants.CHAT_CONVERSATION_NOT_EXISTS);
        }
        // 1.2 校验模型是否存在（修改模型的情况）
        AiModel model = null;
        if (updateReqVO.getModelId() != null) {
            model = aiModelService.validateModel(updateReqVO.getModelId());
        }

        // 2. 更新对话信息
        AiChatConversation updateObj = BeanUtil.toBean(updateReqVO, AiChatConversation.class);
        if (Boolean.TRUE.equals(updateReqVO.getPinned())) {
            updateObj.setPinnedTime(DateUtils.getNowDate());
        }
        if (model != null) {
            updateObj.setModel(model.getModel());
        }
        updateObj.setUpdateBy(SecurityUtils.getUsername());
        updateObj.setUpdateTime(DateUtils.getNowDate());
        aiChatConversationMapper.updateAiChatConversation(updateObj);
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

    public AiChatConversation validateChatConversationExists(Long id) {
        AiChatConversation conversation = aiChatConversationMapper.selectAiChatConversationById(id);
        if (conversation == null) {
            throw new ServiceException(AiErrorConstants.CHAT_CONVERSATION_NOT_EXISTS);
        }
        return conversation;
    }

}
