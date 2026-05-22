package com.lucky.common.security.utils;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.lucky.common.core.constant.HttpStatus;
import com.lucky.common.core.domain.model.LoginUser;
import com.lucky.common.core.exception.ServiceException;

/**
 * 安全服务工具类
 *
 * @author ruoyi
 */
public class SecurityUtils {

    public static final String LOGIN_USER_KEY = "loginUser";
    public static final String USER_KEY = "userId";
    public static final String USER_NAME_KEY = "userName";
    public static final String DEPT_KEY = "deptId";
    public static final String DEPT_NAME_KEY = "deptName";

    /**
     * 登录操作
     *
     * @param loginUser 登录用户信息
     */
    public static void login(LoginUser loginUser) {
        SaLoginParameter parameter = new SaLoginParameter();
        parameter.setExtra(USER_KEY, loginUser.getUserId());
        parameter.setExtra(USER_NAME_KEY, loginUser.getUserName());
        parameter.setExtra(DEPT_KEY, loginUser.getDeptId());
        parameter.setExtra(DEPT_NAME_KEY, loginUser.getDeptName());
        StpUtil.login(loginUser.getUserId(), parameter);
        StpUtil.getTokenSession().set(LOGIN_USER_KEY, loginUser);
    }

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        SaSession session = StpUtil.getTokenSession();
        if (ObjectUtil.isNull(session)) {
            throw new ServiceException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
        return (LoginUser) session.get(LOGIN_USER_KEY);
    }

    /**
     * 通过Token获取用户
     **/
    public static LoginUser getLoginUser(String token) {
        SaSession session = StpUtil.getTokenSessionByToken(token);
        if (ObjectUtil.isNull(session)) {
            throw new ServiceException("通过Token获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
        return (LoginUser) session.get(LOGIN_USER_KEY);
    }

    /**
     * 刷新用户信息
     *
     * @param loginUser 登录用户信息
     */
    public static void refreshLoginUser(LoginUser loginUser) {
        if (ObjectUtil.isNotNull(loginUser)) {
            StpUtil.getTokenSession().set(LOGIN_USER_KEY, loginUser);
        }
    }

    /**
     * 获取用户ID
     **/
    public static Long getUserId() {
        return Convert.toLong(getExtra(USER_KEY));
    }

    /**
     * 获取部门ID
     **/
    public static Long getDeptId() {
        return Convert.toLong(getExtra(DEPT_KEY));
    }

    /**
     * 获取用户账户
     **/
    public static String getUserName() {
        return Convert.toStr(getExtra(USER_NAME_KEY));
    }

    /**
     * 获取部门名称
     **/
    public static String getDeptName() {
        return Convert.toStr(getExtra(DEPT_NAME_KEY));
    }

    /**
     * 获取当前 Token 的扩展信息
     *
     * @param key 键值
     * @return 对应的扩展数据
     */
    private static Object getExtra(String key) {
        try {
            return StpUtil.getExtra(key);
        } catch (Exception e) {
            throw new ServiceException("获取扩展Key信息异常：" + key, HttpStatus.ERROR);
        }
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        return BCrypt.hashpw(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }

    /**
     * 是否为管理员
     *
     * @return 结果
     */
    public static boolean isAdmin() {
        return isAdmin(getUserId());
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

}
