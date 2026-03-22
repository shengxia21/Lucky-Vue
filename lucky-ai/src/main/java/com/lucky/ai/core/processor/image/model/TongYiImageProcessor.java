package com.lucky.ai.core.processor.image.model;

import com.alibaba.cloud.ai.dashscope.api.DashScopeImageApi;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import com.lucky.ai.controller.image.vo.AiImageDrawReqVO;
import com.lucky.ai.core.processor.image.AbstractImageProcessor;
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
public class TongYiImageProcessor extends AbstractImageProcessor {

    @Override
    protected ImageModel buildImageModel(String apiKey) {
        DashScopeImageApi dashScopeApi = DashScopeImageApi.builder()
                .apiKey(apiKey)
                .build();
        return DashScopeImageModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
    }

    @Override
    protected ImageOptions buildImageOptions(AiModel model, AiImageDrawReqVO drawReqVO) {
        return DashScopeImageOptions.builder()
                .model(model.getModel()).n(1)
                .height(drawReqVO.getHeight()).width(drawReqVO.getWidth())
                .promptExtend(Boolean.parseBoolean(drawReqVO.getOptions().getOrDefault("promptExtend", "false")))
                .negativePrompt(drawReqVO.getOptions().getOrDefault("negativePrompt", ""))
                .enableInterleave(true)
                .build();
    }

    @Override
    public String getProcessorName() {
        return AiPlatformEnum.TONG_YI.getPlatform();
    }

}
