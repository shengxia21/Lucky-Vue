package com.lucky.common.security.config;

import cn.dev33.satoken.filter.SaTokenContextFilterForJakartaServlet;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.servlet.DispatcherType;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.EnumSet;

/**
 * 权限安全配置
 *
 * @author lucky
 */
@AutoConfiguration
public class SecurityConfig implements WebMvcConfigurer {

    /**
     * 重新注册 Sa-Token 上下文过滤器，使其覆盖 Servlet 异步分发。
     * <p>
     * SSE、WebSocket 握手等场景可能触发 ASYNC/ERROR dispatcher，如果上下文过滤器只处理普通 REQUEST，
     * 后续统一鉴权或业务代码读取 SaHolder/StpUtil 时会出现 SaTokenContext 未初始化。
     *
     * @param filter Sa-Token 官方上下文过滤器
     * @return 过滤器注册配置
     */
    @Bean
    public FilterRegistrationBean<SaTokenContextFilterForJakartaServlet> saTokenContextFilterRegistration(
            SaTokenContextFilterForJakartaServlet filter) {
        FilterRegistrationBean<SaTokenContextFilterForJakartaServlet> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.setName("saTokenContextFilterForServlet");
        registration.addUrlPatterns("/*");
        registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.ASYNC, DispatcherType.ERROR));
        registration.setAsyncSupported(true);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    /**
     * 注册sa-token的拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册路由拦截器，自定义验证规则
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                // 拦截所有请求
                .addPathPatterns("/**")
                // 排除图片资源
                .excludePathPatterns("/profile/**")
                // 排除swagger-ui 数据
                .excludePathPatterns("/swagger-ui/**", "/v3/api-docs/**");
    }

}
