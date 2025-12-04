package com.lucky.ai.controller.image.vo;

import jakarta.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * AI 绘画分页查询VO
 *
 * @author lucky
 */
public class AiImagePageReqVO {

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 平台
     */
    private String platform;

    /**
     * 提示词
     */
    private String prompt;

    /**
     * 绘画状态
     */
    private Integer status;

    /**
     * 是否发布
     */
    @NotNull(message = "发布状态不能为空")
    private Boolean publicStatus;

    /**
     * 查询参数（开始时间、结束时间）
     */
    private Map<String, Object> params;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getPublicStatus() {
        return publicStatus;
    }

    public void setPublicStatus(Boolean publicStatus) {
        this.publicStatus = publicStatus;
    }

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

}
