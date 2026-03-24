package com.lucky.ai.core.processor.chat;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 聊天处理器工厂
 * 负责管理和分发不同平台的聊天处理器
 * [工厂模式：自动注入所有实现类]
 *
 * @author lucky
 */
@Component
public class ChatProcessorFactory implements ApplicationContextAware {

    private final Map<String, AbstractChatProcessor> chatProcessorMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 初始化时收集所有ChatProcessor的实现
        Map<String, AbstractChatProcessor> processorMap = applicationContext.getBeansOfType(AbstractChatProcessor.class);
        for (AbstractChatProcessor processor : processorMap.values()) {
            chatProcessorMap.put(processor.getProcessorName(), processor);
        }
    }

    /**
     * 根据平台获取聊天处理器
     *
     * @param platform 平台枚举
     * @return 聊天处理器
     */
    public AbstractChatProcessor getOriginalProcessor(String platform) {
        AbstractChatProcessor processor = chatProcessorMap.get(platform);
        if (processor == null) {
            throw new IllegalArgumentException("暂不支持 " + platform + " 平台的聊天功能");
        }
        return processor;
    }

}