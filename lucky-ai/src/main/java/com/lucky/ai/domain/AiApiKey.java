package com.lucky.ai.domain;

import com.lucky.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * AI API 秘钥对象 ai_api_key
 *
 * @author lucky
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AiApiKey extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 密钥
     */
    private String apiKey;

    /**
     * 平台
     */
    private String platform;

    /**
     * API 地址
     */
    private String url;

    /**
     * 状态（0开启 1关闭）
     */
    private Integer status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

}
