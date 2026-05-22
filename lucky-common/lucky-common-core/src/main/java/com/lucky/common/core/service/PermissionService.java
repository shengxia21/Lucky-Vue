package com.lucky.common.core.service;

import java.util.Set;

/**
 * 权限服务接口
 *
 * @author lucky
 */
public interface PermissionService {

    /**
     * 获取用户角色权限
     *
     * @param userId 用户ID
     * @return 角色权限
     */
    Set<String> getRolePermission(Long userId);

    /**
     * 获取用户菜单权限
     *
     * @param userId 用户ID
     * @return 菜单权限
     */
    Set<String> getMenuPermission(Long userId);

}
