package com.lucky.ai.domain.vo.model;

import com.lucky.ai.domain.AiModel;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.util.Date;

/**
 * AI 模型响应VO
 *
 * @author lucky
 */
@Data
@AutoMapper(target = AiModel.class)
public class ModelVO {

    /**
     * 编号
     */
    private Long id;

    /**
     * API 秘钥编号
     */
    private Long keyId;

    /**
     * 模型名字
     */
    private String name;

    /**
     * 模型标识
     */
    private String model;

    /**
     * 模型平台
     */
    private String platform;

    /**
     * 模型类型
     */
    private Integer type;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态
     */
    private Integer status;

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
     * 创建时间
     */
    private Date createTime;

}