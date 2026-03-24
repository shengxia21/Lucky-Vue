package com.lucky.ai.factory;

import com.lucky.ai.core.context.ImageContext;
import com.lucky.ai.core.processor.image.ImageProcessor;
import com.lucky.ai.core.processor.image.ImageProcessorFactory;
import com.lucky.ai.domain.AiModel;
import com.lucky.common.utils.spring.SpringUtils;

/**
 * 异步工厂（产生任务用）
 *
 * @author lucky
 */
public class AsyncAiFactory {

    /**
     * 执行绘制图片任务
     *
     * @param imageContext 图片上下文
     * @return 任务
     */
    public static Runnable executeDrawImage(ImageContext imageContext) {
        return () -> {
            AiModel model = imageContext.getModel();
            String platform = model.getPlatform();
            ImageProcessorFactory imageProcessorFactory = SpringUtils.getBean(ImageProcessorFactory.class);

            // 根据平台获取图片处理器
            ImageProcessor imageProcessor = imageProcessorFactory.getOriginalProcessor(platform);
            // 执行图片生成任务
            imageProcessor.processImage(imageContext);
        };
    }

}
