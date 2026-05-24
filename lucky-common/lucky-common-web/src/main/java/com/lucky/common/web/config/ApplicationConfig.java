package com.lucky.common.web.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.TimeZone;

/**
 * 程序注解配置
 *
 * @author ruoyi
 */
@AutoConfiguration
// 表示通过aop框架暴露该代理对象,AopContext能够访问
@EnableAspectJAutoProxy(exposeProxy = true)
// 开启异步任务
@EnableAsync(proxyTargetClass = true)
public class ApplicationConfig {

    /**
     * Jackson增强配置
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
        return builder -> {
            // 配置时区
            builder.timeZone(TimeZone.getDefault());
            // 将Long类型转换为字符串类型(解决雪花id在前端精度丢失问题)
            builder.serializerByType(Long.class, ToStringSerializer.instance);
        };
    }

}
