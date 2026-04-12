package com.lucky.system.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.common.core.mybatis.BaseMapperX;
import com.lucky.system.domain.SysRoleDept;

import java.util.Arrays;

/**
 * 角色与部门关联表 数据层
 *
 * @author lucky
 */
public interface SysRoleDeptMapper extends BaseMapperX<SysRoleDept, SysRoleDept> {

    default int deleteByRoleId(Long roleId) {
        return delete(Wrappers.<SysRoleDept>lambdaQuery()
                .eq(SysRoleDept::getRoleId, roleId));
    }

    default int deleteByRoleIds(Long[] roleIds) {
        return delete(Wrappers.<SysRoleDept>lambdaQuery()
                .in(SysRoleDept::getRoleId, Arrays.asList(roleIds)));
    }

}
