package com.lucky.ai.domain.query.model;

import lombok.Data;

/**
 * AI 模型分页查询请求对象
 *
 * @author lucky
 */
@Data
public class AiModelQuery {

    /**
     * 模型名称
     */
    private String name;

    /**
     * 模型提供商
     */
    private String provider;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

}