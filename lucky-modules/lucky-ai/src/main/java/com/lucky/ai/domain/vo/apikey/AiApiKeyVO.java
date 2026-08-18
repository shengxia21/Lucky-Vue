package com.lucky.ai.domain.vo.apikey;

import com.lucky.ai.domain.AiApiKey;
import com.lucky.common.core.enums.DesensitizedType;
import com.lucky.common.security.annotation.Sensitive;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

/**
 * AI API 密钥响应VO
 *
 * @author lucky
 */
@Data
@AutoMapper(target = AiApiKey.class)
public class AiApiKeyVO {

    /**
     * 编号
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 提供商
     */
    private String provider;

    /**
     * 密钥
     */
    @Sensitive(desensitizedType = DesensitizedType.API_KEY)
    private String apiKey;

    /**
     * 自定义 API 地址
     */
    private String url;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

}