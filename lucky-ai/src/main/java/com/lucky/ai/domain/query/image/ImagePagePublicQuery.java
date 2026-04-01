package com.lucky.ai.domain.query.image;

import lombok.Data;

/**
 * 公开的绘图分页查询VO
 *
 * @author lucky
 */
@Data
public class ImagePagePublicQuery {

    /**
     * 提示词
     */
    private String prompt;

}