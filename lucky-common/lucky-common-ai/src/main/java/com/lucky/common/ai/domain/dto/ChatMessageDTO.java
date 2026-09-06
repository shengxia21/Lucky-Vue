package com.lucky.common.ai.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 聊天消息DTO
 *
 * @author lucky
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 对话编号
     */
    private Long conversationId;

    /**
     * 提供商
     */
    private String provider;

    /**
     * 模型标志
     */
    private String model;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 系统消息
     */
    private String systemMessage;

    /**
     * 聊天内容
     */
    private String content;

    /**
     * 推理内容
     */
    private String reasoningContent;

    /**
     * 提示词 Token 数量
     */
    private Integer promptTokens;

    /**
     * 生成 Token 数量
     */
    private Integer completionTokens;

    /**
     * 总 Token 数量
     */
    private Integer totalTokens;

    /**
     * 附件 URL 数组
     */
    private List<String> attachmentUrls;

    // ======= 用户信息(异步执行，参数传递) =======

    /**
     * 创建部门
     */
    private Long deptId;

    /**
     * 用户名称
     */
    private String userName;

}
