package com.lucky.ai.service.impl;

import com.lucky.ai.domain.AiImage;
import com.lucky.ai.enums.GenerateStatus;
import com.lucky.ai.mapper.AiImageMapper;
import com.lucky.common.ai.service.image.ImagePersistenceHandler;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 数据库图片持久化处理器
 *
 * @author lucky
 */
@Component
public class DBImagePersistenceHandler implements ImagePersistenceHandler {

    @Resource
    private AiImageMapper imageMapper;

    @Override
    public void onSuccess(Long imageId, String filePath) {
        AiImage aiImage = new AiImage();
        aiImage.setId(imageId);
        aiImage.setGenerateStatus(GenerateStatus.SUCCESS.getCode());
        aiImage.setPicUrl(filePath);
        aiImage.setFinishTime(LocalDateTime.now());
        imageMapper.updateById(aiImage);
    }

    @Override
    public void onFailure(Long imageId, String errorMessage) {
        AiImage aiImage = new AiImage();
        aiImage.setId(imageId);
        aiImage.setGenerateStatus(GenerateStatus.FAIL.getCode());
        aiImage.setErrorMessage(errorMessage);
        imageMapper.updateById(aiImage);
    }

}
