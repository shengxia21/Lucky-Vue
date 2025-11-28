package com.lucky.ai.controller.model.vo.apikey;

import com.lucky.common.annotation.Sensitive;
import com.lucky.common.enums.DesensitizedType;

/**
 * AI API 密钥响应VO
 *
 * @author lucky
 */
public class AiApiKeyRespVO {

    /**
     * 编号
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 密钥
     */
    @Sensitive(desensitizedType = DesensitizedType.AI_API_KEY)
    private String apiKey;

    /**
     * 平台
     */
    private String platform;

    /**
     * 自定义 API 地址
     */
    private String url;

    /**
     * 状态
     */
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
