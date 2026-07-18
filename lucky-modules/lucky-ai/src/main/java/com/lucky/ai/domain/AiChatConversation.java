package com.lucky.ai.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lucky.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

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
     * 编号
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
     * 角色头像
     */
    private String avatar;

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
     * 携带历史消息数
     */
    private Integer messageCount;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

}
