package com.lucky.system.service.impl;

import com.lucky.common.core.constant.Constants;
import com.lucky.common.core.service.PermissionService;
import com.lucky.common.security.utils.SecurityUtils;
import com.lucky.system.service.ISysMenuService;
import com.lucky.system.service.ISysRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * 系统权限服务实现类
 *
 * @author lucky
 */
@Service
public class SysPermissionServiceImpl implements PermissionService {

    @Resource
    private ISysRoleService roleService;

    @Resource
    private ISysMenuService menuService;

    @Override
    public Set<String> getRolePermission(Long userId) {
        Set<String> roles = new HashSet<>();
        // 管理员拥有所有权限
        if (SecurityUtils.isAdmin(userId)) {
            roles.add(Constants.SUPER_ADMIN);
        } else {
            roles.addAll(roleService.selectRolePermissionByUserId(userId));
        }
        return roles;
    }

    @Override
    public Set<String> getMenuPermission(Long userId) {
        Set<String> perms = new HashSet<>();
        // 管理员拥有所有权限
        if (SecurityUtils.isAdmin(userId)) {
            perms.add(Constants.ALL_PERMISSION);
        } else {
            perms.addAll(menuService.selectMenuPermsByUserId(userId));
        }
        return perms;
    }

}
