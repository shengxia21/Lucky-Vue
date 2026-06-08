package com.lucky.ai.service.impl;

import com.lucky.ai.domain.AiImage;
import com.lucky.ai.mapper.AiImageMapper;
import com.lucky.common.ai.domain.request.ImageRequest;
import com.lucky.common.ai.enums.AiImageStatusEnum;
import com.lucky.common.ai.factory.ImageServiceFactory;
import com.lucky.common.ai.service.AbstractImageService;
import com.lucky.common.ai.service.ImageService;
import com.lucky.common.core.utils.DateUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 图片服务外观类
 *
 * @author lucky
 */
@Slf4j
@Service
public class ImageServiceFacade implements ImageService {

    @Resource
    private AiImageMapper imageMapper;

    @Resource
    private ImageServiceFactory imageFactory;

    @Async
    @Override
    public void generateImage(ImageRequest imageRequest) {
        try {
            // 获取图片模型策略
            AbstractImageService strategy = imageFactory.getOriginalService(imageRequest.getPlatform());
            // 构建请求选项
            ImageOptions imageOptions = strategy.buildImageOptions(imageRequest);
            // 构建 ImageModel
            ImageModel imageModel = strategy.buildImageModel(imageRequest.getUrl(), imageRequest.getApiKey());
            // 构建 Prompt
            ImagePrompt prompt = new ImagePrompt(imageRequest.getPrompt(), imageOptions);
            // 执行请求
            ImageResponse response = imageModel.call(prompt);
            if (response.getResult() == null) {
                String message = response.getMetadata().getRawMap().getOrDefault("message", "生成结果为空").toString();
                throw new IllegalArgumentException(message);
            }
            // 上传到文件服务
            String filePath = uploadImage(response);
            // 更新数据库
            AiImage aiImage = new AiImage();
            aiImage.setId(imageRequest.getImageId());
            aiImage.setStatus(AiImageStatusEnum.SUCCESS.getStatus());
            aiImage.setPicUrl(filePath);
            aiImage.setFinishTime(DateUtils.getNowDate());
            imageMapper.updateById(aiImage);
        } catch (Exception ex) {
            log.error("执行异步绘制图片失败, imageId={}, model={}", imageRequest.getImageId(), imageRequest.getModel());
            AiImage aiImage = new AiImage();
            aiImage.setId(imageRequest.getImageId());
            aiImage.setStatus(AiImageStatusEnum.FAIL.getStatus());
            aiImage.setErrorMessage(ex.getMessage());
            imageMapper.updateById(aiImage);
        }
    }

}
