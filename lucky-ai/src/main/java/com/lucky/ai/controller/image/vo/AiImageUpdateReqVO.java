package com.lucky.ai.controller.image.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 更新绘画请求VO
 *
 * @author lucky
 */
@Data
public class AiImageUpdateReqVO {

    /**
     * 编号
     */
    @NotNull(message = "编号不能为空")
    private Long id;

    /**
     * 是否发布
     */
    private Boolean publicStatus;

}