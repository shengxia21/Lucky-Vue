package com.lucky.common.ai.service.image.impl.provider;

import com.alibaba.cloud.ai.dashscope.api.DashScopeImageApi;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import com.lucky.common.ai.domain.request.ImageRequest;
import com.lucky.common.ai.enums.Provider;
import com.lucky.common.ai.service.image.AbstractImageService;
import com.lucky.common.core.utils.StringUtils;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptions;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 通义千问策略
 *
 * @author lucky
 */
@Component
public class TongYiImageServiceImpl implements AbstractImageService {

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
    public ImageOptions buildImageOptions(ImageRequest imageRequest) {
        Map<String, String> options = imageRequest.getOptions();
        return DashScopeImageOptions.builder()
                .model(imageRequest.getModel()).n(1)
                .height(imageRequest.getHeight()).width(imageRequest.getWidth())
                .promptExtend(Boolean.parseBoolean(options.getOrDefault("promptExtend", "false")))
                .negativePrompt(options.getOrDefault("negativePrompt", ""))
                .enableInterleave(true)
                .build();
    }

    @Override
    public String getProviderName() {
        return Provider.TONG_YI.getCode();
    }

}
