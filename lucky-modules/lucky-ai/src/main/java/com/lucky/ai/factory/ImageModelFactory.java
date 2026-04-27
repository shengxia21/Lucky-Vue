package com.lucky.ai.factory;

import com.lucky.ai.core.strategy.ImageModelStrategy;
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
public class ImageModelFactory implements ApplicationContextAware {

    private final Map<String, ImageModelStrategy> imageModelStrategyMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 初始化时收集所有ImageModelStrategy的实现
        Map<String, ImageModelStrategy> strategyMap = applicationContext.getBeansOfType(ImageModelStrategy.class);
        for (ImageModelStrategy strategy : strategyMap.values()) {
            imageModelStrategyMap.put(strategy.getStrategyName(), strategy);
        }
    }

    /**
     * 根据平台获取图片模型策略（不包装代理）
     *
     * @param platform 平台枚举
     * @return 图片处理器
     */
    public ImageModelStrategy getOriginalStrategy(String platform) {
        ImageModelStrategy strategy = imageModelStrategyMap.get(platform);
        if (strategy == null) {
            throw new IllegalArgumentException("暂不支持 " + platform + " 平台的图片生成功能");
        }
        return strategy;
    }

}
