package com.lucky.ai.factory;

import com.lucky.ai.core.strategy.ChatModelStrategy;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 聊天模型工厂
 *
 * @author lucky
 */
@Component
public class ChatModelFactory implements ApplicationContextAware {

    private final Map<String, ChatModelStrategy> chatModelStrategyMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 初始化时收集所有ChatModelStrategy的实现
        Map<String, ChatModelStrategy> strategyMap = applicationContext.getBeansOfType(ChatModelStrategy.class);
        for (ChatModelStrategy strategy : strategyMap.values()) {
            chatModelStrategyMap.put(strategy.getStrategyName(), strategy);
        }
    }

    /**
     * 根据平台获取聊天模型
     *
     * @param platform 平台枚举
     * @return 聊天处理器
     */
    public ChatModelStrategy getOriginalStrategy(String platform) {
        ChatModelStrategy strategy = chatModelStrategyMap.get(platform);
        if (strategy == null) {
            throw new IllegalArgumentException("暂不支持 " + platform + " 平台的聊天功能");
        }
        return strategy;
    }

}