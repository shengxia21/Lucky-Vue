package com.lucky.ai.domain;

import com.lucky.common.annotation.Excel;
import com.lucky.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * AI 模型对象 ai_model
 * 
 * @author lucky
 */
public class AiModel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** API 秘钥编号 */
    @Excel(name = "API 秘钥编号")
    private Long keyId;

    /** 模型名称 */
    @Excel(name = "模型名称")
    private String name;

    /** 模型标志 */
    @Excel(name = "模型标志")
    private String model;

    /** 平台 */
    @Excel(name = "平台")
    private String platform;

    /** 模型类型（1对话 2图片 3语音 4视频 5向量 6重排序） */
    @Excel(name = "模型类型", readConverterExp = "1=对话,2=图片,3=语音,4=视频,5=向量,6=重排序")
    private Integer type;

    /** 状态（0开启 1关闭） */
    @Excel(name = "状态", readConverterExp = "0=开启,1=关闭")
    private Integer status;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sort;

    /** 温度参数 */
    @Excel(name = "温度参数")
    private Double temperature;

    /** 单条回复的最大 Token 数量 */
    @Excel(name = "单条回复的最大 Token 数量")
    private Integer maxTokens;

    /** 上下文的最大 Message 数量 */
    @Excel(name = "上下文的最大 Message 数量")
    private Integer maxContexts;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setKeyId(Long keyId) {
        this.keyId = keyId;
    }

    public Long getKeyId() {
        return keyId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatform() {
        return platform;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getSort() {
        return sort;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxContexts(Integer maxContexts) {
        this.maxContexts = maxContexts;
    }

    public Integer getMaxContexts() {
        return maxContexts;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("keyId", getKeyId())
            .append("name", getName())
            .append("model", getModel())
            .append("platform", getPlatform())
            .append("type", getType())
            .append("status", getStatus())
            .append("sort", getSort())
            .append("temperature", getTemperature())
            .append("maxTokens", getMaxTokens())
            .append("maxContexts", getMaxContexts())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }

}
