package com.lucky.web.core.config;

import com.lucky.common.config.LuckyConfig;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger2的接口配置
 *
 * @author ruoyi
 */
@Configuration
public class SwaggerConfig {

    /**
     * 系统基础配置
     */
    @Resource
    private LuckyConfig LuckyConfig;

    /**
     * 自定义的 OpenAPI 对象
     */
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().components(new Components()
                        // 设置认证的请求头
                        .addSecuritySchemes("apikey", securityScheme()))
                .addSecurityItem(new SecurityRequirement().addList("apikey"))
                .info(getApiInfo());
    }

    @Bean
    public SecurityScheme securityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .name("Authorization")
                .in(SecurityScheme.In.HEADER)
                .scheme("Bearer");
    }

    /**
     * 添加摘要信息
     */
    public Info getApiInfo() {
        return new Info()
                // 设置标题
                .title("标题：浮生管理系统_接口文档")
                // 描述
                .description("描述：用于管理集团旗下公司的人员信息,具体包括XXX,XXX模块...")
                // 作者信息
                .contact(new Contact().name(LuckyConfig.getName()))
                // 版本
                .version("版本号:" + LuckyConfig.getVersion());
    }

}
