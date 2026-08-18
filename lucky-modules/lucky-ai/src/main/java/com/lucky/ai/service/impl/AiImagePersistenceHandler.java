package com.lucky.ai.service.impl;

import com.lucky.ai.domain.AiImage;
import com.lucky.ai.mapper.AiImageMapper;
import com.lucky.common.ai.enums.ImageGenerateStatus;
import com.lucky.common.ai.service.image.ImagePersistenceHandler;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * AI 图片生成任务持久化处理器
 * <p>
 * 将图片生成结果更新到 ai_image 表
 *
 * @author lucky
 */
@Component
public class AiImagePersistenceHandler implements ImagePersistenceHandler {

    @Resource
    private AiImageMapper imageMapper;

    @Override
    public void onSuccess(Long imageId, String filePath) {
        AiImage aiImage = new AiImage();
        aiImage.setId(imageId);
        aiImage.setGenerateStatus(ImageGenerateStatus.SUCCESS.getCode());
        aiImage.setPicUrl(filePath);
        aiImage.setFinishTime(LocalDateTime.now());
        imageMapper.updateById(aiImage);
    }

    @Override
    public void onFailure(Long imageId, String errorMessage) {
        AiImage aiImage = new AiImage();
        aiImage.setId(imageId);
        aiImage.setGenerateStatus(ImageGenerateStatus.FAIL.getCode());
        aiImage.setErrorMessage(errorMessage);
        imageMapper.updateById(aiImage);
    }

}
