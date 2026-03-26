package com.lucky.ai.core.strategy;

import com.lucky.ai.core.context.ImageContext;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptions;

/**
 * 图片模型策略
 *
 * @author lucky
 */
public interface ImageModelStrategy {

    /**
     * 构建图片模型
     *
     * @param apiKey API密钥
     * @return 图片模型
     */
    ImageModel buildImageModel(String apiKey);

    /**
     * 构建图片生成选项
     *
     * @param imageContext 图片上下文
     * @return 图片生成选项
     */
    ImageOptions buildImageOptions(ImageContext imageContext);

    /**
     * 获取策略名称
     *
     * @return 策略名称
     */
    String getStrategyName();

}
