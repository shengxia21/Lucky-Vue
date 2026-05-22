package com.lucky.system.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.common.mybatis.core.mapper.BaseMapperX;
import com.lucky.system.domain.SysRoleDept;

import java.util.Arrays;
import java.util.List;

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

    default List<SysRoleDept> getRoleCustom(Long roleId) {
        return selectList(Wrappers.<SysRoleDept>lambdaQuery()
                .select(SysRoleDept::getDeptId)
                .eq(SysRoleDept::getRoleId, roleId));
    }

}
