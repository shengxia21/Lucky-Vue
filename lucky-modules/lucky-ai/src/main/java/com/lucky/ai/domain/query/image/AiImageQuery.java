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
     * 提供商
     */
    private String provider;

    /**
     * 生成状态（1进行中 2已完成 3已失败）
     */
    private String generateStatus;

    /**
     * 是否发布（Y是 N否）
     */
    private String isPublic;

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
