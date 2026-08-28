package com.lucky.ai.service.impl;

import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.AiImage;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.domain.query.image.ImageQuery;
import com.lucky.ai.mapper.AiImageMapper;
import com.lucky.ai.service.IAiApiKeyService;
import com.lucky.ai.service.IAiImageGenerateService;
import com.lucky.ai.service.IAiModelService;
import com.lucky.common.ai.domain.request.ImageRequest;
import com.lucky.common.ai.enums.ImageGenerateStatus;
import com.lucky.common.ai.service.image.ImageService;
import com.lucky.common.core.enums.YesNo;
import com.lucky.common.core.utils.MapstructUtils;
import com.lucky.common.security.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * AI 图片生成Service业务层处理
 *
 * @author lucky
 */
@Service
public class AiImageGenerateServiceImpl implements IAiImageGenerateService {

    @Resource
    private AiImageMapper imageMapper;

    @Resource
    private IAiModelService modelService;
    @Resource
    private IAiApiKeyService apiKeyService;

    @Resource
    private ImageService imageService;

    @Override
    public boolean generateImage(ImageQuery query) {
        // 校验模型是否有效
        AiModel model = modelService.validateModel(query.getModelId());
        // 校验apiKey是否有效
        AiApiKey apiKey = apiKeyService.validateApiKey(model.getKeyId());

        // 保存数据库
        AiImage image = MapstructUtils.convert(query, AiImage.class);
        image.setUserId(SecurityUtils.getUserId());
        image.setProvider(model.getProvider());
        image.setModel(model.getModel());
        image.setIsPublic(YesNo.NO.getCode());
        image.setGenerateStatus(ImageGenerateStatus.IN_PROGRESS.getCode());
        imageMapper.insert(image);

        // 构建图片请求
        ImageRequest imageRequest = new ImageRequest();
        imageRequest.setPrompt(query.getPrompt());
        imageRequest.setWidth(query.getWidth());
        imageRequest.setHeight(query.getHeight());
        imageRequest.setOptions(query.getOptions());
        imageRequest.setImageId(image.getId());
        imageRequest.setProvider(model.getProvider());
        imageRequest.setModel(model.getModel());
        imageRequest.setApiKey(apiKey.getApiKey());
        imageRequest.setUrl(apiKey.getUrl());
        // 调用图片生成服务
        imageService.generateImage(imageRequest);
        return true;
    }

}
