package com.lucky.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.common.core.constant.UserConstants;
import com.lucky.common.core.exception.ServiceException;
import com.lucky.common.core.utils.MapstructUtils;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.core.utils.bean.BeanValidators;
import com.lucky.common.core.utils.spring.SpringUtils;
import com.lucky.common.mybatis.annotation.DataScope;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import com.lucky.common.security.utils.SecurityUtils;
import com.lucky.system.domain.SysRole;
import com.lucky.system.domain.SysUser;
import com.lucky.system.domain.SysUserPost;
import com.lucky.system.domain.SysUserRole;
import com.lucky.system.domain.query.user.SysUserQuery;
import com.lucky.system.domain.query.user.SysUserSaveQuery;
import com.lucky.system.domain.vo.post.SysPostVO;
import com.lucky.system.mapper.*;
import com.lucky.system.service.ISysConfigService;
import com.lucky.system.service.ISysDeptService;
import com.lucky.system.service.ISysUserService;
import jakarta.annotation.Resource;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysUserServiceImpl implements ISysUserService {

    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Resource
    protected Validator validator;
    @Resource
    private SysUserMapper userMapper;
    @Resource
    private SysRoleMapper roleMapper;
    @Resource
    private SysPostMapper postMapper;
    @Resource
    private SysUserRoleMapper userRoleMapper;
    @Resource
    private SysUserPostMapper userPostMapper;
    @Resource
    private ISysConfigService configService;
    @Resource
    private ISysDeptService deptService;

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public TableDataInfo<SysUser> selectUserList(PageQuery pageQuery, SysUserQuery query) {
        IPage<SysUser> page = userMapper.selectUserList(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUserQuery query) {
        return userMapper.selectUserList(query);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public TableDataInfo<SysUser> selectAllocatedList(PageQuery pageQuery, SysUserQuery query) {
        IPage<SysUser> page = userMapper.selectAllocatedList(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public TableDataInfo<SysUser> selectUnallocatedList(PageQuery pageQuery, SysUserQuery query) {
        IPage<SysUser> page = userMapper.selectUnallocatedList(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    @Override
    public SysUser selectUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);
    }

    @Override
    public SysUser selectUserById(Long userId) {
        return userMapper.selectUserById(userId);
    }

    @Override
    public String selectUserRoleGroup(String userName) {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }

    @Override
    public String selectUserPostGroup(String userName) {
        List<SysPostVO> list = postMapper.selectPostsByUserName(userName);
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysPostVO::getPostName).collect(Collectors.joining(","));
    }

    @Override
    public boolean checkUserNameUnique(Long userId, String userName) {
        long newUserId = StringUtils.isNull(userId) ? -1L : userId;
        SysUser info = userMapper.checkUserNameUnique(userName);
        if (StringUtils.isNotNull(info) && info.getUserId() != newUserId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean checkPhoneUnique(Long userId, String phoneNumber) {
        long newUserId = StringUtils.isNull(userId) ? -1L : userId;
        SysUser info = userMapper.checkPhoneUnique(phoneNumber);
        if (StringUtils.isNotNull(info) && info.getUserId() != newUserId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean checkEmailUnique(Long userId, String email) {
        long newUserId = StringUtils.isNull(userId) ? -1L : userId;
        SysUser info = userMapper.checkEmailUnique(email);
        if (StringUtils.isNotNull(info) && info.getUserId() != newUserId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public void checkUserAllowed(Long userId) {
        if (StringUtils.isNotNull(userId) && SecurityUtils.isAdmin(userId)) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    @Override
    public void checkUserDataScope(Long userId) {
        if (!SecurityUtils.isAdmin()) {
            SysUserQuery user = new SysUserQuery();
            user.setUserId(userId);
            List<SysUser> users = SpringUtils.getAopProxy(this).selectUserList(user);
            if (StringUtils.isEmpty(users)) {
                throw new ServiceException("没有权限访问用户数据！");
            }
        }
    }

    @Override
    @Transactional
    public int insertUser(SysUserSaveQuery user) {
        SysUser sysUser = MapstructUtils.convert(user, SysUser.class);
        // 新增用户信息
        int rows = userMapper.insert(sysUser);
        // 新增用户岗位关联
        insertUserPost(user.getUserId(), user.getPostIds());
        // 新增用户与角色管理
        insertUserRole(user.getUserId(), user.getRoleIds());
        return rows;
    }

    @Override
    public boolean registerUser(SysUser user) {
        return userMapper.insert(user) > 0;
    }

    @Override
    @Transactional
    public int updateUser(SysUserSaveQuery user) {
        Long userId = user.getUserId();
        // 删除用户与角色关联
        userRoleMapper.deleteByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(userId, user.getRoleIds());
        // 删除用户与岗位关联
        userPostMapper.deleteByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(userId, user.getPostIds());
        SysUser sysUser = MapstructUtils.convert(user, SysUser.class);
        return userMapper.updateById(sysUser);
    }

    @Override
    @Transactional
    public void insertUserAuth(Long userId, Long[] roleIds) {
        userRoleMapper.deleteByUserId(userId);
        insertUserRole(userId, roleIds);
    }

    @Override
    public int updateUserStatus(SysUserSaveQuery user) {
        return userMapper.updateUserStatus(user.getUserId(), user.getStatus());
    }

    @Override
    public int updateUserProfile(SysUser user) {
        return userMapper.updateById(user);
    }

    @Override
    public boolean updateUserAvatar(Long userId, String avatar) {
        return userMapper.updateUserAvatar(userId, avatar) > 0;
    }

    @Override
    public int resetPwd(SysUserSaveQuery user) {
        SysUser sysUser = MapstructUtils.convert(user, SysUser.class);
        return userMapper.updateById(sysUser);
    }

    @Override
    public int resetUserPwd(Long userId, String password) {
        return userMapper.resetUserPwd(userId, password);
    }

    @Override
    @Transactional
    public int deleteUserById(Long userId) {
        // 删除用户与角色关联
        userRoleMapper.deleteByUserId(userId);
        // 删除用户与岗位表
        userPostMapper.deleteByUserId(userId);
        return userMapper.deleteById(userId);
    }

    @Override
    @Transactional
    public int deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            checkUserAllowed(userId);
            checkUserDataScope(userId);
        }
        // 删除用户与角色关联
        userRoleMapper.deleteByUserIds(userIds);
        // 删除用户与岗位关联
        userPostMapper.deleteByUserIds(userIds);
        return userMapper.deleteByIds(Arrays.asList(userIds));
    }

    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(userList) || userList.isEmpty()) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SysUser user : userList) {
            try {
                // 验证是否存在这个用户
                SysUser u = userMapper.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u)) {
                    BeanValidators.validateWithException(validator, user);
                    deptService.checkDeptDataScope(user.getDeptId());
                    String password = configService.selectConfigByKey("sys.user.initPassword");
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    userMapper.insert(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, user);
                    checkUserAllowed(u.getUserId());
                    checkUserDataScope(u.getUserId());
                    deptService.checkDeptDataScope(user.getDeptId());
                    user.setUserId(u.getUserId());
                    user.setUpdateBy(operName);
                    userMapper.updateById(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Override
    public List<SysUser> selectUserAll() {
        return userMapper.selectUserAll();
    }

    /**
     * 更新用户登录信息（IP和登录时间）
     *
     * @param userId    用户ID
     * @param loginIp   登录IP地址
     * @param loginDate 登录时间
     */
    public void updateLoginInfo(Long userId, String loginIp, Date loginDate) {
        userMapper.updateLoginInfo(userId, loginIp, loginDate);
    }

    /**
     * 新增用户岗位信息
     *
     * @param userId 用户ID
     * @param postIds 岗位组
     */
    public void insertUserPost(Long userId, Long[] postIds) {
        if (StringUtils.isNotEmpty(postIds)) {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<>(postIds.length);
            for (Long postId : postIds) {
                SysUserPost up = new SysUserPost();
                up.setUserId(userId);
                up.setPostId(postId);
                list.add(up);
            }
            userPostMapper.insertBatch(list);
        }
    }

    /**
     * 新增用户角色信息
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    public void insertUserRole(Long userId, Long[] roleIds) {
        if (StringUtils.isNotEmpty(roleIds)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<>(roleIds.length);
            for (Long roleId : roleIds) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            userRoleMapper.insertBatch(list);
        }
    }

}