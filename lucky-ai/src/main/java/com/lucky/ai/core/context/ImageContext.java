package com.lucky.ai.core.context;

import com.lucky.ai.controller.image.vo.AiImageDrawReqVO;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.AiImage;
import com.lucky.ai.domain.AiModel;

/**
 * 图片生成任务上下文
 * 用于存储图片生成任务的上下文信息，如模型、API Key等
 *
 * @author lucky
 */
public class ImageContext {

    /**
     * 绘图请求参数
     */
    private AiImageDrawReqVO drawReqVO;

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

    public AiImageDrawReqVO getDrawReqVO() {
        return drawReqVO;
    }

    public void setDrawReqVO(AiImageDrawReqVO drawReqVO) {
        this.drawReqVO = drawReqVO;
    }

    public AiImage getImage() {
        return image;
    }

    public void setImage(AiImage image) {
        this.image = image;
    }

    public AiModel getModel() {
        return model;
    }

    public void setModel(AiModel model) {
        this.model = model;
    }

    public AiApiKey getApiKey() {
        return apiKey;
    }

    public void setApiKey(AiApiKey apiKey) {
        this.apiKey = apiKey;
    }

}
