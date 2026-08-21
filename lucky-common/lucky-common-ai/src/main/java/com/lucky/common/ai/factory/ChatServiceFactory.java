package com.lucky.common.ai.factory;

import com.lucky.common.ai.service.chat.AbstractChatService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 聊天模型工厂
 *
 * @author lucky
 */
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
     * 根据提供商获取聊天服务
     *
     * @param provider 提供商
     * @return 聊天处理器
     */
    public AbstractChatService getOriginalService(String provider) {
        AbstractChatService service = chatServiceMap.get(provider);
        if (service == null) {
            throw new IllegalArgumentException("暂不支持 " + provider + " 提供商的聊天服务");
        }
        return service;
    }

}