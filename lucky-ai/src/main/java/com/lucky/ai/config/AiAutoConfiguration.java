package com.lucky.ai.config;

import com.lucky.ai.core.model.AiModelFactory;
import com.lucky.ai.core.model.AiModelFactoryImpl;
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
