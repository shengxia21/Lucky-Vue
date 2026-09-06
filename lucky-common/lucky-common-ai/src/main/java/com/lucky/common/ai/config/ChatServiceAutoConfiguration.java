package com.lucky.common.ai.config;

import com.lucky.common.ai.cache.ChatModelCache;
import com.lucky.common.ai.chat.memory.LuckyChatMemory;
import com.lucky.common.ai.factory.ChatServiceFactory;
import com.lucky.common.ai.service.chat.ChatService;
import com.lucky.common.ai.service.chat.impl.DefaultChatService;
import com.lucky.common.ai.service.chat.stream.ChatStreamAssembler;
import com.lucky.common.ai.service.chat.stream.ChatStreamPipeline;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 聊天服务自动配置类
 *
 * @author lucky
 */
@AutoConfiguration
@ConditionalOnClass(ChatService.class)
public class ChatServiceAutoConfiguration {

    @Bean
    ChatServiceFactory chatServiceFactory() {
        return new ChatServiceFactory();
    }

    @Bean
    ChatModelCache chatModelCache() {
        return new ChatModelCache();
    }

    @Bean
    ChatStreamAssembler chatStreamAssembler(ChatModelCache chatModelCache, LuckyChatMemory luckyChatMemory) {
        return new ChatStreamAssembler(chatModelCache, luckyChatMemory);
    }

    @Bean
    ChatStreamPipeline chatStreamPipeline() {
        return new ChatStreamPipeline();
    }

    @Bean
    @ConditionalOnMissingBean
    ChatService chatService() {
        return new DefaultChatService();
    }

}
