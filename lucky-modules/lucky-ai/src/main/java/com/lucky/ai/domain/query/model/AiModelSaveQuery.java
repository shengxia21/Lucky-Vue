package com.lucky.ai.domain.query.model;

import com.lucky.ai.domain.AiModel;
import com.lucky.common.ai.enums.AiModelTypeEnum;
import com.lucky.common.ai.enums.AiPlatformEnum;
import com.lucky.common.ai.enums.AiStatusEnum;
import com.lucky.common.core.enumeration.InEnum;
import com.lucky.common.core.validate.Update;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * AI 模型保存对象
 *
 * @author lucky
 */
@Data
@AutoMapper(target = AiModel.class, reverseConvertGenerate = false)
public class AiModelSaveQuery {

    /**
     * 编号
     */
    @NotNull(message = "编号不能为空", groups = {Update.class})
    private Long id;

    /**
     * API 秘钥编号
     */
    @NotNull(message = "API 秘钥编号不能为空")
    private Long keyId;

    /**
     * 模型名字
     */
    @NotEmpty(message = "模型名字不能为空")
    private String name;

    /**
     * 模型平台
     */
    @InEnum(AiPlatformEnum.class)
    @NotEmpty(message = "模型平台不能为空")
    private String platform;

    /**
     * 模型标识
     */
    @NotEmpty(message = "模型标识不能为空")
    private String model;

    /**
     * 模型类型
     */
    @InEnum(AiModelTypeEnum.class)
    @NotNull(message = "模型类型不能为空")
    private Integer type;

    /**
     * 状态
     */
    @InEnum(AiStatusEnum.class)
    @NotNull(message = "状态不能为空")
    private Integer status;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空")
    private Integer sort;

}