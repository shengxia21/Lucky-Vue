package com.lucky.ai.domain.vo.model;

import com.lucky.ai.domain.AiModel;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

/**
 * AI 模型响应VO
 *
 * @author lucky
 */
@Data
@AutoMapper(target = AiModel.class)
public class AiModelVO {

    /**
     * 编号
     */
    private Long id;

    /**
     * API 秘钥编号
     */
    private Long keyId;

    /**
     * 模型名字
     */
    private String name;

    /**
     * 服务提供商
     */
    private String provider;

    /**
     * 模型标识
     */
    private String model;

    /**
     * 模型类型
     */
    private Integer type;

    /**
     * 是否支持联网搜索（0否 1是），仅对话模型有值
     */
    private Boolean enableSearch;

    /**
     * 是否支持多模态（0否 1是），仅对话模型有值
     */
    private Boolean enableMultimodal;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 排序
     */
    private Integer sort;

}