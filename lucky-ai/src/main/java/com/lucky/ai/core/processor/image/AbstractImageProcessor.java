package com.lucky.ai.core.processor.image;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.lucky.ai.controller.image.vo.AiImageDrawReqVO;
import com.lucky.ai.core.context.ImageContext;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.AiImage;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.enums.image.AiImageStatusEnum;
import com.lucky.ai.mapper.AiImageMapper;
import com.lucky.common.config.LuckyConfig;
import com.lucky.common.exception.file.FileUploadException;
import com.lucky.common.utils.DateUtils;
import com.lucky.common.utils.file.FileUtils;
import com.lucky.common.utils.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;

import java.io.IOException;

/**
 * 抽象图片处理器
 * 提供通用的图片生成处理逻辑，子类只需实现特定平台的逻辑
 * [模板方法核心类]
 *
 * @author lucky
 */
public abstract class AbstractImageProcessor implements ImageProcessor {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 执行图片生成任务
     *
     * @param imageContext 图片上下文
     * @return 图片路径
     */
    @Override
    public void processImage(ImageContext imageContext) {
        AiImage image = imageContext.getImage();
        AiImageDrawReqVO drawReqVO = imageContext.getDrawReqVO();
        AiModel model = imageContext.getModel();
        AiApiKey apiKey = imageContext.getApiKey();

        AiImageMapper aiImageMapper = SpringUtils.getBean(AiImageMapper.class);

        try {
            // 构建请求选项
            ImageOptions request = buildImageOptions(model, drawReqVO);
            // 构建 ImageModel
            ImageModel imageModel = buildImageModel(apiKey.getApiKey());
            // 执行请求
            ImageResponse response = imageModel.call(new ImagePrompt(drawReqVO.getPrompt(), request));
            if (response.getResult() == null) {
                String message = response.getMetadata().getRawMap().getOrDefault("message", "生成结果为空").toString();
                throw new IllegalArgumentException(message);
            }

            // 上传到文件服务
            String filePath = uploadImage(response);

            // 更新数据库
            AiImage aiImage = new AiImage();
            aiImage.setId(image.getId());
            aiImage.setStatus(AiImageStatusEnum.SUCCESS.getStatus());
            aiImage.setPicUrl(filePath);
            aiImage.setFinishTime(DateUtils.getNowDate());
            aiImageMapper.updateAiImage(aiImage);
        } catch (Exception ex) {
            log.error("执行异步绘制图片失败, imageId={}, model={}", image.getId(), model.getModel());
            AiImage aiImage = new AiImage();
            aiImage.setId(image.getId());
            aiImage.setStatus(AiImageStatusEnum.FAIL.getStatus());
            aiImage.setErrorMessage(ex.getMessage());
            aiImageMapper.updateAiImage(aiImage);
        }
    }

    /**
     * 构建特定平台的 ImageModel
     *
     * @param apiKey API密钥
     * @return ImageModel 实例
     */
    protected abstract ImageModel buildImageModel(String apiKey);

    /**
     * 构建特定平台的 ImageOptions
     *
     * @param model     模型信息
     * @param drawReqVO 绘图请求参数
     * @return ImageOptions 实例
     */
    protected abstract ImageOptions buildImageOptions(AiModel model, AiImageDrawReqVO drawReqVO);

    /**
     * 上传图片到文件服务
     *
     * @param response 图片响应对象
     * @return 图片路径
     */
    protected String uploadImage(ImageResponse response) throws FileUploadException {
        try {
            // 上传到文件服务
            String b64Json = response.getResult().getOutput().getB64Json();
            String url = response.getResult().getOutput().getUrl();
            byte[] fileContent = StrUtil.isNotEmpty(b64Json) ? Base64.decode(b64Json) : HttpUtil.downloadBytes(url);
            return FileUtils.writeBytes(fileContent, LuckyConfig.getDrawImagePath());
        } catch (IOException e) {
            log.error("上传图片到文件服务失败, 错误信息={}", e.getMessage());
            throw new FileUploadException("上传图片到文件服务失败");
        }
    }

}
