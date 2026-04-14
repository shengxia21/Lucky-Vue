package com.lucky.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.common.core.domain.entity.SysRole;
import com.lucky.common.core.mybatis.BaseMapperX;
import com.lucky.system.domain.query.role.SysRoleQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色表 数据层
 *
 * @author lucky
 */
public interface SysRoleMapper extends BaseMapperX<SysRole, SysRole> {

    /**
     * 根据条件分页查询角色数据
     *
     * @param page  角色信息
     * @param query 查询参数
     * @return 角色数据集合信息
     */
    IPage<SysRole> selectRoleList(IPage<SysRole> page, @Param("query") SysRoleQuery query);

    /**
     * 根据条件查询角色数据
     *
     * @param query 查询参数
     * @return 角色数据集合信息
     */
    List<SysRole> selectRoleList(@Param("query") SysRoleQuery query);

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRolePermissionByUserId(Long userId);

    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    List<Long> selectRoleListByUserId(Long userId);

    /**
     * 根据用户ID查询角色
     *
     * @param userName 用户名
     * @return 角色列表
     */
    List<SysRole> selectRolesByUserName(String userName);

    default SysRole checkRoleNameUnique(String roleName) {
        return selectOne(Wrappers.<SysRole>lambdaQuery()
                .eq(SysRole::getRoleName, roleName));
    }

    default SysRole checkRoleKeyUnique(String roleKey) {
        return selectOne(Wrappers.<SysRole>lambdaQuery()
                .eq(SysRole::getRoleKey, roleKey));
    }

}
