package com.lucky.ai.domain.query.image;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * AI 绘画分页查询VO
 *
 * @author lucky
 */
@Data
public class ImagePageQuery {

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
    private Boolean publicStatus;

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