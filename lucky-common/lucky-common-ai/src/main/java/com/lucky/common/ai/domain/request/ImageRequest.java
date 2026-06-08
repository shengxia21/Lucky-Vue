package com.lucky.common.ai.domain.request;

import lombok.Data;

import java.util.Map;

/**
 * 图片生成任务请求
 * 用于存储图片生成任务的请求信息，如模型、API Key等
 *
 * @author lucky
 */
@Data
public class ImageRequest {

    // =======请求参数相关=======

    /**
     * 提示词
     */
    private String prompt;

    /**
     * 图片宽度
     */
    private Integer width;

    /**
     * 图片高度
     */
    private Integer height;

    /**
     * 绘制参数，不同 platform 的不同参数
     */
    private Map<String, String> options;

    // =======图片信息相关=======

    /**
     * 图片ID
     */
    private Long imageId;

    // =======模型信息相关=======

    /**
     * 模型标志
     */
    private String model;

    /**
     * 平台
     */
    private String platform;

    // =======绘图请求参数相关=======

    /**
     * API 地址
     */
    private String url;

    /**
     * 密钥
     */
    private String apiKey;

}
