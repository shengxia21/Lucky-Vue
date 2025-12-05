package com.lucky.ai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.lucky.ai.controller.chat.vo.conversation.AiChatConversationPageReqVO;
import com.lucky.ai.controller.chat.vo.conversation.AiChatConversationRespVO;
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
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.lucky.ai.util.CollectionUtils.convertList;

/**
 * AI 聊天对话Service业务层处理
 *
 * @author lucky
 */
@Service
public class AiChatConversationServiceImpl implements IAiChatConversationService {

    @Resource
    private AiChatConversationMapper aiChatConversationMapper;

    @Resource
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
    public int updateChatConversationMy(AiChatConversationUpdateMyReqVO updateReqVO, Long userId) {
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
        return aiChatConversationMapper.updateAiChatConversation(updateObj);
    }

    /**
     * 获得我的聊天对话列表
     *
     * @param userId 用户ID
     * @return 聊天对话列表
     */
    @Override
    public List<AiChatConversation> getChatConversationListByUserId(Long userId) {
        return aiChatConversationMapper.selectListByUserId(userId);
    }

    /**
     * 获得我的聊天对话
     *
     * @param id 对话ID
     * @return 聊天对话
     */
    @Override
    public AiChatConversation getChatConversation(Long id) {
        return aiChatConversationMapper.selectAiChatConversationById(id);
    }

    /**
     * 删除我的聊天对话
     *
     * @param id     对话ID
     * @param userId 用户ID
     * @return 是否成功
     */
    @Override
    public int deleteChatConversationMy(Long id, Long userId) {
        // 1. 校验对话是否存在
        AiChatConversation conversation = validateChatConversationExists(id);
        if (conversation == null || ObjUtil.notEqual(conversation.getUserId(), userId)) {
            throw new ServiceException(AiErrorConstants.CHAT_CONVERSATION_NOT_EXISTS);
        }
        // 2. 删除对话
        return aiChatConversationMapper.deleteAiChatConversationById(id);
    }

    /**
     * 删除我的未置顶聊天对话
     *
     * @param userId 用户ID
     * @return 是否成功
     */
    @Override
    public int deleteChatConversationMyByUnpinned(Long userId) {
        List<AiChatConversation> list = aiChatConversationMapper.selectListByUserIdAndPinned(userId, false);
        if (CollUtil.isEmpty(list)) {
            return 0;
        }
        return aiChatConversationMapper.deleteAiChatConversationByIds(convertList(list, AiChatConversation::getId));
    }

    /**
     * 获取对话分页列表
     *
     * @param pageReqVO 分页查询对象
     * @return 分页列表
     */
    @Override
    public List<AiChatConversationRespVO> getChatConversationPage(AiChatConversationPageReqVO pageReqVO) {
        return aiChatConversationMapper.selectChatConversationPage(pageReqVO);
    }

    /**
     * 管理员删除对话
     *
     * @param id 对话ID
     * @return 是否成功
     */
    @Override
    public int deleteChatConversationByAdmin(Long id) {
        // 1. 校验对话是否存在
        AiChatConversation conversation = validateChatConversationExists(id);
        if (conversation == null) {
            throw new ServiceException(AiErrorConstants.CHAT_CONVERSATION_NOT_EXISTS);
        }
        // 2. 删除对话
        return aiChatConversationMapper.deleteAiChatConversationById(id);
    }

    /**
     * 校验对话是否存在
     *
     * @param id 对话ID
     * @return 对话
     */
    @Override
    public AiChatConversation validateChatConversationExists(Long id) {
        AiChatConversation conversation = aiChatConversationMapper.selectAiChatConversationById(id);
        if (conversation == null) {
            throw new ServiceException(AiErrorConstants.CHAT_CONVERSATION_NOT_EXISTS);
        }
        return conversation;
    }

    private void validateChatModel(AiModel model) {
        if (ObjectUtil.isAllNotEmpty(model.getTemperature(), model.getMaxTokens(), model.getMaxContexts())) {
            return;
        }
        Assert.equals(model.getType(), AiModelTypeEnum.CHAT.getType(), "模型类型不正确：" + model);
        throw new ServiceException(AiErrorConstants.CHAT_CONVERSATION_MODEL_ERROR);
    }

}
