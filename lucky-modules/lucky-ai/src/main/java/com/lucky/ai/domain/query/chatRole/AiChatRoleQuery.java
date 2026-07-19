package com.lucky.ai.domain.query.chatRole;

import lombok.Data;

/**
 * AI 聊天角色分页请求对象
 *
 * @author lucky
 */
@Data
public class AiChatRoleQuery {

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 是否公开
     */
    private Boolean publicStatus;

}