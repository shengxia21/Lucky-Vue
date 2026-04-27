package com.lucky.common.security.handler;

import com.alibaba.fastjson2.JSON;
import com.lucky.common.core.domain.AjaxResult;
import com.lucky.common.core.utils.MessageUtils;
import com.lucky.common.core.utils.ServletUtils;
import com.lucky.common.core.utils.spring.SpringUtils;
import com.lucky.common.security.event.LogoutSuccessEvent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * 自定义退出处理类 返回成功
 *
 * @author ruoyi
 */
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    /**
     * 退出处理
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SpringUtils.context().publishEvent(new LogoutSuccessEvent(request));
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.success(MessageUtils.message("user.logout.success"))));
    }

}
