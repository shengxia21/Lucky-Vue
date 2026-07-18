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
     * 模型标识
     */
    private String model;

    /**
     * 模型平台
     */
    private String platform;

    /**
     * 状态
     */
    private Integer status;

}