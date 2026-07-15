package com.lucky.common.ai.factory;

import com.lucky.common.ai.service.chat.AbstractChatService;
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
public class ChatServiceFactory implements ApplicationContextAware {

    private final Map<String, AbstractChatService> chatServiceMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 初始化时收集所有AbstractChatService的实现
        Map<String, AbstractChatService> serviceMap = applicationContext.getBeansOfType(AbstractChatService.class);
        for (AbstractChatService service : serviceMap.values()) {
            if (service != null) {
                chatServiceMap.put(service.getProviderName(), service);
            }
        }
    }

    /**
     * 根据平台获取聊天模型
     *
     * @param platform 平台枚举
     * @return 聊天处理器
     */
    public AbstractChatService getOriginalService(String platform) {
        AbstractChatService service = chatServiceMap.get(platform);
        if (service == null) {
            throw new IllegalArgumentException("暂不支持 " + platform + " 平台的聊天功能");
        }
        return service;
    }

}