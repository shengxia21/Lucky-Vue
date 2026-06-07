package com.lucky.common.ai.strategy.image;

import com.lucky.common.ai.domain.context.ImageContext;
import com.lucky.common.ai.enums.AiPlatformEnum;
import com.lucky.common.ai.strategy.ImageModelStrategy;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.zhipuai.ZhiPuAiImageModel;
import org.springframework.ai.zhipuai.ZhiPuAiImageOptions;
import org.springframework.ai.zhipuai.api.ZhiPuAiImageApi;
import org.springframework.stereotype.Component;

/**
 * 智谱图片处理器
 *
 * @author lucky
 */
@Component
public class ZhiPuImageStrategy implements ImageModelStrategy {

    @Override
    public ImageModel buildImageModel(String baseUrl, String apiKey) {
        ZhiPuAiImageApi zhiPuAiImageApi = new ZhiPuAiImageApi(apiKey);
        return new ZhiPuAiImageModel(zhiPuAiImageApi);
    }

    @Override
    public ImageOptions buildImageOptions(ImageContext imageContext) {
        return ZhiPuAiImageOptions.builder()
                .model(imageContext.getModel())
                .build();
    }

    @Override
    public String getStrategyName() {
        return AiPlatformEnum.ZHI_PU.getPlatform();
    }

}
