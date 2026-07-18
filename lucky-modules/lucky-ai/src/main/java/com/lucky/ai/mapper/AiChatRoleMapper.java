package com.lucky.ai.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.ai.domain.AiChatRole;
import com.lucky.ai.domain.query.chatRole.AiChatRoleMyQuery;
import com.lucky.ai.domain.query.chatRole.AiChatRoleQuery;
import com.lucky.ai.domain.vo.chatRole.AiChatRoleVO;
import com.lucky.common.ai.enums.AiStatusEnum;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.mybatis.core.mapper.BaseMapperX;
import com.lucky.common.security.utils.SecurityUtils;

/**
 * AI 聊天角色Mapper接口
 *
 * @author lucky
 */
public interface AiChatRoleMapper extends BaseMapperX<AiChatRole, AiChatRoleVO> {

    default IPage<AiChatRoleVO> selectMyPage(IPage<AiChatRole> page, AiChatRoleMyQuery query) {
        LambdaQueryWrapper<AiChatRole> wrapper = Wrappers.<AiChatRole>lambdaQuery()
                .like(StringUtils.isNotEmpty(query.getName()), AiChatRole::getName, query.getName())
                // 公开 查全部用户
                .eq(Boolean.TRUE.equals(query.getPublicStatus()), AiChatRole::getPublicStatus, query.getPublicStatus())
                // 私有 查当前用户
                .eq(Boolean.FALSE.equals(query.getPublicStatus()), AiChatRole::getUserId, SecurityUtils.getUserId())
                // 只返回已启用的聊天角色
                .eq(AiChatRole::getStatus, AiStatusEnum.ENABLE.getStatus())
                .orderByAsc(AiChatRole::getSort);
        return selectVoPage(page, wrapper);
    }

    default AiChatRoleVO selectMyById(Long id) {
        LambdaQueryWrapper<AiChatRole> wrapper = Wrappers.<AiChatRole>lambdaQuery()
                .eq(AiChatRole::getId, id)
                .eq(AiChatRole::getUserId, SecurityUtils.getUserId());
        return selectVoOne(wrapper, false);
    }

    default int updateMyById(AiChatRole entity) {
        LambdaUpdateWrapper<AiChatRole> wrapper = Wrappers.<AiChatRole>lambdaUpdate()
                .eq(AiChatRole::getId, entity.getId())
                .eq(AiChatRole::getUserId, SecurityUtils.getUserId());
        return update(entity, wrapper);
    }

    default int deleteMyById(Long id) {
        LambdaQueryWrapper<AiChatRole> wrapper = Wrappers.<AiChatRole>lambdaQuery()
                .eq(AiChatRole::getId, id)
                .eq(AiChatRole::getUserId, SecurityUtils.getUserId());
        return delete(wrapper);
    }

    default IPage<AiChatRoleVO> selectPage(IPage<AiChatRole> page, AiChatRoleQuery query) {
        LambdaQueryWrapper<AiChatRole> wrapper = Wrappers.<AiChatRole>lambdaQuery()
                .like(StringUtils.isNotEmpty(query.getName()), AiChatRole::getName, query.getName())
                .eq(StringUtils.isNotNull(query.getStatus()), AiChatRole::getStatus, query.getStatus())
                .eq(StringUtils.isNotNull(query.getPublicStatus()), AiChatRole::getPublicStatus, query.getPublicStatus())
                .orderByAsc(AiChatRole::getSort);
        return selectVoPage(page, wrapper);
    }

}