package com.lucky.ai.domain.query.apiKey;

import lombok.Data;

/**
 * API 密钥分页查询对象
 *
 * @author lucky
 */
@Data
public class AiApiKeyQuery {

    /**
     * 名称
     */
    private String name;

    /**
     * 提供商
     */
    private String provider;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

}