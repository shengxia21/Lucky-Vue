package com.lucky.generator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 读取代码生成相关配置
 *
 * @author ruoyi
 */
@Component
@ConfigurationProperties(prefix = "gen")
@PropertySource(value = {"classpath:generator.yml"})
public class GenConfig {

    /**
     * 作者
     */
    @Value("${author}")
    public static String author;

    /**
     * 生成包路径
     */
    @Value("${packageName}")
    public static String packageName;

    /**
     * 自动去除表前缀
     */
    @Value("${autoRemovePre}")
    public static boolean autoRemovePre;

    /**
     * 表前缀
     */
    @Value("${tablePrefix}")
    public static String tablePrefix;

    /**
     * 是否允许生成文件覆盖到本地（自定义路径）
     */
    @Value("${allowOverwrite}")
    public static boolean allowOverwrite;

}
