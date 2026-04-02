package com.lucky.ai.domain.vo.apikey;

import com.lucky.ai.domain.AiApiKey;
import com.lucky.common.annotation.Sensitive;
import com.lucky.common.enums.DesensitizedType;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

/**
 * AI API 密钥响应VO
 *
 * @author lucky
 */
@Data
@AutoMapper(target = AiApiKey.class)
public class ApiKeyVO {

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
    @Sensitive(desensitizedType = DesensitizedType.AI_API_KEY)
    private String apiKey;

    /**
     * 平台
     */
    private String platform;

    /**
     * 自定义 API 地址
     */
    private String url;

    /**
     * 状态
     */
    private Integer status;

}