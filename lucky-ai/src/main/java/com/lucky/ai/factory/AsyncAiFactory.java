package com.lucky.ai.factory;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.lucky.ai.controller.image.vo.AiImageDrawReqVO;
import com.lucky.ai.domain.AiImage;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.enums.image.AiImageStatusEnum;
import com.lucky.ai.mapper.AiImageMapper;
import com.lucky.ai.service.IAiModelService;
import com.lucky.common.config.LuckyConfig;
import com.lucky.common.utils.DateUtils;
import com.lucky.common.utils.SecurityUtils;
import com.lucky.common.utils.file.FileUtils;
import com.lucky.common.utils.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;

import static com.lucky.ai.service.impl.AiImageServiceImpl.buildImageOptions;

/**
 * 异步工厂（产生任务用）
 *
 * @author lucky
 */
public class AsyncAiFactory {

    private static final Logger ai_logger = LoggerFactory.getLogger("ai-large-model");

    /**
     * 执行绘制图片任务
     *
     * @param image 图片对象
     * @param reqVO 图片绘制请求参数
     * @param model 模型对象
     * @return 任务
     */
    public static Runnable executeDrawImage(final AiImage image, final AiImageDrawReqVO reqVO, final AiModel model) {
        return () -> {
            AiImageMapper aiImageMapper = SpringUtils.getBean(AiImageMapper.class);
            IAiModelService aiModelService = SpringUtils.getBean(IAiModelService.class);
            try {
                // 1.1 构建请求
                ImageOptions request = buildImageOptions(reqVO, model);
                // 1.2 执行请求
                ImageModel imageModel = aiModelService.getImageModel(model.getId());
                ImageResponse response = imageModel.call(new ImagePrompt(reqVO.getPrompt(), request));
                if (response.getResult() == null) {
                    AiImage aiImage = new AiImage();
                    aiImage.setId(image.getId());
                    aiImage.setTaskId(response.getMetadata().getRawMap().get("taskId").toString());
                    aiImageMapper.updateAiImage(aiImage);
                    throw new IllegalArgumentException("生成结果为空");
                }

                // 2. 上传到文件服务
                String b64Json = response.getResult().getOutput().getB64Json();
                byte[] fileContent = StrUtil.isNotEmpty(b64Json) ? Base64.decode(b64Json)
                        : HttpUtil.downloadBytes(response.getResult().getOutput().getUrl());
                String filePath = FileUtils.writeBytes(fileContent, LuckyConfig.getDrawImagePath());

                // 3. 更新数据库
                AiImage aiImage = new AiImage();
                aiImage.setId(image.getId());
                aiImage.setStatus(AiImageStatusEnum.SUCCESS.getStatus());
                aiImage.setPicUrl(filePath);
                aiImage.setFinishTime(DateUtils.getNowDate());
                aiImageMapper.updateAiImage(aiImage);
            } catch (Exception ex) {
                ai_logger.error("执行异步绘制图片失败, imageId={}, modelId={}", image.getId(), model.getId(), ex);
                AiImage aiImage = new AiImage();
                aiImage.setId(image.getId());
                aiImage.setStatus(AiImageStatusEnum.FAIL.getStatus());
                aiImage.setErrorMessage(ex.getMessage());
                aiImage.setFinishTime(DateUtils.getNowDate());
                aiImage.setUpdateBy(SecurityUtils.getUsername());
                aiImage.setUpdateTime(DateUtils.getNowDate());
                aiImageMapper.updateAiImage(aiImage);
            }
        };
    }

}
