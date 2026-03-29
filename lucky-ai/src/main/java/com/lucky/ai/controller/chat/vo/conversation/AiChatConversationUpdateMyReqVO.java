package com.lucky.ai.controller.chat.vo.conversation;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 更新我的聊天对话请求VO
 *
 * @author lucky
 */
@Data
public class AiChatConversationUpdateMyReqVO {

    /**
     * 对话编号
     */
    @NotNull(message = "对话编号不能为空")
    private Long id;

    /**
     * 对话标题
     */
    private String title;

    /**
     * 是否置顶
     */
    private Boolean pinned;

    /**
     * 模型编号
     */
    private Long modelId;

    /**
     * 知识库编号
     */
    private Long knowledgeId;

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

}