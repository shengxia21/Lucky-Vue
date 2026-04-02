package com.lucky.ai.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lucky.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;
import java.util.Map;

/**
 * AI 绘画对象 ai_image
 *
 * @author lucky
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_image")
public class AiImage extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 提示词
     */
    private String prompt;

    /**
     * 平台
     */
    private String platform;

    /**
     * 模型编号
     */
    private Long modelId;

    /**
     * 模型标识
     */
    private String model;

    /**
     * 图片宽度
     */
    private Integer width;

    /**
     * 图片高度
     */
    private Integer height;

    /**
     * 生成状态（10进行中 20已完成 30已失败）
     */
    private Integer status;

    /**
     * 完成时间
     */
    private Date finishTime;

    /**
     * 绘画错误信息
     */
    private String errorMessage;

    /**
     * 图片地址
     */
    private String picUrl;

    /**
     * 是否公开（0否 1是）
     */
    private Boolean publicStatus;

    /**
     * 绘制参数
     */
    private Map<String, Object> options;

    /**
     * 任务编号
     */
    private String taskId;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

}
