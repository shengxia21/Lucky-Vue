package com.lucky.framework.security.handle;

import com.alibaba.fastjson2.JSON;
import com.lucky.common.constant.Constants;
import com.lucky.common.core.domain.AjaxResult;
import com.lucky.common.core.domain.model.LoginUser;
import com.lucky.common.manager.AsyncManager;
import com.lucky.common.utils.MessageUtils;
import com.lucky.common.utils.ServletUtils;
import com.lucky.common.utils.StringUtils;
import com.lucky.framework.web.service.TokenService;
import com.lucky.system.factory.AsyncSystemFactory;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

/**
 * 自定义退出处理类 返回成功
 *
 * @author ruoyi
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Resource
    private TokenService tokenService;

    /**
     * 退出处理
     *
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            // 记录用户退出日志
            AsyncManager.me().execute(AsyncSystemFactory.recordLoginInfo(userName, Constants.LOGOUT, MessageUtils.message("user.logout.success")));
        }
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.success(MessageUtils.message("user.logout.success"))));
    }

}
