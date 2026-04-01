package com.lucky.ai.domain.query.model;

import com.lucky.ai.enums.CommonStatusEnum;
import com.lucky.ai.enums.model.AiModelTypeEnum;
import com.lucky.ai.enums.model.AiPlatformEnum;
import com.lucky.common.validation.InEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * AI 模型保存VO
 *
 * @author lucky
 */
@Data
public class ModelSaveQuery {

    /**
     * 编号
     */
    private Long id;

    /**
     * API 秘钥编号
     */
    @NotNull(message = "API 秘钥编号不能为空")
    private Long keyId;

    /**
     * 模型名字
     */
    @NotEmpty(message = "模型名字不能为空")
    private String name;

    /**
     * 模型标识
     */
    @NotEmpty(message = "模型标识不能为空")
    private String model;

    /**
     * 模型平台
     */
    @InEnum(AiPlatformEnum.class)
    @NotEmpty(message = "模型平台不能为空")
    private String platform;

    /**
     * 模型类型
     */
    @InEnum(AiModelTypeEnum.class)
    @NotNull(message = "模型类型不能为空")
    private Integer type;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空")
    private Integer sort;

    /**
     * 状态
     */
    @InEnum(CommonStatusEnum.class)
    @NotNull(message = "状态不能为空")
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

}