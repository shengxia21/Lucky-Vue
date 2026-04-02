package com.lucky.ai.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lucky.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * AI 模型对象 ai_model
 *
 * @author lucky
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_model")
public class AiModel extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id")
    private Long id;

    /**
     * API 秘钥编号
     */
    private Long keyId;

    /**
     * 模型名称
     */
    private String name;

    /**
     * 模型标志
     */
    private String model;

    /**
     * 平台
     */
    private String platform;

    /**
     * 模型类型（1对话 2图片 3语音 4视频 5向量 6重排序）
     */
    private Integer type;

    /**
     * 状态（0开启 1关闭）
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 温度参数
     */
    private Double temperature;

    /**
     * 单条回复的最大 Token 数量
     */
    private Integer maxTokens;

    /**
     * 上下文的最大 Message 数量
     */
    private Integer maxContexts;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

}
