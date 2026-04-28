package com.lucky.system.web;

import com.lucky.common.core.constant.Constants;
import com.lucky.common.core.constant.UserConstants;
import com.lucky.common.core.domain.dto.RoleDTO;
import com.lucky.common.core.domain.dto.UserDTO;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.security.utils.SecurityUtils;
import com.lucky.system.service.ISysMenuService;
import com.lucky.system.service.ISysRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户权限处理
 *
 * @author ruoyi
 */
@Component
public class SysPermissionService {

    @Resource
    private ISysRoleService roleService;

    @Resource
    private ISysMenuService menuService;

    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(UserDTO user) {
        Set<String> roles = new HashSet<>();
        // 管理员拥有所有权限
        if (SecurityUtils.isAdmin(user.getUserId())) {
            roles.add(Constants.SUPER_ADMIN);
        } else {
            roles.addAll(roleService.selectRolePermissionByUserId(user.getUserId()));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(UserDTO user) {
        Set<String> perms = new HashSet<>();
        // 管理员拥有所有权限
        if (SecurityUtils.isAdmin(user.getUserId())) {
            perms.add(Constants.ALL_PERMISSION);
        } else {
            List<RoleDTO> roles = user.getRoles();
            if (!CollectionUtils.isEmpty(roles)) {
                // 多角色设置permissions属性，以便数据权限匹配权限
                for (RoleDTO role : roles) {
                    if (StringUtils.equals(role.getStatus(), UserConstants.ROLE_NORMAL) && !SecurityUtils.isAdmin(role.getRoleId())) {
                        Set<String> rolePerms = menuService.selectMenuPermsByRoleId(role.getRoleId());
                        role.setPermissions(rolePerms);
                        perms.addAll(rolePerms);
                    }
                }
            } else {
                perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
            }
        }
        return perms;
    }

}
