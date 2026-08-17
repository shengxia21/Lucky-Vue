package com.lucky.ai.domain.query.image;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * AI 绘画分页查询对象
 *
 * @author lucky
 */
@Data
public class AiImageQuery {

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 平台
     */
    private String platform;

    /**
     * 绘画状态
     */
    private Integer generateStatus;

    /**
     * 是否发布
     */
    private Boolean isPublic;

    /**
     * 查询参数（开始时间、结束时间）
     */
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

}