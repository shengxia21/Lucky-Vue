package com.lucky.ai.controller.model.vo.model;

import java.util.Date;

/**
 * AI 模型响应VO
 *
 * @author lucky
 */
public class AiModelRespVO {

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

    public Long getId() {
        return id;
    }

    public AiModelRespVO setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getKeyId() {
        return keyId;
    }

    public AiModelRespVO setKeyId(Long keyId) {
        this.keyId = keyId;
        return this;
    }

    public String getName() {
        return name;
    }

    public AiModelRespVO setName(String name) {
        this.name = name;
        return this;
    }

    public String getModel() {
        return model;
    }

    public AiModelRespVO setModel(String model) {
        this.model = model;
        return this;
    }

    public String getPlatform() {
        return platform;
    }

    public AiModelRespVO setPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public AiModelRespVO setType(Integer type) {
        this.type = type;
        return this;
    }

    public Integer getSort() {
        return sort;
    }

    public AiModelRespVO setSort(Integer sort) {
        this.sort = sort;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public AiModelRespVO setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Double getTemperature() {
        return temperature;
    }

    public AiModelRespVO setTemperature(Double temperature) {
        this.temperature = temperature;
        return this;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public AiModelRespVO setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
        return this;
    }

    public Integer getMaxContexts() {
        return maxContexts;
    }

    public AiModelRespVO setMaxContexts(Integer maxContexts) {
        this.maxContexts = maxContexts;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public AiModelRespVO setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

}
