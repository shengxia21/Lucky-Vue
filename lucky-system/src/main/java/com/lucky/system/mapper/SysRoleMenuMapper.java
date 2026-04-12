package com.lucky.system.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.common.core.mybatis.BaseMapperX;
import com.lucky.system.domain.SysRoleMenu;

import java.util.Arrays;

/**
 * 角色与菜单关联表 数据层
 *
 * @author lucky
 */
public interface SysRoleMenuMapper extends BaseMapperX<SysRoleMenu, SysRoleMenu> {

    default Long checkMenuExistRole(Long menuId) {
        return selectCount(Wrappers.<SysRoleMenu>lambdaQuery()
                .eq(SysRoleMenu::getMenuId, menuId));
    }

    default int deleteByRoleId(Long roleId) {
        return delete(Wrappers.<SysRoleMenu>lambdaQuery()
                .eq(SysRoleMenu::getRoleId, roleId));
    }

    default int deleteByRoleIds(Long[] roleIds) {
        return delete(Wrappers.<SysRoleMenu>lambdaQuery()
                .in(SysRoleMenu::getRoleId, Arrays.asList(roleIds)));
    }

}
