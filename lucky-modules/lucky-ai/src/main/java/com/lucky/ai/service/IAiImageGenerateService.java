package com.lucky.ai.service;

import com.lucky.ai.domain.query.image.ImageQuery;

/**
 * AI 图片生成Service接口
 *
 * @author lucky
 */
public interface IAiImageGenerateService {

    /**
     * 生成图片
     *
     * @param query 图片参数
     * @return 结果
     */
    boolean generateImage(ImageQuery query);

}
