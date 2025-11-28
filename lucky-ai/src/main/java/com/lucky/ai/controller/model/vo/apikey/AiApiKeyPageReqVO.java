package com.lucky.ai.controller.model.vo.apikey;

/**
 * API 密钥分页查询VO
 *
 * @author lucky
 */
public class AiApiKeyPageReqVO {

    /**
     * 名称
     */
    private String name;

    /**
     * 平台
     */
    private String platform;

    /**
     * 状态
     */
    private Integer status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
