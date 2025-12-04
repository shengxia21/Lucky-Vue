package com.lucky.ai.controller.image.vo;

import jakarta.validation.constraints.NotNull;

/**
 * 更新绘画请求VO
 *
 * @author lucky
 */
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getPublicStatus() {
        return publicStatus;
    }

    public void setPublicStatus(Boolean publicStatus) {
        this.publicStatus = publicStatus;
    }

}
