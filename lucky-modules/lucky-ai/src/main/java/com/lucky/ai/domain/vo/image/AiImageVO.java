package com.lucky.ai.domain.vo.image;

import com.lucky.ai.domain.AiImage;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * AI 绘画响应VO
 *
 * @author lucky
 */
@Data
@AutoMapper(target = AiImage.class)
public class AiImageVO {

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
     * 图片地址
     */
    private String picUrl;

    /**
     * 完成时间
     */
    private LocalDateTime finishTime;

    /**
     * 绘制参数
     */
    private Map<String, Object> options;

    /**
     * 绘画错误信息
     */
    private String errorMessage;

    /**
     * 是否发布
     */
    private Boolean publicStatus;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}