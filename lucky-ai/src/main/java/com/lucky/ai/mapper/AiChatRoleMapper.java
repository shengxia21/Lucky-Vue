package com.lucky.ai.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.ai.controller.model.vo.chatRole.AiChatRolePageReqVO;
import com.lucky.ai.domain.AiChatRole;
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

    default IPage<AiChatRole> selectPage(IPage<AiChatRole> page, AiChatRolePageReqVO pageReqVO) {
        LambdaQueryWrapper<AiChatRole> wrapper = Wrappers.<AiChatRole>lambdaQuery()
                .like(StringUtils.isNotEmpty(pageReqVO.getName()), AiChatRole::getName, pageReqVO.getName())
                .eq(StringUtils.isNotEmpty(pageReqVO.getCategory()), AiChatRole::getCategory, pageReqVO.getCategory())
                .eq(StringUtils.isNotNull(pageReqVO.getPublicStatus()), AiChatRole::getStatus, pageReqVO.getPublicStatus())
                .orderByAsc(AiChatRole::getSort);
        return selectPage(page, wrapper);
    }

    default IPage<AiChatRole> selectMyPage(IPage<AiChatRole> page, AiChatRolePageReqVO pageReqVO, Long userId) {
        LambdaQueryWrapper<AiChatRole> wrapper = Wrappers.<AiChatRole>lambdaQuery()
                .like(StringUtils.isNotEmpty(pageReqVO.getName()), AiChatRole::getName, pageReqVO.getName())
                .eq(StringUtils.isNotEmpty(pageReqVO.getCategory()), AiChatRole::getCategory, pageReqVO.getCategory())
                // 情况一：公开
                .eq(Boolean.TRUE.equals(pageReqVO.getPublicStatus()), AiChatRole::getPublicStatus, pageReqVO.getPublicStatus())
                // 情况二：私有
                .eq(Boolean.FALSE.equals(pageReqVO.getPublicStatus()), AiChatRole::getUserId, userId)
                .eq(Boolean.FALSE.equals(pageReqVO.getPublicStatus()), AiChatRole::getStatus, CommonStatusEnum.ENABLE.getStatus())
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