package com.lucky.ai.core.facade;

import com.lucky.ai.core.ImageService;
import com.lucky.ai.core.context.ImageContext;
import com.lucky.ai.core.strategy.ImageModelStrategy;
import com.lucky.ai.domain.AiImage;
import com.lucky.ai.enums.image.AiImageStatusEnum;
import com.lucky.ai.factory.ImageModelFactory;
import com.lucky.ai.mapper.AiImageMapper;
import com.lucky.common.utils.DateUtils;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.stereotype.Service;

/**
 * 图片服务外观类
 *
 * @author lucky
 */
@Service
public class ImageServiceFacade implements ImageService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private AiImageMapper aiImageMapper;

    @Resource
    private ImageModelFactory imageFactory;

    @Override
    public void generateImage(ImageContext imageContext) {
        try {
            // 获取图片模型策略
            ImageModelStrategy strategy = imageFactory.getOriginalStrategy(imageContext.getModel().getPlatform());
            // 构建请求选项
            ImageOptions imageOptions = strategy.buildImageOptions(imageContext);
            // 构建 ImageModel
            ImageModel imageModel = strategy.buildImageModel(imageContext.getApiKey().getApiKey());
            // 构建 Prompt
            ImagePrompt prompt = new ImagePrompt(imageContext.getRequest().getPrompt(), imageOptions);
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
            aiImage.setId(imageContext.getImage().getId());
            aiImage.setStatus(AiImageStatusEnum.SUCCESS.getStatus());
            aiImage.setPicUrl(filePath);
            aiImage.setFinishTime(DateUtils.getNowDate());
            aiImageMapper.updateAiImage(aiImage);
        } catch (Exception ex) {
            log.error("执行异步绘制图片失败, imageId={}, model={}", imageContext.getImage().getId(), imageContext.getModel().getModel());
            AiImage aiImage = new AiImage();
            aiImage.setId(imageContext.getImage().getId());
            aiImage.setStatus(AiImageStatusEnum.FAIL.getStatus());
            aiImage.setErrorMessage(ex.getMessage());
            aiImageMapper.updateAiImage(aiImage);
        }
    }

}
