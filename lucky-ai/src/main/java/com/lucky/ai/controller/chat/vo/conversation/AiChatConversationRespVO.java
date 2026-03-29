package com.lucky.ai.controller.chat.vo.conversation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * AI 聊天对话响应VO
 *
 * @author lucky
 */
@Data
public class AiChatConversationRespVO {

    /**
     * 对话编号
     */
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
     * 是否置顶
     */
    private Boolean pinned;

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
     * 模型名字
     */
    private String modelName;

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
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 角色头像
     */
    private String roleAvatar;

    /**
     * 角色名字
     */
    private String roleName;

    /**
     * 消息数量
     */
    private Integer messageCount;

}