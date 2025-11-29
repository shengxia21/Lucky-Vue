package com.lucky.ai.controller.model.vo.model;

import com.lucky.ai.enums.CommonStatusEnum;
import com.lucky.ai.enums.model.AiModelTypeEnum;
import com.lucky.ai.enums.model.AiPlatformEnum;
import com.lucky.common.validation.InEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * AI 模型保存VO
 *
 * @author lucky
 */
public class AiModelSaveReqVO {

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKeyId() {
        return keyId;
    }

    public void setKeyId(Long keyId) {
        this.keyId = keyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    public Integer getMaxContexts() {
        return maxContexts;
    }

    public void setMaxContexts(Integer maxContexts) {
        this.maxContexts = maxContexts;
    }

}
