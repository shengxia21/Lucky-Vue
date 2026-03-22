package com.lucky.ai.core.processor.image;

import com.lucky.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 图片处理器工厂
 * 负责管理和分发不同平台的图片处理器
 * [工厂模式：自动注入所有实现类]
 *
 * @author lucky
 */
@Component
public class ImageProcessorFactory {

    private final Map<String, ImageProcessor> processorMap;

    @Autowired
    public ImageProcessorFactory(List<ImageProcessor> processors) {
        this.processorMap = processors.stream()
                .collect(Collectors.toMap(
                        ImageProcessor::getProcessorName,
                        processor -> processor)
                );
    }

    /**
     * 根据平台获取图片处理器
     *
     * @param platform 平台枚举
     * @return 图片处理器
     */
    public ImageProcessor getProcessor(String platform) {
        ImageProcessor processor = processorMap.get(platform);
        if (processor == null) {
            throw new ServiceException("暂不支持 " + platform + " 平台的图片生成功能");
        }
        return processor;
    }

}
