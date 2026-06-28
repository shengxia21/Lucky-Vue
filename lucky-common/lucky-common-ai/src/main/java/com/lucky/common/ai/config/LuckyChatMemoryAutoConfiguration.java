package com.lucky.common.ai.config;

import com.lucky.common.ai.chat.memory.LuckyChatMemory;
import com.lucky.common.ai.chat.memory.LuckyChatMemoryRepository;
import com.lucky.common.ai.chat.memory.LuckyInMemoryChatMemoryRepository;
import com.lucky.common.ai.chat.memory.LuckyMessageWindowChatMemory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Lucky 聊天存储库自动配置类
 *
 * @author lucky
 */
@AutoConfiguration
@ConditionalOnClass({LuckyChatMemory.class, LuckyChatMemoryRepository.class})
public class LuckyChatMemoryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    LuckyChatMemoryRepository luckyChatMemoryRepository() {
        return new LuckyInMemoryChatMemoryRepository();
    }

    @Bean
    @ConditionalOnMissingBean
    LuckyChatMemory luckyChatMemory(LuckyChatMemoryRepository luckyChatMemoryRepository) {
        return LuckyMessageWindowChatMemory.builder().luckyChatMemoryRepository(luckyChatMemoryRepository).build();
    }

}
