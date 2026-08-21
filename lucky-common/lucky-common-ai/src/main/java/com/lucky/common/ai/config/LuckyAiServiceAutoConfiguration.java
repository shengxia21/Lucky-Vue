package com.lucky.common.ai.config;

import com.lucky.common.ai.cache.ChatModelCache;
import com.lucky.common.ai.factory.ChatServiceFactory;
import com.lucky.common.ai.factory.ImageServiceFactory;
import com.lucky.common.ai.service.chat.ChatService;
import com.lucky.common.ai.service.chat.impl.DefaultChatService;
import com.lucky.common.ai.service.image.ImagePersistenceHandler;
import com.lucky.common.ai.service.image.ImageService;
import com.lucky.common.ai.service.image.impl.DefaultImageService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Lucky AI 服务自动配置类
 *
 * @author lucky
 */
@AutoConfiguration
@ConditionalOnClass({ChatService.class, ImageService.class})
public class LuckyAiServiceAutoConfiguration {

    @Bean
    ChatServiceFactory chatServiceFactory() {
        return new ChatServiceFactory();
    }

    @Bean
    ImageServiceFactory imageServiceFactory() {
        return new ImageServiceFactory();
    }

    @Bean
    ChatModelCache chatModelCache() {
        return new ChatModelCache();
    }

    @Bean
    @ConditionalOnMissingBean
    ChatService chatService() {
        return new DefaultChatService();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(ImagePersistenceHandler.class)
    ImageService imageService() {
        return new DefaultImageService();
    }

}
