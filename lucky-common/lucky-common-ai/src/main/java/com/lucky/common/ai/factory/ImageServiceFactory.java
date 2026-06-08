package com.lucky.common.ai.factory;

import com.lucky.common.ai.service.AbstractImageService;
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
            imageServiceMap.put(service.getProviderName(), service);
        }
    }

    /**
     * 根据平台获取图片模型策略（不包装代理）
     *
     * @param platform 平台枚举
     * @return 图片处理器
     */
    public AbstractImageService getOriginalService(String platform) {
        AbstractImageService service = imageServiceMap.get(platform);
        if (service == null) {
            throw new IllegalArgumentException("暂不支持 " + platform + " 平台的图片生成功能");
        }
        return service;
    }

}
