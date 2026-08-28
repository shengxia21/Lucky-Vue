package com.lucky.ai.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.ai.domain.AiChatRole;
import com.lucky.ai.domain.query.chatRole.AiChatRoleMyQuery;
import com.lucky.ai.domain.query.chatRole.AiChatRoleQuery;
import com.lucky.ai.domain.query.chatRole.AiChatRoleSaveMyQuery;
import com.lucky.ai.domain.query.chatRole.AiChatRoleSaveQuery;
import com.lucky.ai.domain.vo.chatRole.AiChatRoleVO;
import com.lucky.ai.mapper.AiChatRoleMapper;
import com.lucky.ai.service.IAiChatRoleService;
import com.lucky.common.core.constant.AiConstants;
import com.lucky.common.core.enums.DataStatus;
import com.lucky.common.core.enums.YesNo;
import com.lucky.common.core.exception.ServiceException;
import com.lucky.common.core.utils.MapstructUtils;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import com.lucky.common.security.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;

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
    public TableDataInfo<AiChatRoleVO> selectMyChatRoleList(PageQuery pageQuery, AiChatRoleMyQuery query) {
        IPage<AiChatRoleVO> page = chatRoleMapper.selectMyPage(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    @Override
    public AiChatRoleVO selectMyChatRoleById(Long id) {
        return chatRoleMapper.selectMyById(id);
    }

    @Override
    public int insertMyChatRole(AiChatRoleSaveMyQuery query) {
        AiChatRole chatRole = MapstructUtils.convert(query, AiChatRole.class);
        chatRole.setUserId(SecurityUtils.getUserId());
        chatRole.setStatus(DataStatus.OK.getCode());
        chatRole.setIsPublic(YesNo.NO.getCode());
        return chatRoleMapper.insert(chatRole);
    }

    @Override
    public int updateMyChatRole(AiChatRoleSaveMyQuery query) {
        AiChatRole updateObj = MapstructUtils.convert(query, AiChatRole.class);
        return chatRoleMapper.updateMyById(updateObj);
    }

    @Override
    public int deleteMyChatRoleById(Long id) {
        return chatRoleMapper.deleteMyById(id);
    }

    @Override
    public TableDataInfo<AiChatRoleVO> selectChatRoleList(PageQuery pageQuery, AiChatRoleQuery query) {
        IPage<AiChatRoleVO> page = chatRoleMapper.selectPage(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    @Override
    public AiChatRoleVO selectChatRoleById(Long id) {
        return chatRoleMapper.selectVoById(id);
    }

    @Override
    public int insertChatRole(AiChatRoleSaveQuery query) {
        AiChatRole chatRole = MapstructUtils.convert(query, AiChatRole.class);
        return chatRoleMapper.insert(chatRole);
    }

    @Override
    public int updateChatRole(AiChatRoleSaveQuery query) {
        AiChatRole chatRole = MapstructUtils.convert(query, AiChatRole.class);
        return chatRoleMapper.updateById(chatRole);
    }

    @Override
    public int deleteChatRoleByIds(Long[] ids) {
        return chatRoleMapper.deleteByIds(Arrays.asList(ids));
    }

    @Override
    public AiChatRole validateChatRole(Long id) {
        AiChatRole chatRole = validateChatRoleExists(id);
        if (DataStatus.DISABLE.getCode().equals(chatRole.getStatus())) {
            throw new ServiceException(StringUtils.format(AiConstants.CHAT_ROLE_DISABLE, chatRole.getName()));
        }
        return chatRole;
    }

    private AiChatRole validateChatRoleExists(Long id) {
        AiChatRole chatRole = chatRoleMapper.selectById(id);
        if (chatRole == null) {
            throw new ServiceException(AiConstants.CHAT_ROLE_NOT_EXISTS);
        }
        return chatRole;
    }

}