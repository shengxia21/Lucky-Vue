package com.lucky.ai.domain.query.apiKey;

import com.lucky.ai.domain.AiApiKey;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * AI API 密钥新增/修改 请求VO
 *
 * @author lucky
 */
@Data
@AutoMapper(target = AiApiKey.class, reverseConvertGenerate = false)
public class AiApiKeySaveQuery {

    /**
     * 编号
     */
    private Long id;

    /**
     * 名称
     */
    @NotEmpty(message = "名称不能为空")
    private String name;

    /**
     * 密钥
     */
    @NotEmpty(message = "密钥不能为空")
    private String apiKey;

    /**
     * 平台
     */
    @NotEmpty(message = "平台不能为空")
    private String platform;

    /**
     * 自定义 API 地址
     */
    private String url;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空")
    private Integer status;

}