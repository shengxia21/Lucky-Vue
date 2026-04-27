package com.lucky.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.common.mybatis.core.mapper.BaseMapperX;
import com.lucky.system.domain.SysUser;
import com.lucky.system.domain.query.user.SysUserQuery;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 用户表 数据层
 *
 * @author lucky
 */
public interface SysUserMapper extends BaseMapperX<SysUser, SysUser> {

    /**
     * 根据条件分页查询用户列表
     *
     * @param page  分页参数
     * @param query 用户信息
     * @return 用户信息集合信息
     */
    IPage<SysUser> selectUserList(IPage<SysUser> page, @Param("query") SysUserQuery query);

    /**
     * 根据条件查询用户列表
     *
     * @param query 用户信息
     * @return 用户信息集合信息
     */
    List<SysUser> selectUserList(@Param("query") SysUserQuery query);

    /**
     * 根据条件分页查询已配用户角色列表
     *
     * @param page  分页参数
     * @param query 查询参数
     * @return 用户信息集合信息
     */
    IPage<SysUser> selectAllocatedList(IPage<SysUser> page, @Param("query") SysUserQuery query);

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param page  分页参数
     * @param query 查询参数
     * @return 用户信息集合信息
     */
    IPage<SysUser> selectUnallocatedList(IPage<SysUser> page, @Param("query") SysUserQuery query);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    SysUser selectUserByUserName(String userName);

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    SysUser selectUserById(Long userId);

    default List<SysUser> selectUserAll() {
        return selectList(Wrappers.<SysUser>lambdaQuery()
                .select(SysUser::getUserId, SysUser::getNickName, SysUser::getUserName));
    }

    default int updateUserStatus(Long userId, String status) {
        return update(Wrappers.<SysUser>lambdaUpdate()
                .set(SysUser::getStatus, status)
                .eq(SysUser::getUserId, userId));
    }

    default int updateUserAvatar(Long userId, String avatar) {
        return update(Wrappers.<SysUser>lambdaUpdate()
                .set(SysUser::getAvatar, avatar)
                .eq(SysUser::getUserId, userId));
    }

    default int updateLoginInfo(Long userId, String loginIp, Date loginDate) {
        return update(Wrappers.<SysUser>lambdaUpdate()
                .set(SysUser::getLoginIp, loginIp)
                .set(SysUser::getLoginDate, loginDate)
                .eq(SysUser::getUserId, userId));
    }

    default int resetUserPwd(Long userId, String password) {
        return update(Wrappers.<SysUser>lambdaUpdate()
                .set(SysUser::getPwdUpdateDate, new Date())
                .set(SysUser::getPassword, password)
                .eq(SysUser::getUserId, userId));
    }

    default SysUser checkUserNameUnique(String userName) {
        return selectOne(Wrappers.<SysUser>lambdaQuery()
                .select(SysUser::getUserId, SysUser::getUserName)
                .eq(SysUser::getUserName, userName));
    }

    default SysUser checkPhoneUnique(String phoneNumber) {
        return selectOne(Wrappers.<SysUser>lambdaQuery()
                .select(SysUser::getUserId, SysUser::getPhoneNumber)
                .eq(SysUser::getPhoneNumber, phoneNumber));
    }

    default SysUser checkEmailUnique(String email) {
        return selectOne(Wrappers.<SysUser>lambdaQuery()
                .select(SysUser::getUserId, SysUser::getEmail)
                .eq(SysUser::getEmail, email));
    }

    default Long checkDeptExistUser(Long deptId) {
        return selectCount(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getDeptId, deptId));
    }

}