package com.lucky.common.ai.service.image;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.lucky.common.ai.domain.request.ImageRequest;
import com.lucky.common.core.config.LuckyConfig;
import com.lucky.common.core.exception.file.FileUploadException;
import com.lucky.common.core.utils.file.FileUtils;
import org.springframework.ai.image.ImageResponse;

import java.io.IOException;

/**
 * 图片服务接口
 *
 * @author lucky
 */
public interface ImageService {

    /**
     * 生成图片
     *
     * @param imageRequest 图片请求
     */
    void generateImage(ImageRequest imageRequest);

    /**
     * 上传图片到文件服务
     *
     * @param response 图片响应
     * @return 图片路径
     * @throws FileUploadException 上传失败
     */
    default String uploadImage(ImageResponse response) throws FileUploadException {
        try {
            // 上传到文件服务
            String b64Json = response.getResult().getOutput().getB64Json();
            String url = response.getResult().getOutput().getUrl();
            byte[] fileContent = StrUtil.isNotEmpty(b64Json) ? Base64.decode(b64Json) : HttpUtil.downloadBytes(url);
            return FileUtils.writeBytes(fileContent, LuckyConfig.getGenerateImagePath());
        } catch (IOException e) {
            throw new FileUploadException("上传图片到文件服务失败");
        }
    }

}
