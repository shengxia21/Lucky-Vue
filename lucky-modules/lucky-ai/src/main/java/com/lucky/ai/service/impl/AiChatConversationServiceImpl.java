package com.lucky.ai.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.AiChatRole;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.domain.query.conversation.AiChatConversationCreateMyQuery;
import com.lucky.ai.domain.query.conversation.AiChatConversationPageQuery;
import com.lucky.ai.domain.query.conversation.AiChatConversationUpdateMyQuery;
import com.lucky.ai.domain.vo.conversation.AiChatConversationVO;
import com.lucky.ai.mapper.AiChatConversationMapper;
import com.lucky.ai.service.IAiChatConversationService;
import com.lucky.ai.service.IAiChatMessageService;
import com.lucky.ai.service.IAiChatRoleService;
import com.lucky.ai.service.IAiModelService;
import com.lucky.common.ai.enums.AiModelTypeEnum;
import com.lucky.common.core.constant.AiErrorConstants;
import com.lucky.common.core.exception.ServiceException;
import com.lucky.common.core.utils.DateUtils;
import com.lucky.common.core.utils.MapstructUtils;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import com.lucky.common.security.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * AI 聊天对话Service业务层处理
 *
 * @author lucky
 */
@Service
public class AiChatConversationServiceImpl implements IAiChatConversationService {

    @Resource
    private AiChatConversationMapper chatConversationMapper;

    @Resource
    private IAiModelService modelService;
    @Resource
    private IAiChatRoleService chatRoleService;
    @Resource
    private IAiChatMessageService chatMessageService;

    @Override
    public Long insertMyChatConversation(AiChatConversationCreateMyQuery query) {
        // 1.1 获得 AiChatRoleDO 聊天角色
        AiChatRole role = query.getRoleId() != null ? chatRoleService.validateChatRole(query.getRoleId()) : null;
        // 1.2 获得 AiModelDO 聊天模型
        AiModel model = role != null && role.getModelId() != null ? modelService.validateModel(role.getModelId())
                : modelService.getDefaultModelByType(AiModelTypeEnum.CHAT.getType());
        Assert.notNull(model, "必须找到默认模型");
        validateChatModel(model);

        // 2. 创建 AiChatConversation 聊天对话
        AiChatConversation conversation = new AiChatConversation();
        conversation.setUserId(SecurityUtils.getUserId());
        conversation.setPinned(false);
        conversation.setModelId(model.getId());
        conversation.setModel(model.getModel());
        conversation.setTemperature(model.getTemperature());
        conversation.setMaxTokens(model.getMaxTokens());
        conversation.setMaxContexts(model.getMaxContexts());
        if (role != null) {
            conversation.setTitle(role.getName());
            conversation.setRoleId(role.getId());
            conversation.setSystemMessage(role.getSystemMessage());
        } else {
            conversation.setTitle(AiChatConversation.TITLE_DEFAULT);
        }
        chatConversationMapper.insert(conversation);
        return conversation.getId();
    }

    @Override
    public int updateMyChatConversation(AiChatConversationUpdateMyQuery query) {
        // 1.1 校验对话是否存在
        AiChatConversation conversation = validateChatConversationExists(query.getId());
        if (ObjUtil.notEqual(conversation.getUserId(), SecurityUtils.getUserId())) {
            throw new ServiceException(AiErrorConstants.CHAT_CONVERSATION_NOT_EXISTS);
        }
        // 1.2 校验模型是否存在（修改模型的情况）
        AiModel model = null;
        if (query.getModelId() != null) {
            model = modelService.validateModel(query.getModelId());
        }

        // 2. 更新对话信息
        AiChatConversation updateObj = MapstructUtils.convert(query, AiChatConversation.class);
        if (Boolean.TRUE.equals(query.getPinned())) {
            updateObj.setPinnedTime(DateUtils.getNowDate());
        }
        if (model != null) {
            updateObj.setModel(model.getModel());
        }
        return chatConversationMapper.updateById(updateObj);
    }

    @Override
    public List<AiChatConversationVO> selectMyChatConversationList() {
        return chatConversationMapper.selectListByUserId(SecurityUtils.getUserId());
    }

    @Override
    public AiChatConversationVO selectChatConversationById(Long id) {
        return chatConversationMapper.selectVoById(id);
    }

    @Override
    public int deleteMyChatConversationById(Long id) {
        // 1. 校验对话是否存在
        AiChatConversation conversation = validateChatConversationExists(id);
        // 1.1 校验对话是否属于当前用户
        if (conversation == null || ObjUtil.notEqual(conversation.getUserId(), SecurityUtils.getUserId())) {
            throw new ServiceException(AiErrorConstants.CHAT_CONVERSATION_NOT_EXISTS);
        }
        // 2. 删除对话
        return chatConversationMapper.deleteById(id);
    }

    @Override
    public int deleteMyUnpinnedChatConversation() {
        Long userId = SecurityUtils.getUserId();
        List<AiChatConversation> list = chatConversationMapper.selectListByUserIdAndPinned(userId, false);
        if (CollUtil.isEmpty(list)) {
            return 0;
        }
        List<Long> ids = list.stream().map(AiChatConversation::getId).toList();
        return chatConversationMapper.deleteByIds(ids);
    }

    @Override
    public TableDataInfo<AiChatConversationVO> selectChatConversationList(PageQuery pageQuery, AiChatConversationPageQuery query) {
        IPage<AiChatConversationVO> page = chatConversationMapper.selectPage(pageQuery.build(), query);
        if (CollUtil.isEmpty(page.getRecords())) {
            return TableDataInfo.build(page);
        }
        // 收集对话ID列表
        List<Long> ids = page.getRecords().stream().map(AiChatConversationVO::getId).toList();
        // 查询每个对话的消息数量
        Map<Long, Integer> countMap = chatMessageService.selectChatMessageCountMap(ids);
        // 添加消息数量
        page.getRecords().forEach(conversation -> conversation.setMessageCount(countMap.getOrDefault(conversation.getId(), 0)));
        return TableDataInfo.build(page);
    }

    @Override
    public int deleteChatConversationByIds(Long[] ids) {
        return chatConversationMapper.deleteByIds(Arrays.asList(ids));
    }

    @Override
    public AiChatConversation validateChatConversationExists(Long id) {
        AiChatConversation conversation = chatConversationMapper.selectById(id);
        if (conversation == null) {
            throw new ServiceException(AiErrorConstants.CHAT_CONVERSATION_NOT_EXISTS);
        }
        return conversation;
    }

    /**
     * 校验聊天模型是否正确
     *
     * @param model 聊天模型
     */
    private void validateChatModel(AiModel model) {
        if (ObjectUtil.isAllNotEmpty(model.getTemperature(), model.getMaxTokens(), model.getMaxContexts())) {
            return;
        }
        Assert.equals(model.getType(), AiModelTypeEnum.CHAT.getType(), "模型类型不正确：" + model);
        throw new ServiceException(AiErrorConstants.CHAT_CONVERSATION_MODEL_ERROR);
    }

}