package com.lucky.common.ai.image.impl;

import com.lucky.common.ai.domain.request.ImageRequest;
import com.lucky.common.ai.factory.ImageServiceFactory;
import com.lucky.common.ai.image.ImagePersistenceHandler;
import com.lucky.common.ai.image.ImageService;
import com.lucky.common.ai.service.AbstractImageService;
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
 * <p>
 * 负责编排图片生成流程（策略选择、请求构建、模型调用、文件上传），
 * 持久化逻辑委托给 {@link ImagePersistenceHandler}，由业务模块实现，
 *
 * @author lucky
 */
@Slf4j
@Service
public class ImageServiceFacade implements ImageService {

    @Resource
    private ImageServiceFactory imageFactory;

    @Resource
    private ImagePersistenceHandler persistenceHandler;

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

            // 上传到文件服务
            String filePath = uploadImage(response);
            // 持久化成功结果
            persistenceHandler.onSuccess(imageRequest.getImageId(), filePath);
        } catch (Exception ex) {
            log.error("执行异步绘制图片失败, imageId={}, model={}", imageRequest.getImageId(), imageRequest.getModel());
            // 持久化失败结果
            persistenceHandler.onFailure(imageRequest.getImageId(), ex.getMessage());
        }
    }

}
