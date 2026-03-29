package com.lucky.ai.controller.model.vo.chatRole;

import lombok.Data;

/**
 * AI 聊天角色分页请求VO
 *
 * @author lucky
 */
@Data
public class AiChatRolePageReqVO {

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色类别
     */
    private String category;

    /**
     * 是否公开
     */
    private Boolean publicStatus;

}