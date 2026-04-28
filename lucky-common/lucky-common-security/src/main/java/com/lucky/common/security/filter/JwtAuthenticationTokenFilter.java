package com.lucky.common.security.filter;

import com.lucky.common.core.utils.spring.SpringUtils;
import com.lucky.common.security.domain.event.AuthenticationTokenEvent;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * token过滤器 验证token有效性
 *
 * @author ruoyi
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        SpringUtils.context().publishEvent(new AuthenticationTokenEvent(request));
        chain.doFilter(request, response);
    }

}
