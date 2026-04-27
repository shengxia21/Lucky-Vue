package com.lucky.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.common.core.constant.UserConstants;
import com.lucky.common.core.exception.ServiceException;
import com.lucky.common.core.utils.MapstructUtils;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.core.utils.spring.SpringUtils;
import com.lucky.common.mybatis.annotation.DataScope;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import com.lucky.common.security.utils.SecurityUtils;
import com.lucky.system.domain.SysRole;
import com.lucky.system.domain.SysRoleDept;
import com.lucky.system.domain.SysRoleMenu;
import com.lucky.system.domain.SysUserRole;
import com.lucky.system.domain.query.role.SysRoleQuery;
import com.lucky.system.domain.query.role.SysRoleSaveQuery;
import com.lucky.system.mapper.SysRoleDeptMapper;
import com.lucky.system.mapper.SysRoleMapper;
import com.lucky.system.mapper.SysRoleMenuMapper;
import com.lucky.system.mapper.SysUserRoleMapper;
import com.lucky.system.service.ISysRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 角色 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService {

    @Resource
    private SysRoleMapper roleMapper;

    @Resource
    private SysRoleMenuMapper roleMenuMapper;
    @Resource
    private SysUserRoleMapper userRoleMapper;
    @Resource
    private SysRoleDeptMapper roleDeptMapper;

    @Override
    @DataScope(deptAlias = "d")
    public TableDataInfo<SysRole> selectRoleList(PageQuery pageQuery, SysRoleQuery query) {
        IPage<SysRole> page = roleMapper.selectRoleList(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    @Override
    @DataScope(deptAlias = "d")
    public List<SysRole> selectRoleList(SysRoleQuery query) {
        return roleMapper.selectRoleList(query);
    }

    @Override
    public List<SysRole> selectRolesByUserId(Long userId) {
        List<SysRole> userRoles = roleMapper.selectRolePermissionByUserId(userId);
        List<SysRole> roles = selectRoleAll();
        for (SysRole role : roles) {
            for (SysRole userRole : userRoles) {
                if (role.getRoleId().longValue() == userRole.getRoleId().longValue()) {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }

    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<SysRole> perms = roleMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            if (StringUtils.isNotNull(perm)) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    @Override
    public List<SysRole> selectRoleAll() {
        return SpringUtils.getAopProxy(this).selectRoleList(new SysRoleQuery());
    }

    @Override
    public List<Long> selectRoleListByUserId(Long userId) {
        return roleMapper.selectRoleListByUserId(userId);
    }

    @Override
    public SysRole selectRoleById(Long roleId) {
        return roleMapper.selectById(roleId);
    }

    @Override
    public boolean checkRoleNameUnique(Long roleId, String roleName) {
        long newRoleId = StringUtils.isNull(roleId) ? -1L : roleId;
        SysRole info = roleMapper.checkRoleNameUnique(roleName);
        if (StringUtils.isNotNull(info) && info.getRoleId() != newRoleId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean checkRoleKeyUnique(Long roleId, String roleKey) {
        long newRoleId = StringUtils.isNull(roleId) ? -1L : roleId;
        SysRole info = roleMapper.checkRoleKeyUnique(roleKey);
        if (StringUtils.isNotNull(info) && info.getRoleId() != newRoleId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public void checkRoleAllowed(SysRoleSaveQuery role) {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员角色");
        }
    }

    @Override
    public void checkRoleDataScope(Long... roleIds) {
        if (!SecurityUtils.isAdmin()) {
            for (Long roleId : roleIds) {
                SysRoleQuery query = new SysRoleQuery();
                query.setRoleId(roleId);
                List<SysRole> roles = SpringUtils.getAopProxy(this).selectRoleList(query);
                if (StringUtils.isEmpty(roles)) {
                    throw new ServiceException("没有权限访问角色数据！");
                }
            }
        }
    }

    @Override
    public Long countUserRoleByRoleId(Long roleId) {
        return userRoleMapper.countByRoleId(roleId);
    }

    @Override
    @Transactional
    public boolean insertRole(SysRoleSaveQuery role) {
        SysRole sysRole = MapstructUtils.convert(role, SysRole.class);
        // 新增角色信息
        roleMapper.insert(sysRole);
        return insertRoleMenu(role);
    }

    @Override
    @Transactional
    public boolean updateRole(SysRoleSaveQuery role) {
        SysRole sysRole = MapstructUtils.convert(role, SysRole.class);
        // 修改角色信息
        roleMapper.updateById(sysRole);
        // 删除角色与菜单关联
        roleMenuMapper.deleteByRoleId(role.getRoleId());
        return insertRoleMenu(role);
    }

    @Override
    public int updateRoleStatus(SysRoleSaveQuery role) {
        SysRole sysRole = MapstructUtils.convert(role, SysRole.class);
        return roleMapper.updateById(sysRole);
    }

    @Override
    @Transactional
    public boolean authDataScope(SysRoleSaveQuery role) {
        SysRole sysRole = MapstructUtils.convert(role, SysRole.class);
        // 修改角色信息
        roleMapper.updateById(sysRole);
        // 删除角色与部门关联
        roleDeptMapper.deleteById(role.getRoleId());
        // 新增角色和部门信息（数据权限）
        return insertRoleDept(role);
    }

    @Override
    @Transactional
    public int deleteRoleById(Long roleId) {
        // 删除角色与菜单关联
        roleMenuMapper.deleteByRoleId(roleId);
        // 删除角色与部门关联
        roleDeptMapper.deleteByRoleId(roleId);
        return roleMapper.deleteById(roleId);
    }

    @Override
    @Transactional
    public int deleteRoleByIds(Long[] roleIds) {
        for (Long roleId : roleIds) {
            checkRoleAllowed(new SysRoleSaveQuery(roleId));
            checkRoleDataScope(roleId);
            SysRole role = selectRoleById(roleId);
            if (countUserRoleByRoleId(roleId) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }
        // 删除角色与菜单关联
        roleMenuMapper.deleteByRoleIds(roleIds);
        // 删除角色与部门关联
        roleDeptMapper.deleteByRoleIds(roleIds);
        return roleMapper.deleteByIds(Arrays.asList(roleIds));
    }

    @Override
    public int deleteAuthUser(SysUserRole userRole) {
        return userRoleMapper.deleteUserRoleInfo(userRole);
    }

    @Override
    public int deleteAuthUsers(Long roleId, Long[] userIds) {
        return userRoleMapper.deleteUserRoleInfos(roleId, userIds);
    }

    @Override
    public boolean insertAuthUsers(Long roleId, Long[] userIds) {
        // 新增用户与角色管理
        List<SysUserRole> list = new ArrayList<>();
        for (Long userId : userIds) {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        return userRoleMapper.insertBatch(list);
    }

    /**
     * 新增角色菜单信息
     *
     * @param role 角色对象
     */
    public boolean insertRoleMenu(SysRoleSaveQuery role) {
        // 新增用户与角色管理
        List<SysRoleMenu> list = new ArrayList<>();
        for (Long menuId : role.getMenuIds()) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (!list.isEmpty()) {
            return roleMenuMapper.insertBatch(list);
        }
        return false;
    }

    /**
     * 新增角色部门信息(数据权限)
     *
     * @param role 角色对象
     */
    public boolean insertRoleDept(SysRoleSaveQuery role) {
        // 新增角色与部门（数据权限）管理
        List<SysRoleDept> list = new ArrayList<>();
        for (Long deptId : role.getDeptIds()) {
            SysRoleDept rd = new SysRoleDept();
            rd.setRoleId(role.getRoleId());
            rd.setDeptId(deptId);
            list.add(rd);
        }
        if (!list.isEmpty()) {
            return roleDeptMapper.insertBatch(list);
        }
        return false;
    }

}
