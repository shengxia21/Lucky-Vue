package com.lucky.common.security.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 权限安全配置
 *
 * @author lucky
 */
@AutoConfiguration
public class SecurityConfig implements WebMvcConfigurer {

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
