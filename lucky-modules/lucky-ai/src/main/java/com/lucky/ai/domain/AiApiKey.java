package com.lucky.ai.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lucky.common.mybatis.core.domain.BaseEntity;
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
@TableName("ai_api_key")
public class AiApiKey extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id")
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
    private String apiKey;

    /**
     * API 地址
     */
    private String url;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private String delFlag;

}
