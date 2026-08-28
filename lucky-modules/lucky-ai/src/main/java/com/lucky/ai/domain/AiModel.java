package com.lucky.ai.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lucky.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * AI 模型对象 ai_model
 *
 * @author lucky
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_model")
public class AiModel extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id")
    private Long id;

    /**
     * API 秘钥编号
     */
    private Long keyId;

    /**
     * 模型名称
     */
    private String name;

    /**
     * 提供商
     */
    private String provider;

    /**
     * 模型标识
     */
    private String model;

    /**
     * 模型类型（1对话 2图片 3语音 4视频 5向量 6重排序）
     */
    private String type;

    /**
     * 是否支持联网搜索（Y是 N否），仅对话模型有值
     */
    private String enableSearch;

    /**
     * 是否支持多模态（Y是 N否），仅对话模型有值
     */
    private String enableMultimodal;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private String delFlag;

}
