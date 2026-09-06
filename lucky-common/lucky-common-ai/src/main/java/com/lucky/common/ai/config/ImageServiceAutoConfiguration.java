package com.lucky.common.ai.config;

import com.lucky.common.ai.factory.ImageServiceFactory;
import com.lucky.common.ai.service.image.ImagePersistenceHandler;
import com.lucky.common.ai.service.image.ImageService;
import com.lucky.common.ai.service.image.impl.DefaultImageService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 图像服务自动配置类
 *
 * @author lucky
 */
@AutoConfiguration
@ConditionalOnClass(ImageService.class)
public class ImageServiceAutoConfiguration {

    @Bean
    ImageServiceFactory imageServiceFactory() {
        return new ImageServiceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(ImagePersistenceHandler.class)
    ImageService imageService() {
        return new DefaultImageService();
    }

}
