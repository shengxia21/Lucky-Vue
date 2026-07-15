package com.lucky.common.ai.service.image.impl.provider;

import com.lucky.common.ai.domain.request.ImageRequest;
import com.lucky.common.ai.enums.AiPlatformEnum;
import com.lucky.common.ai.service.image.AbstractImageService;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.zhipuai.ZhiPuAiImageModel;
import org.springframework.ai.zhipuai.ZhiPuAiImageOptions;
import org.springframework.ai.zhipuai.api.ZhiPuAiImageApi;
import org.springframework.stereotype.Component;

/**
 * 智普策略
 *
 * @author lucky
 */
@Component
public class ZhiPuImageServiceImpl implements AbstractImageService {

    @Override
    public ImageModel buildImageModel(String baseUrl, String apiKey) {
        ZhiPuAiImageApi zhiPuAiImageApi = new ZhiPuAiImageApi(apiKey);
        return new ZhiPuAiImageModel(zhiPuAiImageApi);
    }

    @Override
    public ImageOptions buildImageOptions(ImageRequest imageRequest) {
        return ZhiPuAiImageOptions.builder()
                .model(imageRequest.getModel())
                .build();
    }

    @Override
    public String getProviderName() {
        return AiPlatformEnum.ZHI_PU.getPlatform();
    }

}
