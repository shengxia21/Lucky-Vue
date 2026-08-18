package com.lucky.common.ai.service.image;

import com.lucky.common.ai.domain.request.ImageRequest;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptions;

/**
 * 抽象图片服务接口（策略）
 *
 * @author lucky
 */
public interface AbstractImageService {

    /**
     * 构建图片模型
     *
     * @param baseUrl 基础URL
     * @param apiKey  API密钥
     * @return 图片模型
     */
    ImageModel buildImageModel(String baseUrl, String apiKey);

    /**
     * 构建图片生成选项
     *
     * @param imageRequest 图片请求
     * @return 图片生成选项
     */
    ImageOptions buildImageOptions(ImageRequest imageRequest);

    /**
     * 获取服务提供商名称
     *
     * @return 提供商名称
     */
    String getProviderName();

}
