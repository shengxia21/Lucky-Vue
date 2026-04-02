package com.lucky.ai.domain.query.apiKey;

import lombok.Data;

/**
 * API 密钥分页查询VO
 *
 * @author lucky
 */
@Data
public class AiApiKeyPageQuery {

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

}