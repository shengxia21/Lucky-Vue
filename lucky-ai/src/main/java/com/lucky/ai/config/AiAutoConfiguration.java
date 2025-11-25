package com.lucky.ai.config;

import com.lucky.ai.factory.AiModelFactory;
import com.lucky.ai.factory.AiModelFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI自动配置类
 *
 * @author lucky
 */
@Configuration
public class AiAutoConfiguration {

    @Bean
    public AiModelFactory aiModelFactory() {
        return new AiModelFactoryImpl();
    }

}
