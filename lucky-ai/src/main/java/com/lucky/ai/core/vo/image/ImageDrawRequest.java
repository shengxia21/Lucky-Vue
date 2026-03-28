package com.lucky.ai.core.vo.image;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 生成图片请求
 *
 * @author lucky
 */
@Data
public class ImageDrawRequest {

    /**
     * 模型编号
     */
    @NotNull(message = "模型编号不能为空")
    private Long modelId;

    /**
     * 提示词
     */
    @NotEmpty(message = "提示词不能为空")
    @Size(max = 1200, message = "提示词最大 1200")
    private String prompt;

    /**
     * 图片高度
     */
    @NotNull(message = "图片高度不能为空")
    private Integer height;

    /**
     * 图片宽度
     */
    @NotNull(message = "图片宽度不能为空")
    private Integer width;

    // ========== 各平台绘画的拓展参数 ==========

    /**
     * 绘制参数，不同 platform 的不同参数
     */
    private Map<String, String> options;

    public Map<String, String> getOptions() {
        if (options == null) {
            options = new HashMap<>();
        }
        return options;
    }

}
