package com.lucky.ai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.ai.controller.chat.vo.conversation.AiChatConversationCreateMyReqVO;
import com.lucky.ai.controller.chat.vo.conversation.AiChatConversationPageReqVO;
import com.lucky.ai.controller.chat.vo.conversation.AiChatConversationRespVO;
import com.lucky.ai.controller.chat.vo.conversation.AiChatConversationUpdateMyReqVO;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.AiChatRole;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.enums.model.AiModelTypeEnum;
import com.lucky.ai.mapper.AiChatConversationMapper;
import com.lucky.ai.service.AiChatConversationService;
import com.lucky.ai.service.AiChatMessageService;
import com.lucky.ai.service.AiChatRoleService;
import com.lucky.ai.service.AiModelService;
import com.lucky.common.constant.AiErrorConstants;
import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.common.exception.ServiceException;
import com.lucky.common.utils.DateUtils;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.lucky.ai.util.CollectionUtils.convertList;

/**
 * AI 聊天对话Service业务层处理
 *
 * @author lucky
 */
@Service
public class AiChatConversationServiceImpl implements AiChatConversationService {

    @Resource
    private AiChatConversationMapper chatConversationMapper;

    @Resource
    private AiModelService modelService;
    @Resource
    private AiChatRoleService chatRoleService;
    @Lazy
    @Resource
    private AiChatMessageService chatMessageService;

    /**
     * 创建我的聊天对话
     *
     * @param userId 用户ID
     * @return 聊天对话ID
     */
    @Override
    public Long createChatConversationMy(AiChatConversationCreateMyReqVO createReqVO, Long userId) {
        // 1.1 获得 AiChatRoleDO 聊天角色
        AiChatRole role = createReqVO.getRoleId() != null ? chatRoleService.validateChatRole(createReqVO.getRoleId()) : null;
        // 1.2 获得 AiModelDO 聊天模型
        AiModel model = role != null && role.getModelId() != null ? modelService.validateModel(role.getModelId())
                : modelService.getRequiredDefaultModel(AiModelTypeEnum.CHAT.getType());
        Assert.notNull(model, "必须找到默认模型");
        validateChatModel(model);

        // 1.3 校验知识库

        // 2. 创建 AiChatConversation 聊天对话
        AiChatConversation conversation = new AiChatConversation();
        conversation.setUserId(userId);
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
            model = modelService.validateModel(updateReqVO.getModelId());
        }
        // 1.3 校验知识库是否存在

        // 2. 更新对话信息
        AiChatConversation updateObj = BeanUtil.toBean(updateReqVO, AiChatConversation.class);
        if (Boolean.TRUE.equals(updateReqVO.getPinned())) {
            updateObj.setPinnedTime(DateUtils.getNowDate());
        }
        if (model != null) {
            updateObj.setModel(model.getModel());
        }
        return chatConversationMapper.updateById(updateObj);
    }

    /**
     * 获得我的聊天对话列表
     *
     * @param userId 用户ID
     * @return 聊天对话列表
     */
    @Override
    public List<AiChatConversationRespVO> getChatConversationListByUserId(Long userId) {
        List<AiChatConversation> conversationList = chatConversationMapper.selectListByUserId(userId);
        return convertList(conversationList, u -> BeanUtil.toBean(u, AiChatConversationRespVO.class));
    }

    /**
     * 获得我的聊天对话
     *
     * @param id 对话ID
     * @return 聊天对话
     */
    @Override
    public AiChatConversation getChatConversationById(Long id) {
        return chatConversationMapper.selectById(id);
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
        return chatConversationMapper.deleteById(id);
    }

    /**
     * 删除我的未置顶聊天对话
     *
     * @param userId 用户ID
     * @return 是否成功
     */
    @Override
    public int deleteChatConversationMyByUserId(Long userId) {
        List<AiChatConversation> list = chatConversationMapper.selectListByUserIdAndPinned(userId, false);
        if (CollUtil.isEmpty(list)) {
            return 0;
        }
        return chatConversationMapper.deleteByIds(convertList(list, AiChatConversation::getId));
    }

    /**
     * 获取对话分页列表
     *
     * @param pageQuery 分页查询对象
     * @param pageReqVO 查询参数
     * @return 分页列表
     */
    @Override
    public TableDataInfo<AiChatConversationRespVO> getChatConversationPage(PageQuery pageQuery, AiChatConversationPageReqVO pageReqVO) {
        IPage<AiChatConversation> page = chatConversationMapper.selectPage(pageQuery.build(), pageReqVO);
        Map<Long, Integer> countMap = chatMessageService.getChatMessageCountMap(convertList(page.getRecords(), AiChatConversation::getId));
        // 转换为响应VO,并添加消息数量
        List<AiChatConversationRespVO> list = convertList(page.getRecords(), conversation -> {
            AiChatConversationRespVO respVO = BeanUtil.toBean(conversation, AiChatConversationRespVO.class);
            respVO.setMessageCount(countMap.get(conversation.getId()));
            return respVO;
        });
        return TableDataInfo.build(list, page.getTotal());
    }

    /**
     * 管理员删除对话
     *
     * @param id 对话ID
     * @return 是否成功
     */
    @Override
    public int deleteChatConversationById(Long id) {
        // 1. 校验对话是否存在
        AiChatConversation conversation = validateChatConversationExists(id);
        if (conversation == null) {
            throw new ServiceException(AiErrorConstants.CHAT_CONVERSATION_NOT_EXISTS);
        }
        // 2. 删除对话
        return chatConversationMapper.deleteById(id);
    }

    /**
     * 校验对话是否存在
     *
     * @param id 对话ID
     * @return 对话
     */
    @Override
    public AiChatConversation validateChatConversationExists(Long id) {
        AiChatConversation conversation = chatConversationMapper.selectById(id);
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