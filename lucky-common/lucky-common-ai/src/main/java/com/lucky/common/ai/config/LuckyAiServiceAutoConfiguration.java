package com.lucky.common.ai.config;

import com.lucky.common.ai.chat.service.ChatService;
import com.lucky.common.ai.chat.service.impl.ChatServiceFacade;
import com.lucky.common.ai.image.ImagePersistenceHandler;
import com.lucky.common.ai.image.ImageService;
import com.lucky.common.ai.image.impl.ImageServiceFacade;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Lucky AI 服务外观自动配置类
 * <p>
 * 注册 {@link ChatService} 与 {@link ImageService} 的默认外观实现：
 * <ul>
 *     <li>{@link ChatServiceFacade}：聊天服务外观，依赖 {@code ChatServiceFactory} 与 {@code LuckyChatMemory}</li>
 *     <li>{@link ImageServiceFacade}：图片服务外观，依赖 {@code ImageServiceFactory} 与 {@link ImagePersistenceHandler}</li>
 * </ul>
 * 业务模块可通过自定义同类型 Bean 覆盖默认实现（{@code @ConditionalOnMissingBean}）。
 * 当容器中不存在 {@link ImagePersistenceHandler} 时，不注册图片服务外观，
 * 使通用模块在无业务模块接入时仍可正常启动。
 *
 * @author lucky
 */
@AutoConfiguration
@ConditionalOnClass({ChatService.class, ImageService.class})
public class LuckyAiServiceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    ChatService chatService() {
        return new ChatServiceFacade();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(ImagePersistenceHandler.class)
    ImageService imageService() {
        return new ImageServiceFacade();
    }

}
