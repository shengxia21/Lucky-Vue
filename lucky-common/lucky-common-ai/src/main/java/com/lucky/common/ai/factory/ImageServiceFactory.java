package com.lucky.common.ai.factory;

import com.lucky.common.ai.service.image.AbstractImageService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 图片模型工厂
 *
 * @author lucky
 */
@Component
public class ImageServiceFactory implements ApplicationContextAware {

    private final Map<String, AbstractImageService> imageServiceMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 初始化时收集所有AbstractImageService的实现
        Map<String, AbstractImageService> serviceMap = applicationContext.getBeansOfType(AbstractImageService.class);
        for (AbstractImageService service : serviceMap.values()) {
            if (service != null ) {
                imageServiceMap.put(service.getProviderName(), service);
            }
        }
    }

    /**
     * 根据提供商获取图片服务（不包装代理）
     *
     * @param provider 提供商
     * @return 图片处理器
     */
    public AbstractImageService getOriginalService(String provider) {
        AbstractImageService service = imageServiceMap.get(provider);
        if (service == null) {
            throw new IllegalArgumentException("暂不支持 " + provider + " 提供商的图片服务功能");
        }
        return service;
    }

}
