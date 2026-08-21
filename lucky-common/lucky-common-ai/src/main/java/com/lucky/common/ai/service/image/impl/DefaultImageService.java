package com.lucky.common.ai.service.image.impl;

import cn.hutool.core.util.StrUtil;
import com.lucky.common.ai.domain.request.ImageRequest;
import com.lucky.common.ai.factory.ImageServiceFactory;
import com.lucky.common.ai.service.image.AbstractImageService;
import com.lucky.common.ai.service.image.ImagePersistenceHandler;
import com.lucky.common.ai.service.image.ImageService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.scheduling.annotation.Async;

/**
 * 默认图片服务（外观）
 * <p>
 * 负责编排图片生成流程（策略选择、请求构建、模型调用、文件上传）
 * 持久化逻辑委托给 {@link ImagePersistenceHandler}，由业务模块实现
 *
 * @author lucky
 */
@Slf4j
public class DefaultImageService implements ImageService {

    @Resource
    private ImageServiceFactory imageFactory;

    @Resource
    private ImagePersistenceHandler persistenceHandler;

    @Async
    @Override
    public void generateImage(ImageRequest imageRequest) {
        try {
            // 参数校验（options、url 允许为 null/空）
            this.validateImageRequest(imageRequest);
            // 获取图片模型策略
            AbstractImageService strategy = imageFactory.getOriginalService(imageRequest.getProvider());
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
            log.error("执行异步绘制图片失败, imageId={}, model={}, error={}", imageRequest.getImageId(), imageRequest.getModel(), ex.getMessage());
            // 持久化失败结果
            persistenceHandler.onFailure(imageRequest.getImageId(), ex.getMessage());
        }
    }

    /**
     * 校验图片生成请求参数
     * <p>String 类型参数校验非空字符（拦截 null、空串、纯空白），其余类型校验非 null;
     * 其中 options、url 允许为 null/空</p>
     *
     * @param imageRequest 图片生成请求
     */
    private void validateImageRequest(ImageRequest imageRequest) {
        if (imageRequest == null) {
            throw new IllegalArgumentException("图片生成请求参数不能为空");
        }
        if (StrUtil.isBlank(imageRequest.getPrompt())) {
            throw new IllegalArgumentException("提示词(prompt)不能为空");
        }
        if (imageRequest.getWidth() == null || imageRequest.getWidth() <= 0) {
            throw new IllegalArgumentException("图片宽度(width)不能为空并且必须大于0");
        }
        if (imageRequest.getHeight() == null || imageRequest.getHeight() <= 0) {
            throw new IllegalArgumentException("图片高度(height)不能为空并且必须大于0");
        }
        if (imageRequest.getImageId() == null) {
            throw new IllegalArgumentException("图片ID(imageId)不能为空");
        }
        if (StrUtil.isBlank(imageRequest.getModel())) {
            throw new IllegalArgumentException("模型(model)不能为空");
        }
        if (StrUtil.isBlank(imageRequest.getProvider())) {
            throw new IllegalArgumentException("提供商(provider)不能为空");
        }
        if (StrUtil.isBlank(imageRequest.getApiKey())) {
            throw new IllegalArgumentException("密钥(apiKey)不能为空");
        }
    }

}
