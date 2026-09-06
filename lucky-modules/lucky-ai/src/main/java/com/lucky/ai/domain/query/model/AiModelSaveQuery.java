package com.lucky.ai.domain.query.model;

import com.lucky.ai.domain.AiModel;
import com.lucky.ai.enums.ModelType;
import com.lucky.common.ai.enums.Provider;
import com.lucky.common.core.enumeration.InEnum;
import com.lucky.common.core.enums.DataStatus;
import com.lucky.common.core.enums.YesNo;
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
     * 服务提供商
     */
    @InEnum(Provider.class)
    @NotEmpty(message = "服务提供商不能为空")
    private String provider;

    /**
     * 模型标识
     */
    @NotEmpty(message = "模型标识不能为空")
    private String model;

    /**
     * 模型类型
     */
    @InEnum(ModelType.class)
    @NotEmpty(message = "模型类型不能为空")
    private String type;

    /**
     * 是否支持联网搜索（Y是 N否），仅对话模型有效
     */
    @InEnum(YesNo.class)
    private String enableSearch;

    /**
     * 是否支持多模态（Y是 N否），仅对话模型有效
     */
    @InEnum(YesNo.class)
    private String enableMultimodal;

    /**
     * 状态（0正常 1停用）
     */
    @InEnum(DataStatus.class)
    @NotNull(message = "状态不能为空")
    private String status;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空")
    private Integer sort;

}