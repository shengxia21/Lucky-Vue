package com.lucky.system.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.common.core.mybatis.BaseMapperX;
import com.lucky.system.domain.SysUserRole;

import java.util.Arrays;

/**
 * 用户与角色关联表 数据层
 *
 * @author lucky
 */
public interface SysUserRoleMapper extends BaseMapperX<SysUserRole, SysUserRole> {

    default int deleteByUserId(Long userId) {
        return delete(Wrappers.<SysUserRole>lambdaQuery()
                .eq(SysUserRole::getUserId, userId));
    }

    default int deleteByUserIds(Long[] userIds) {
        return delete(Wrappers.<SysUserRole>lambdaQuery()
                .in(SysUserRole::getUserId, Arrays.asList(userIds)));
    }

    default Long countByRoleId(Long roleId) {
        return selectCount(Wrappers.<SysUserRole>lambdaQuery()
                .eq(SysUserRole::getRoleId, roleId));
    }

    default int deleteUserRoleInfo(SysUserRole userRole) {
        return delete(Wrappers.<SysUserRole>lambdaQuery()
                .eq(SysUserRole::getUserId, userRole.getUserId())
                .eq(SysUserRole::getRoleId, userRole.getRoleId()));
    }

    default int deleteUserRoleInfos(Long roleId, Long[] userIds) {
        return delete(Wrappers.<SysUserRole>lambdaQuery()
                .eq(SysUserRole::getRoleId, roleId)
                .in(SysUserRole::getUserId, Arrays.asList(userIds)));
    }

}
