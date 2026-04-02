package com.lucky.ai.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.ai.domain.AiChatRole;
import com.lucky.ai.domain.query.chatRole.ChatRolePageQuery;
import com.lucky.ai.domain.query.chatRole.ChatRoleSaveMyQuery;
import com.lucky.ai.domain.query.chatRole.ChatRoleSaveQuery;
import com.lucky.ai.domain.vo.chatRole.ChatRoleVO;
import com.lucky.ai.enums.CommonStatusEnum;
import com.lucky.ai.mapper.AiChatRoleMapper;
import com.lucky.ai.service.AiChatRoleService;
import com.lucky.common.constant.AiErrorConstants;
import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.common.exception.ServiceException;
import com.lucky.common.utils.MapstructUtils;
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
public class AiChatRoleServiceImpl implements AiChatRoleService {

    @Resource
    private AiChatRoleMapper chatRoleMapper;

    /**
     * 创建聊天角色
     *
     * @param query 创建信息
     * @return 编号
     */
    @Override
    public Long createChatRole(ChatRoleSaveQuery query) {
        // 校验文档
        // 校验工具

        // 保存角色
        AiChatRole chatRole = MapstructUtils.convert(query, AiChatRole.class);
        chatRoleMapper.insert(chatRole);
        return chatRole.getId();
    }

    /**
     * 创建【我的】聊天角色
     *
     * @param query  创建信息
     * @param userId 用户编号
     * @return 编号
     */
    @Override
    public Long createChatRoleMy(ChatRoleSaveMyQuery query, Long userId) {
        // 校验文档
        // 校验工具

        // 保存角色
        AiChatRole chatRole = MapstructUtils.convert(query, AiChatRole.class);
        chatRole.setUserId(userId);
        chatRole.setStatus(CommonStatusEnum.ENABLE.getStatus());
        chatRole.setPublicStatus(false);
        chatRoleMapper.insert(chatRole);
        return chatRole.getId();
    }

    /**
     * 更新聊天角色
     *
     * @param query 更新信息
     */
    @Override
    public int updateChatRole(ChatRoleSaveQuery query) {
        // 校验存在
        validateChatRoleExists(query.getId());
        // 校验文档
        // 校验工具

        // 更新角色
        AiChatRole chatRole = MapstructUtils.convert(query, AiChatRole.class);
        return chatRoleMapper.updateById(chatRole);
    }

    /**
     * 更新【我的】聊天角色
     *
     * @param query  更新信息
     * @param userId 用户编号
     */
    @Override
    public int updateChatRoleMy(ChatRoleSaveMyQuery query, Long userId) {
        // 校验存在
        AiChatRole chatRole = validateChatRoleExists(query.getId());
        if (ObjectUtil.notEqual(chatRole.getUserId(), userId)) {
            throw new ServiceException(AiErrorConstants.CHAT_ROLE_NOT_EXISTS);
        }
        // 校验文档
        // 校验工具

        // 更新角色
        AiChatRole updateObj = MapstructUtils.convert(query, AiChatRole.class);
        return chatRoleMapper.updateById(updateObj);
    }

    /**
     * 删除聊天角色
     *
     * @param id 编号
     */
    @Override
    public int deleteChatRoleById(Long id) {
        // 校验存在
        validateChatRoleExists(id);
        // 删除
        return chatRoleMapper.deleteById(id);
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
        return chatRoleMapper.deleteById(id);
    }

    /**
     * 获得聊天角色
     *
     * @param id 编号
     * @return AI 聊天角色
     */
    @Override
    public ChatRoleVO getChatRoleById(Long id) {
        return chatRoleMapper.selectVoById(id);
    }

    /**
     * 获得聊天角色列表
     *
     * @param ids 编号数组
     * @return 聊天角色列表
     */
    @Override
    public List<ChatRoleVO> getChatRoleList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return chatRoleMapper.selectVoByIds(ids);
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
     * @param pageQuery 分页查询
     * @param query     分页查询
     * @return 聊天角色分页
     */
    @Override
    public TableDataInfo<ChatRoleVO> getChatRolePage(PageQuery pageQuery, ChatRolePageQuery query) {
        IPage<ChatRoleVO> page = chatRoleMapper.selectPage(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    /**
     * 获得【我的】聊天角色分页
     *
     * @param pageQuery 分页查询
     * @param query     分页查询
     * @param userId    用户编号
     * @return 聊天角色分页
     */
    @Override
    public TableDataInfo<ChatRoleVO> getChatRoleMyPage(PageQuery pageQuery, ChatRolePageQuery query, Long userId) {
        IPage<ChatRoleVO> page = chatRoleMapper.selectMyPage(pageQuery.build(), query, userId);
        return TableDataInfo.build(page);
    }

    /**
     * 获得聊天角色的分类列表
     *
     * @return 分类列表
     */
    @Override
    public List<String> getChatRoleCategoryList() {
        List<String> list = chatRoleMapper.selectListGroupByCategory(CommonStatusEnum.ENABLE.getStatus());
        return list.stream().filter(StrUtil::isNotBlank).toList();
    }

    /**
     * 根据名字获得聊天角色
     *
     * @param name 名字
     * @return 聊天角色列表
     */
    @Override
    public List<ChatRoleVO> getChatRoleListByName(String name) {
        return chatRoleMapper.selectListByName(name);
    }

    private AiChatRole validateChatRoleExists(Long id) {
        AiChatRole chatRole = chatRoleMapper.selectById(id);
        if (chatRole == null) {
            throw new ServiceException(AiErrorConstants.CHAT_ROLE_NOT_EXISTS);
        }
        return chatRole;
    }

}