package com.lucky.ai.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.ai.domain.AiChatRole;
import com.lucky.ai.domain.query.chatRole.ChatRolePageQuery;
import com.lucky.ai.enums.CommonStatusEnum;
import com.lucky.common.utils.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * AI 聊天角色Mapper接口
 *
 * @author lucky
 */
public interface AiChatRoleMapper extends BaseMapper<AiChatRole> {

    default IPage<AiChatRole> selectPage(IPage<AiChatRole> page, ChatRolePageQuery query) {
        LambdaQueryWrapper<AiChatRole> wrapper = Wrappers.<AiChatRole>lambdaQuery()
                .like(StringUtils.isNotEmpty(query.getName()), AiChatRole::getName, query.getName())
                .eq(StringUtils.isNotEmpty(query.getCategory()), AiChatRole::getCategory, query.getCategory())
                .eq(StringUtils.isNotNull(query.getPublicStatus()), AiChatRole::getStatus, query.getPublicStatus())
                .orderByAsc(AiChatRole::getSort);
        return selectPage(page, wrapper);
    }

    default IPage<AiChatRole> selectMyPage(IPage<AiChatRole> page, ChatRolePageQuery query, Long userId) {
        LambdaQueryWrapper<AiChatRole> wrapper = Wrappers.<AiChatRole>lambdaQuery()
                .like(StringUtils.isNotEmpty(query.getName()), AiChatRole::getName, query.getName())
                .eq(StringUtils.isNotEmpty(query.getCategory()), AiChatRole::getCategory, query.getCategory())
                // 情况一：公开
                .eq(Boolean.TRUE.equals(query.getPublicStatus()), AiChatRole::getPublicStatus, query.getPublicStatus())
                // 情况二：私有
                .eq(Boolean.FALSE.equals(query.getPublicStatus()), AiChatRole::getUserId, userId)
                .eq(Boolean.FALSE.equals(query.getPublicStatus()), AiChatRole::getStatus, CommonStatusEnum.ENABLE.getStatus())
                .orderByAsc(AiChatRole::getSort);
        return selectPage(page, wrapper);
    }

    default List<String> selectListGroupByCategory(Integer status) {
        LambdaQueryWrapper<AiChatRole> wrapper = Wrappers.<AiChatRole>lambdaQuery()
                .select(AiChatRole::getCategory)
                .eq(StringUtils.isNotNull(status), AiChatRole::getStatus, status)
                .groupBy(AiChatRole::getCategory);
        return selectObjs(wrapper).stream()
                .map(Objects::toString)
                .toList();
    }

    default List<AiChatRole> selectListByName(String name) {
        LambdaQueryWrapper<AiChatRole> wrapper = Wrappers.<AiChatRole>lambdaQuery()
                .like(StringUtils.isNotEmpty(name), AiChatRole::getName, name)
                .orderByAsc(AiChatRole::getSort);
        return selectList(wrapper);
    }

}