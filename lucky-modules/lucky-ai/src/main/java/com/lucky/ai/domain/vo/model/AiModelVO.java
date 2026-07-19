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
     * 模型平台
     */
    private String platform;

    /**
     * 模型标识
     */
    private String model;

    /**
     * 模型类型
     */
    private Integer type;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

}