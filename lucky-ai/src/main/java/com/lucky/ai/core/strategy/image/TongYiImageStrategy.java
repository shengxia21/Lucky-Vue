package com.lucky.ai.core.strategy.image;

import com.alibaba.cloud.ai.dashscope.api.DashScopeImageApi;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import com.lucky.ai.core.context.ImageContext;
import com.lucky.ai.core.strategy.ImageModelStrategy;
import com.lucky.ai.core.vo.image.ImageDrawRequest;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.enums.model.AiPlatformEnum;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptions;
import org.springframework.stereotype.Component;

/**
 * 通义千问图片处理器
 *
 * @author lucky
 */
@Component
public class TongYiImageStrategy implements ImageModelStrategy {

    @Override
    public ImageModel buildImageModel(String apiKey) {
        DashScopeImageApi dashScopeApi = DashScopeImageApi.builder()
                .apiKey(apiKey)
                .build();
        return DashScopeImageModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
    }

    @Override
    public ImageOptions buildImageOptions(ImageContext imageContext) {
        ImageDrawRequest request = imageContext.getRequest();
        AiModel model = imageContext.getModel();
        return DashScopeImageOptions.builder()
                .model(model.getModel()).n(1)
                .height(request.getHeight()).width(request.getWidth())
                .promptExtend(Boolean.parseBoolean(request.getOptions().getOrDefault("promptExtend", "false")))
                .negativePrompt(request.getOptions().getOrDefault("negativePrompt", ""))
                .enableInterleave(true)
                .build();
    }

    @Override
    public String getStrategyName() {
        return AiPlatformEnum.TONG_YI.getPlatform();
    }

}
