package com.lucky.ai.core.strategy.image;

import com.lucky.ai.core.context.ImageContext;
import com.lucky.ai.core.strategy.ImageModelStrategy;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.enums.model.AiPlatformEnum;
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
    public ImageModel buildImageModel(String apiKey) {
        ZhiPuAiImageApi zhiPuAiImageApi = new ZhiPuAiImageApi(apiKey);
        return new ZhiPuAiImageModel(zhiPuAiImageApi);
    }

    @Override
    public ImageOptions buildImageOptions(ImageContext imageContext) {
        AiModel model = imageContext.getModel();
        return ZhiPuAiImageOptions.builder()
                .model(model.getModel())
                .build();
    }

    @Override
    public String getStrategyName() {
        return AiPlatformEnum.ZHI_PU.getPlatform();
    }

}
