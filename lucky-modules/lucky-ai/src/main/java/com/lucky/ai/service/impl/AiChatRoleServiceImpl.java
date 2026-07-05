package com.lucky.ai.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.ai.domain.AiChatRole;
import com.lucky.ai.domain.query.chatRole.AiChatRolePageQuery;
import com.lucky.ai.domain.query.chatRole.AiChatRoleSaveMyQuery;
import com.lucky.ai.domain.query.chatRole.AiChatRoleSaveQuery;
import com.lucky.ai.domain.vo.chatRole.AiChatRoleVO;
import com.lucky.ai.mapper.AiChatRoleMapper;
import com.lucky.ai.service.IAiChatRoleService;
import com.lucky.common.ai.enums.CommonStatusEnum;
import com.lucky.common.core.constant.AiErrorConstants;
import com.lucky.common.core.exception.ServiceException;
import com.lucky.common.core.utils.MapstructUtils;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import com.lucky.common.security.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
    private AiChatRoleMapper chatRoleMapper;

    @Override
    public int insertChatRole(AiChatRoleSaveQuery query) {
        AiChatRole chatRole = MapstructUtils.convert(query, AiChatRole.class);
        return chatRoleMapper.insert(chatRole);
    }

    @Override
    public int insertMyChatRole(AiChatRoleSaveMyQuery query) {
        AiChatRole chatRole = MapstructUtils.convert(query, AiChatRole.class);
        chatRole.setUserId(SecurityUtils.getUserId());
        chatRole.setStatus(CommonStatusEnum.ENABLE.getStatus());
        chatRole.setPublicStatus(false);
        return chatRoleMapper.insert(chatRole);
    }

    @Override
    public int updateChatRole(AiChatRoleSaveQuery query) {
        // 校验存在
        validateChatRoleExists(query.getId());
        // 更新角色
        AiChatRole chatRole = MapstructUtils.convert(query, AiChatRole.class);
        return chatRoleMapper.updateById(chatRole);
    }

    @Override
    public int updateMyChatRole(AiChatRoleSaveMyQuery query) {
        // 校验存在
        AiChatRole chatRole = validateChatRoleExists(query.getId());
        if (ObjectUtil.notEqual(chatRole.getUserId(), SecurityUtils.getUserId())) {
            throw new ServiceException(AiErrorConstants.CHAT_ROLE_NOT_EXISTS);
        }
        // 更新角色
        AiChatRole updateObj = MapstructUtils.convert(query, AiChatRole.class);
        return chatRoleMapper.updateById(updateObj);
    }

    @Override
    public int deleteChatRoleByIds(Long[] ids) {
        return chatRoleMapper.deleteByIds(Arrays.asList(ids));
    }

    @Override
    public int deleteMyChatRoleById(Long id) {
        // 校验存在
        AiChatRole chatRole = validateChatRoleExists(id);
        if (ObjectUtil.notEqual(chatRole.getUserId(), SecurityUtils.getUserId())) {
            throw new ServiceException(AiErrorConstants.CHAT_ROLE_NOT_EXISTS);
        }
        // 删除
        return chatRoleMapper.deleteById(id);
    }

    @Override
    public AiChatRoleVO selectChatRoleById(Long id) {
        return chatRoleMapper.selectVoById(id);
    }

    @Override
    public List<AiChatRoleVO> selectChatRoleList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return chatRoleMapper.selectVoByIds(ids);
    }

    @Override
    public AiChatRole validateChatRole(Long id) {
        AiChatRole chatRole = validateChatRoleExists(id);
        if (CommonStatusEnum.isDisable(chatRole.getStatus())) {
            throw new ServiceException(StringUtils.format(AiErrorConstants.CHAT_ROLE_DISABLE, chatRole.getName()));
        }
        return chatRole;
    }

    @Override
    public TableDataInfo<AiChatRoleVO> selectChatRoleList(PageQuery pageQuery, AiChatRolePageQuery query) {
        IPage<AiChatRoleVO> page = chatRoleMapper.selectPage(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    @Override
    public TableDataInfo<AiChatRoleVO> selectMyChatRoleList(PageQuery pageQuery, AiChatRolePageQuery query) {
        IPage<AiChatRoleVO> page = chatRoleMapper.selectMyPage(pageQuery.build(), query, SecurityUtils.getUserId());
        return TableDataInfo.build(page);
    }

    @Override
    public List<String> selectChatRoleCategoryList() {
        List<String> list = chatRoleMapper.selectListGroupByCategory(CommonStatusEnum.ENABLE.getStatus());
        return list.stream().filter(StrUtil::isNotBlank).toList();
    }

    @Override
    public List<AiChatRoleVO> selectChatRoleListByName(String name) {
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