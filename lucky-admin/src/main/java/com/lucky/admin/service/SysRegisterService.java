package com.lucky.admin.service;

import com.lucky.common.core.constant.CacheConstants;
import com.lucky.common.core.constant.Constants;
import com.lucky.common.core.constant.UserConstants;
import com.lucky.common.core.domain.model.RegisterBody;
import com.lucky.common.core.exception.user.CaptchaException;
import com.lucky.common.core.exception.user.CaptchaExpireException;
import com.lucky.common.core.utils.DateUtils;
import com.lucky.common.core.utils.MessageUtils;
import com.lucky.common.core.utils.ServletUtils;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.core.utils.spring.SpringUtils;
import com.lucky.common.log.event.LoginInfoEvent;
import com.lucky.common.redis.utils.RedisCache;
import com.lucky.common.security.utils.SecurityUtils;
import com.lucky.system.domain.SysUser;
import com.lucky.system.service.ISysConfigService;
import com.lucky.system.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 注册校验方法
 *
 * @author lucky
 */
@Component
public class SysRegisterService {

    @Resource
    private ISysUserService userService;

    @Resource
    private ISysConfigService configService;

    @Resource
    private RedisCache redisCache;

    /**
     * 注册
     *
     * @param registerBody 注册信息
     */
    public String register(RegisterBody registerBody) {
        String userName = registerBody.getUsername();
        String password = registerBody.getPassword();
        String code = registerBody.getCode();
        String uuid = registerBody.getUuid();
        // 校验验证码
        validateCaptcha(code, uuid);
        // 校验注册用户信息
        String msg = "";
        SysUser sysUser = new SysUser();
        sysUser.setUserName(userName);
        if (StringUtils.isEmpty(userName)) {
            msg = "用户名不能为空";
        } else if (StringUtils.isEmpty(password)) {
            msg = "用户密码不能为空";
        } else if (userName.length() < UserConstants.USERNAME_MIN_LENGTH
                || userName.length() > UserConstants.USERNAME_MAX_LENGTH) {
            msg = "账户长度必须在2到20个字符之间";
        } else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            msg = "密码长度必须在5到20个字符之间";
        } else if (!userService.checkUserNameUnique(sysUser.getUserId(), sysUser.getUserName())) {
            msg = "保存用户'" + userName + "'失败，注册账号已存在";
        } else {
            sysUser.setNickName(userName);
            sysUser.setPwdUpdateDate(DateUtils.getNowDate());
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            boolean regFlag = userService.registerUser(sysUser);
            if (!regFlag) {
                msg = "注册失败,请联系系统管理人员";
            } else {
                // 记录注册信息
                LoginInfoEvent loginInfoEvent = new LoginInfoEvent();
                loginInfoEvent.setUserName(userName);
                loginInfoEvent.setStatus(Constants.REGISTER);
                loginInfoEvent.setMessage(MessageUtils.message("user.register.success"));
                loginInfoEvent.setRequest(ServletUtils.getRequest());
                SpringUtils.context().publishEvent(loginInfoEvent);
            }
        }
        return msg;
    }

    /**
     * 校验验证码
     *
     * @param code 验证码
     * @param uuid 唯一标识
     */
    public void validateCaptcha(String code, String uuid) {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            redisCache.deleteObject(verifyKey);
            if (captcha == null) {
                throw new CaptchaExpireException();
            }
            if (!code.equalsIgnoreCase(captcha)) {
                throw new CaptchaException();
            }
        }
    }

}
