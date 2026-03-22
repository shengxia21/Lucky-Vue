package com.lucky.ai.core.processor.image.model;

import com.lucky.ai.controller.image.vo.AiImageDrawReqVO;
import com.lucky.ai.core.processor.image.AbstractImageProcessor;
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
public class ZhiPuImageProcessor extends AbstractImageProcessor {

    @Override
    protected ImageModel buildImageModel(String apiKey) {
        ZhiPuAiImageApi zhiPuAiImageApi = new ZhiPuAiImageApi(apiKey);
        return new ZhiPuAiImageModel(zhiPuAiImageApi);
    }

    @Override
    protected ImageOptions buildImageOptions(AiModel model, AiImageDrawReqVO drawReqVO) {
        return ZhiPuAiImageOptions.builder()
                .model(model.getModel())
                .build();
    }

    @Override
    public String getProcessorName() {
        return AiPlatformEnum.ZHI_PU.getPlatform();
    }

}
