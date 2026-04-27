package com.lucky.generator.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 读取代码生成相关配置
 *
 * @author ruoyi
 */
@Getter
@Component
@ConfigurationProperties(prefix = "gen")
@PropertySource(value = {"classpath:generator.yml"})
public class GenConfig {

    /**
     * 作者
     */
    public static String author;

    /**
     * 生成包路径
     */
    public static String packageName;

    /**
     * 自动去除表前缀
     */
    public static boolean autoRemovePre;

    /**
     * 表前缀
     */
    public static String tablePrefix;

    /**
     * 是否允许生成文件覆盖到本地（自定义路径）
     */
    public static boolean allowOverwrite;

    @Value("${author}")
    public void setAuthor(String author) {
        GenConfig.author = author;
    }

    @Value("${packageName}")
    public void setPackageName(String packageName) {
        GenConfig.packageName = packageName;
    }

    @Value("${autoRemovePre}")
    public void setAutoRemovePre(boolean autoRemovePre) {
        GenConfig.autoRemovePre = autoRemovePre;
    }

    @Value("${tablePrefix}")
    public void setTablePrefix(String tablePrefix) {
        GenConfig.tablePrefix = tablePrefix;
    }

    @Value("${allowOverwrite}")
    public void setAllowOverwrite(boolean allowOverwrite) {
        GenConfig.allowOverwrite = allowOverwrite;
    }

}
