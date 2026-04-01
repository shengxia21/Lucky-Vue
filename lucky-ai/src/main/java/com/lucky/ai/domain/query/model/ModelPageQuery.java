package com.lucky.ai.domain.query.model;

import lombok.Data;

/**
 * AI 模型分页查询请求VO
 *
 * @author lucky
 */
@Data
public class ModelPageQuery {

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

}