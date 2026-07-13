package com.lucky.admin.service;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.lucky.common.core.constant.CacheConstants;
import com.lucky.common.core.constant.Constants;
import com.lucky.common.core.constant.UserConstants;
import com.lucky.common.core.domain.dto.UserDTO;
import com.lucky.common.core.domain.model.LoginBody;
import com.lucky.common.core.domain.model.LoginUser;
import com.lucky.common.core.enums.UserStatus;
import com.lucky.common.core.exception.ServiceException;
import com.lucky.common.core.exception.user.*;
import com.lucky.common.core.service.PermissionService;
import com.lucky.common.core.utils.MessageUtils;
import com.lucky.common.core.utils.ServletUtils;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.core.utils.ip.IpUtils;
import com.lucky.common.core.utils.spring.SpringUtils;
import com.lucky.common.log.event.LoginInfoEvent;
import com.lucky.common.redis.utils.RedisCache;
import com.lucky.common.security.utils.SecurityUtils;
import com.lucky.system.service.ISysConfigService;
import com.lucky.system.service.ISysUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 登录校验方法
 *
 * @author lucky
 */
@Slf4j
@Component
public class SysLoginService {

    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;

    @Resource
    private RedisCache redisCache;

    @Resource
    private ISysUserService userService;

    @Resource
    private ISysConfigService configService;

    @Resource
    private PermissionService permissionService;

    /**
     * 登录验证
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    public String login(LoginBody loginBody) {
        String userName = loginBody.getUsername();
        String password = loginBody.getPassword();
        String code = loginBody.getCode();
        String uuid = loginBody.getUuid();
        // 验证码校验
        validateCaptcha(userName, code, uuid);
        // 登录前置校验
        loginPreCheck(userName, password);
        // 验证账号，并加载用户信息
        UserDTO user = loadUserByUserName(userName);
        // 校验账号密码
        validatePassword(user.getUserName(), user.getPassword(), password);
        // 构建登录用户信息
        LoginUser loginUser = buildLoginUser(user);
        // Sa-Token 登录操作，触发doLogin方法
        SecurityUtils.login(loginUser);
        // 更新登录用户信息
        updateLoginInfo(loginUser.getUserId());
        // 获取登录token
        return StpUtil.getTokenValue();
    }

    /**
     * 校验验证码
     *
     * @param userName 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    public void validateCaptcha(String userName, String code, String uuid) {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            if (captcha == null) {
                recordLoginInfo(userName, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire"));
                throw new CaptchaExpireException();
            }
            redisCache.deleteObject(verifyKey);
            if (!code.equalsIgnoreCase(captcha)) {
                recordLoginInfo(userName, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error"));
                throw new CaptchaException();
            }
        }
    }

    /**
     * 登录前置校验
     *
     * @param userName 用户名
     * @param password 用户密码
     */
    public void loginPreCheck(String userName, String password) {
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            recordLoginInfo(userName, Constants.LOGIN_FAIL, MessageUtils.message("not.null"));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            recordLoginInfo(userName, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match"));
            throw new UserPasswordNotMatchException();
        }
        // 用户名不在指定范围内 错误
        if (userName.length() < UserConstants.USERNAME_MIN_LENGTH
                || userName.length() > UserConstants.USERNAME_MAX_LENGTH) {
            recordLoginInfo(userName, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match"));
            throw new UserPasswordNotMatchException();
        }
        // IP黑名单校验
        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr())) {
            recordLoginInfo(userName, Constants.LOGIN_FAIL, MessageUtils.message("login.blocked"));
            throw new BlackListException();
        }
    }

    /**
     * 通过用户名加载用户信息
     *
     * @param userName 用户账号
     */
    public UserDTO loadUserByUserName(String userName) {
        UserDTO user = userService.selectUserByUserName(userName);
        if (StringUtils.isNull(user)) {
            log.info("登录用户：{} 不存在.", userName);
            throw new ServiceException(MessageUtils.message("user.not.exists"));
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录用户：{} 已被删除.", userName);
            throw new ServiceException(MessageUtils.message("user.password.delete"));
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", userName);
            throw new ServiceException(MessageUtils.message("user.blocked"));
        }
        return user;
    }

    /**
     * 校验账号密码
     *
     * @param userName      用户账号
     * @param password      密码
     * @param inputPassword 输入密码
     */
    public void validatePassword(String userName, String password, String inputPassword) {
        String cacheKey = CacheConstants.PWD_ERR_CNT_KEY + userName;
        Integer retryCount = redisCache.getCacheObject(cacheKey);

        if (retryCount == null) {
            retryCount = 0;
        }

        if (retryCount >= maxRetryCount) {
            throw new UserPasswordRetryLimitExceedException(maxRetryCount, lockTime);
        }

        if (!SecurityUtils.matchesPassword(inputPassword, password)) {
            retryCount = retryCount + 1;
            redisCache.setCacheObject(cacheKey, retryCount, Duration.ofMinutes(lockTime));
            throw new UserPasswordNotMatchException();
        } else {
            if (redisCache.hasKey(cacheKey)) {
                redisCache.deleteObject(cacheKey);
            }
        }
    }

    /**
     * 构建登录用户信息
     *
     * @param user 用户信息
     * @return 登录用户信息
     */
    public LoginUser buildLoginUser(UserDTO user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getUserId());
        loginUser.setDeptId(user.getDeptId());
        loginUser.setUserName(user.getUserName());
        loginUser.setNickName(user.getNickName());
        if (ObjectUtil.isNotNull(user.getDept())) {
            loginUser.setDeptName(user.getDept().getDeptName());
        }
        loginUser.setMenuPermission(permissionService.getMenuPermission(user.getUserId()));
        loginUser.setRolePermission(permissionService.getRolePermission(user.getUserId()));
        loginUser.setUser(user);
        return loginUser;
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void updateLoginInfo(Long userId) {
        userService.updateLoginInfo(userId, IpUtils.getIpAddr(), LocalDateTime.now());
    }

    /**
     * 退出登录
     */
    public void logout() {
        try {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            // 记录退出登录信息
            recordLoginInfo(loginUser.getUserName(), Constants.LOGOUT, MessageUtils.message("user.logout.success"));
        } catch (NotLoginException ignored) {
        } finally {
            StpUtil.logout();
        }
    }

    /**
     * 记录登录信息
     *
     * @param userName 用户名
     * @param status   状态
     * @param message  消息内容
     */
    public void recordLoginInfo(String userName, String status, String message) {
        LoginInfoEvent loginInfoEvent = new LoginInfoEvent();
        loginInfoEvent.setUserName(userName);
        loginInfoEvent.setStatus(status);
        loginInfoEvent.setMessage(message);
        loginInfoEvent.setRequest(ServletUtils.getRequest());
        SpringUtils.context().publishEvent(loginInfoEvent);
    }

}
