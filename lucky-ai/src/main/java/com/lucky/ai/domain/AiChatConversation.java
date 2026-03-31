package com.lucky.ai.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lucky.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * AI 聊天对话对象 ai_chat_conversation
 *
 * @author lucky
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_chat_conversation")
public class AiChatConversation extends BaseEntity {

    public static final String TITLE_DEFAULT = "新对话";

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID 编号，自增
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 对话标题
     */
    private String title;

    /**
     * 是否置顶（0否 1是）
     */
    private Boolean pinned;

    /**
     * 置顶时间
     */
    private Date pinnedTime;

    /**
     * 角色编号
     */
    private Long roleId;

    /**
     * 模型编号
     */
    private Long modelId;

    /**
     * 模型标志
     */
    private String model;

    /**
     * 角色设定
     */
    private String systemMessage;

    /**
     * 温度参数
     */
    private Double temperature;

    /**
     * 单条回复的最大 Token 数量
     */
    private Integer maxTokens;

    /**
     * 上下文的最大 Message 数量
     */
    private Integer maxContexts;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 备注
     */
    @TableField(exist = false)
    private String remark;

}
