package com.lucky.ai.core.context;

import com.lucky.ai.core.vo.image.ImageDrawRequest;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.AiImage;
import com.lucky.ai.domain.AiModel;
import lombok.Data;

/**
 * 图片生成任务上下文
 * 用于存储图片生成任务的上下文信息，如模型、API Key等
 *
 * @author lucky
 */
@Data
public class ImageContext {

    /**
     * 绘图请求参数
     */
    private ImageDrawRequest request;

    /**
     * 图片
     */
    private AiImage image;

    /**
     * 模型
     */
    private AiModel model;

    /**
     * API Key
     */
    private AiApiKey apiKey;

}
