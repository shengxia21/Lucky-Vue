package com.lucky.ai.controller.model.vo.apikey;

import lombok.Data;

/**
 * API 密钥分页查询VO
 *
 * @author lucky
 */
@Data
public class AiApiKeyPageReqVO {

    /**
     * 名称
     */
    private String name;

    /**
     * 平台
     */
    private String platform;

    /**
     * 状态
     */
    private Integer status;

}