package com.lucky.common.security.core.service;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.lucky.common.core.domain.model.LoginUser;
import com.lucky.common.core.exception.ServiceException;
import com.lucky.common.core.service.PermissionService;
import com.lucky.common.core.utils.spring.SpringUtils;
import com.lucky.common.security.utils.SecurityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Sa-Token 权限管理实现类
 *
 * @author lucky
 */
public class SaTokenPermissionImpl implements StpInterface {

    /**
     * 获取菜单权限列表
     */
    @Override
    public List<String> getPermissionList(Object userId, String loginType) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (CollUtil.isNotEmpty(loginUser.getMenuPermission())) {
            // 返回用户菜单权限，避免每次查询数据库
            return new ArrayList<>(loginUser.getMenuPermission());
        } else {
            // 从数据库查询用户菜单权限
            PermissionService permissionService = getPermissionService();
            if (ObjectUtil.isNotNull(permissionService)) {
                Set<String> permissionList = permissionService.getMenuPermission(Long.valueOf(userId.toString()));
                return new ArrayList<>(permissionList);
            } else {
                throw new ServiceException("PermissionService 实现类不存在");
            }
        }
    }

    /**
     * 获取角色权限列表
     */
    @Override
    public List<String> getRoleList(Object userId, String loginType) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (CollUtil.isNotEmpty(loginUser.getRolePermission())) {
            // 返回用户角色权限，避免每次查询数据库
            return new ArrayList<>(loginUser.getRolePermission());
        } else {
            // 从数据库查询用户角色权限
            PermissionService permissionService = getPermissionService();
            if (ObjectUtil.isNotNull(permissionService)) {
                Set<String> permissionList = permissionService.getRolePermission(Long.valueOf(userId.toString()));
                return new ArrayList<>(permissionList);
            } else {
                throw new ServiceException("PermissionService 实现类不存在");
            }
        }
    }

    private PermissionService getPermissionService() {
        try {
            return SpringUtils.getBean(PermissionService.class);
        } catch (Exception e) {
            return null;
        }
    }

}
