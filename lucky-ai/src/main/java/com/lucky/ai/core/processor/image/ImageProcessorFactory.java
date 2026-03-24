package com.lucky.ai.core.processor.image;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 图片处理器工厂
 * 负责管理和分发不同平台的图片处理器
 * [工厂模式：自动注入所有实现类]
 *
 * @author lucky
 */
@Component
public class ImageProcessorFactory implements ApplicationContextAware {

    private final Map<String, AbstractImageProcessor> imageProcessorMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 初始化时收集所有ImageProcessor的实现
        Map<String, AbstractImageProcessor> processorMap = applicationContext.getBeansOfType(AbstractImageProcessor.class);
        for (AbstractImageProcessor processor : processorMap.values()) {
            imageProcessorMap.put(processor.getProcessorName(), processor);
        }
    }

    /**
     * 根据平台获取图片处理器（不包装代理）
     *
     * @param platform 平台枚举
     * @return 图片处理器
     */
    public AbstractImageProcessor getOriginalProcessor(String platform) {
        AbstractImageProcessor processor = imageProcessorMap.get(platform);
        if (processor == null) {
            throw new IllegalArgumentException("暂不支持 " + platform + " 平台的图片生成功能");
        }
        return processor;
    }

}
