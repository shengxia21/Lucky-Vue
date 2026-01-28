package com.lucky.ai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.lucky.ai.controller.model.vo.chatRole.AiChatRolePageReqVO;
import com.lucky.ai.controller.model.vo.chatRole.AiChatRoleRespVO;
import com.lucky.ai.controller.model.vo.chatRole.AiChatRoleSaveMyReqVO;
import com.lucky.ai.controller.model.vo.chatRole.AiChatRoleSaveReqVO;
import com.lucky.ai.domain.AiChatRole;
import com.lucky.ai.enums.CommonStatusEnum;
import com.lucky.ai.mapper.AiChatRoleMapper;
import com.lucky.ai.service.IAiChatRoleService;
import com.lucky.ai.util.CollectionUtils;
import com.lucky.common.constant.AiErrorConstants;
import com.lucky.common.exception.ServiceException;
import com.lucky.common.utils.DateUtils;
import com.lucky.common.utils.SecurityUtils;
import com.lucky.common.utils.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * AI 聊天角色 Service 实现类
 *
 * @author lucky
 */
@Service
public class AiChatRoleServiceImpl implements IAiChatRoleService {

    @Resource
    private AiChatRoleMapper aiChatRoleMapper;

    /**
     * 创建聊天角色
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    @Override
    public Long createChatRole(AiChatRoleSaveReqVO createReqVO) {
        // 校验文档
        // 校验工具

        // 保存角色
        AiChatRole chatRole = BeanUtil.toBean(createReqVO, AiChatRole.class);
        chatRole.setCreateBy(SecurityUtils.getUsername());
        chatRole.setCreateTime(DateUtils.getNowDate());
        aiChatRoleMapper.insertAiChatRole(chatRole);
        return chatRole.getId();
    }

    /**
     * 创建【我的】聊天角色
     *
     * @param createReqVO 创建信息
     * @param userId      用户编号
     * @return 编号
     */
    @Override
    public Long createChatRoleMy(AiChatRoleSaveMyReqVO createReqVO, Long userId) {
        // 校验文档
        // 校验工具

        // 保存角色
        AiChatRole chatRole = BeanUtil.toBean(createReqVO, AiChatRole.class);
        chatRole.setUserId(userId);
        chatRole.setStatus(CommonStatusEnum.ENABLE.getStatus());
        chatRole.setPublicStatus(false);
        chatRole.setCreateBy(SecurityUtils.getUsername());
        chatRole.setCreateTime(DateUtils.getNowDate());
        aiChatRoleMapper.insertAiChatRole(chatRole);
        return chatRole.getId();
    }

    /**
     * 更新聊天角色
     *
     * @param updateReqVO 更新信息
     */
    @Override
    public int updateChatRole(AiChatRoleSaveReqVO updateReqVO) {
        // 校验存在
        validateChatRoleExists(updateReqVO.getId());
        // 校验文档
        // 校验工具

        // 更新角色
        AiChatRole updateObj = BeanUtil.toBean(updateReqVO, AiChatRole.class);
        updateObj.setUpdateBy(SecurityUtils.getUsername());
        updateObj.setUpdateTime(DateUtils.getNowDate());
        return aiChatRoleMapper.updateAiChatRoleById(updateObj);
    }

    /**
     * 更新【我的】聊天角色
     *
     * @param updateReqVO 更新信息
     * @param userId      用户编号
     */
    @Override
    public int updateChatRoleMy(AiChatRoleSaveMyReqVO updateReqVO, Long userId) {
        // 校验存在
        AiChatRole chatRole = validateChatRoleExists(updateReqVO.getId());
        if (ObjectUtil.notEqual(chatRole.getUserId(), userId)) {
            throw new ServiceException(AiErrorConstants.CHAT_ROLE_NOT_EXISTS);
        }
        // 校验文档
        // 校验工具

        // 更新角色
        AiChatRole updateObj = BeanUtil.toBean(updateReqVO, AiChatRole.class);
        updateObj.setUpdateBy(SecurityUtils.getUsername());
        updateObj.setUpdateTime(DateUtils.getNowDate());
        return aiChatRoleMapper.updateAiChatRoleById(updateObj);
    }

    /**
     * 删除聊天角色
     *
     * @param id 编号
     */
    @Override
    public int deleteChatRole(Long id) {
        // 校验存在
        validateChatRoleExists(id);
        // 删除
        return aiChatRoleMapper.deleteAiChatRoleById(id);
    }

    /**
     * 删除【我的】聊天角色
     *
     * @param id     编号
     * @param userId 用户编号
     */
    @Override
    public int deleteChatRoleMy(Long id, Long userId) {
        // 校验存在
        AiChatRole chatRole = validateChatRoleExists(id);
        if (ObjectUtil.notEqual(chatRole.getUserId(), userId)) {
            throw new ServiceException(AiErrorConstants.CHAT_ROLE_NOT_EXISTS);
        }
        // 删除
        return aiChatRoleMapper.deleteAiChatRoleById(id);
    }

    /**
     * 获得聊天角色
     *
     * @param id 编号
     * @return AI 聊天角色
     */
    @Override
    public AiChatRoleRespVO getChatRole(Long id) {
        return aiChatRoleMapper.selectAiChatRoleById(id);
    }

    /**
     * 获得聊天角色列表
     *
     * @param ids 编号数组
     * @return 聊天角色列表
     */
    @Override
    public List<AiChatRole> getChatRoleList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return aiChatRoleMapper.selectByIds(ids);
    }

    /**
     * 校验聊天角色是否合法
     *
     * @param id 角色编号
     */
    @Override
    public AiChatRole validateChatRole(Long id) {
        AiChatRole chatRole = validateChatRoleExists(id);
        if (CommonStatusEnum.isDisable(chatRole.getStatus())) {
            throw new ServiceException(StringUtils.format(AiErrorConstants.CHAT_ROLE_DISABLE, chatRole.getName()));
        }
        return chatRole;
    }

    /**
     * 获得聊天角色分页
     *
     * @param pageReqVO 分页查询
     * @return 聊天角色分页
     */
    @Override
    public List<AiChatRoleRespVO> getChatRolePage(AiChatRolePageReqVO pageReqVO) {
        return aiChatRoleMapper.selectPage(pageReqVO);
    }

    /**
     * 获得【我的】聊天角色分页
     *
     * @param pageReqVO 分页查询
     * @param userId    用户编号
     * @return 聊天角色分页
     */
    @Override
    public List<AiChatRoleRespVO> getChatRoleMyPage(AiChatRolePageReqVO pageReqVO, Long userId) {
        return aiChatRoleMapper.selectPageByMy(pageReqVO, userId);
    }

    /**
     * 获得聊天角色的分类列表
     *
     * @return 分类列表
     */
    @Override
    public List<String> getChatRoleCategoryList() {
        List<String> list = aiChatRoleMapper.selectListGroupByCategory(CommonStatusEnum.ENABLE.getStatus());
        return CollectionUtils.filterList(list, category -> category != null && StrUtil.isNotBlank(category));
    }

    /**
     * 根据名字获得聊天角色
     *
     * @param name 名字
     * @return 聊天角色列表
     */
    @Override
    public List<AiChatRole> getChatRoleListByName(String name) {
        return aiChatRoleMapper.selectListByName(name);
    }

    private AiChatRole validateChatRoleExists(Long id) {
        AiChatRole chatRole = aiChatRoleMapper.selectById(id);
        if (chatRole == null) {
            throw new ServiceException(AiErrorConstants.CHAT_ROLE_NOT_EXISTS);
        }
        return chatRole;
    }

}