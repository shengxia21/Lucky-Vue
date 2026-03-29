package com.lucky.ai.controller.image.vo;

import lombok.Data;

/**
 * 公开的绘图分页查询VO
 *
 * @author lucky
 */
@Data
public class AiImagePublicPageReqVO {

    /**
     * 提示词
     */
    private String prompt;

}