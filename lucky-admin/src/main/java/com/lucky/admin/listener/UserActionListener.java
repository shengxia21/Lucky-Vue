package com.lucky.admin.listener;

import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import com.lucky.admin.service.SysLoginService;
import com.lucky.common.core.constant.CacheConstants;
import com.lucky.common.core.constant.Constants;
import com.lucky.common.core.domain.dto.UserOnlineDTO;
import com.lucky.common.core.utils.MessageUtils;
import com.lucky.common.core.utils.ServletUtils;
import com.lucky.common.core.utils.ip.AddressUtils;
import com.lucky.common.core.utils.ip.IpUtils;
import com.lucky.common.redis.utils.RedisCache;
import com.lucky.common.security.utils.SecurityUtils;
import com.lucky.common.web.utils.UserAgentUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 用户行为监听器
 *
 * @author lucky
 */
@Slf4j
@Component
public class UserActionListener implements SaTokenListener {

    @Resource
    private RedisCache redisCache;

    @Resource
    private SysLoginService loginService;

    /**
     * 每次登录时触发
     */
    @Override
    public void doLogin(String loginType, Object userId, String tokenValue, SaLoginParameter loginParameter) {
        // 缓存用户信息到Redis
        String userAgent = ServletUtils.getRequest().getHeader("User-Agent");
        String ip = IpUtils.getIpAddr();
        UserOnlineDTO dto = new UserOnlineDTO();
        dto.setTokenId(tokenValue);
        dto.setDeptName((String) loginParameter.getExtra(SecurityUtils.DEPT_NAME_KEY));
        String userName = (String) loginParameter.getExtra(SecurityUtils.USER_NAME_KEY);
        dto.setUserName(userName);
        dto.setIpaddr(ip);
        dto.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        dto.setBrowser(UserAgentUtils.getBrowser(userAgent));
        dto.setOs(UserAgentUtils.getOperatingSystem(userAgent));
        dto.setLoginTime(System.currentTimeMillis());
        if (loginParameter.getTimeout() == -1) {
            redisCache.setCacheObject(CacheConstants.LOGIN_TOKEN_KEY + tokenValue, dto);
        } else {
            redisCache.setCacheObject(CacheConstants.LOGIN_TOKEN_KEY + tokenValue, dto, loginParameter.getTimeout(), TimeUnit.SECONDS);
        }
        // 记录登录日志
        loginService.recordLoginInfo(userName, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        // 更新登录信息
        loginService.updateLoginInfo(Long.valueOf(userId.toString()));
        log.info("user doLogin, userId:{}, token:{}", userId, tokenValue);
    }

    /**
     * 每次注销时触发
     */
    @Override
    public void doLogout(String loginType, Object userId, String tokenValue) {
        redisCache.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + tokenValue);
        log.info("user doLogout, userId:{}, token:{}", userId, tokenValue);
    }

    /**
     * 每次被踢下线时触发
     */
    @Override
    public void doKickout(String loginType, Object userId, String tokenValue) {
        redisCache.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + tokenValue);
        log.info("user doKickout, userId:{}, token:{}", userId, tokenValue);
    }

    /**
     * 每次被顶下线时触发
     */
    @Override
    public void doReplaced(String loginType, Object userId, String tokenValue) {
        redisCache.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + tokenValue);
        log.info("user doReplaced, userId:{}, token:{}", userId, tokenValue);
    }

    /**
     * 每次被封禁时触发
     */
    @Override
    public void doDisable(String loginType, Object userId, String service, int level, long disableTime) {
    }

    /**
     * 每次被解封时触发
     */
    @Override
    public void doUntieDisable(String loginType, Object userId, String service) {
    }

    /**
     * 每次打开二级认证时触发
     */
    @Override
    public void doOpenSafe(String loginType, String tokenValue, String service, long safeTime) {
    }

    /**
     * 每次创建Session时触发
     */
    @Override
    public void doCloseSafe(String loginType, String tokenValue, String service) {
    }

    /**
     * 每次创建Session时触发
     */
    @Override
    public void doCreateSession(String id) {
    }

    /**
     * 每次注销Session时触发
     */
    @Override
    public void doLogoutSession(String id) {
    }

    /**
     * 每次Token续期时触发
     */
    @Override
    public void doRenewTimeout(String loginType, Object userId, String tokenValue, long timeout) {
    }

}
