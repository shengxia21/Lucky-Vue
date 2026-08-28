package com.lucky.ai.domain.query.image;

import com.lucky.ai.domain.AiImage;
import com.lucky.common.core.enumeration.InEnum;
import com.lucky.common.core.enums.YesNo;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 更新绘画请求对象
 *
 * @author lucky
 */
@Data
@AutoMapper(target = AiImage.class, reverseConvertGenerate = false)
public class AiImageUpdateQuery {

    /**
     * 编号
     */
    @NotNull(message = "编号不能为空")
    private Long id;

    /**
     * 是否发布（Y是 N否）
     */
    @InEnum(YesNo.class)
    private String isPublic;

}
