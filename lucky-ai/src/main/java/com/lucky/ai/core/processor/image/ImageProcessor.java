package com.lucky.ai.core.processor.image;

import com.lucky.ai.core.context.ImageContext;

/**
 * 图片处理器接口
 * 负责处理不同AI平台的图片生成逻辑
 * [策略模式核心类]
 *
 * @author lucky
 */
public interface ImageProcessor {

    /**
     * 处理图片生成逻辑
     *
     * @param imageContext 图片上下文
     */
    void processImage(ImageContext imageContext);

    /**
     * 获取处理器名称
     *
     * @return 处理器名称
     */
    String getProcessorName();

}
