package com.lucky.common.ai.strategy.image;

import com.alibaba.cloud.ai.dashscope.api.DashScopeImageApi;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import com.lucky.common.ai.domain.context.ImageContext;
import com.lucky.common.ai.enums.AiPlatformEnum;
import com.lucky.common.ai.strategy.ImageModelStrategy;
import com.lucky.common.core.utils.StringUtils;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptions;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 通义千问图片处理器
 *
 * @author lucky
 */
@Component
public class TongYiImageStrategy implements ImageModelStrategy {

    @Override
    public ImageModel buildImageModel(String baseUrl, String apiKey) {
        DashScopeImageApi.Builder builder = DashScopeImageApi.builder().apiKey(apiKey);
        if (StringUtils.isNotBlank(baseUrl)) {
            builder.baseUrl(baseUrl);
        }
        DashScopeImageApi dashScopeApi = builder.build();
        return DashScopeImageModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
    }

    @Override
    public ImageOptions buildImageOptions(ImageContext imageContext) {
        Map<String, String> options = imageContext.getOptions();
        return DashScopeImageOptions.builder()
                .model(imageContext.getModel()).n(1)
                .height(imageContext.getHeight()).width(imageContext.getWidth())
                .promptExtend(Boolean.parseBoolean(options.getOrDefault("promptExtend", "false")))
                .negativePrompt(options.getOrDefault("negativePrompt", ""))
                .enableInterleave(true)
                .build();
    }

    @Override
    public String getStrategyName() {
        return AiPlatformEnum.TONG_YI.getPlatform();
    }

}
