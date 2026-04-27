package com.lucky.ai.domain.query.image;

import lombok.Data;

/**
 * 公开的绘图分页查询对象
 *
 * @author lucky
 */
@Data
public class AiImagePagePublicQuery {

    /**
     * 提示词
     */
    private String prompt;

}