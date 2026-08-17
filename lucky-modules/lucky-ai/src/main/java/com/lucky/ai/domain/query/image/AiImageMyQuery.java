package com.lucky.ai.domain.query.image;

import lombok.Data;

/**
 * AI 绘画分页【我的】请求对象
 *
 * @author lucky
 */
@Data
public class AiImageMyQuery {

    /**
     * 提示词
     */
    private String prompt;

    /**
     * 是否发布
     */
    private Boolean isPublic;

}
