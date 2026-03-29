package com.lucky.ai.controller.image.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * AI 绘画响应VO
 *
 * @author lucky
 */
@Data
public class AiImageRespVO {

    /**
     * 编号
     */
    private Long id;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 平台（参见 AiPlatformEnum 枚举）
     */
    private String platform;

    /**
     * 模型
     */
    private String model;

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
     * 绘画状态
     */
    private Integer status;

    /**
     * 是否发布
     */
    private Boolean publicStatus;

    /**
     * 图片地址
     */
    private String picUrl;

    /**
     * 绘画错误信息
     */
    private String errorMessage;

    /**
     * 绘制参数
     */
    private Map<String, String> options;

    /**
     * 完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}