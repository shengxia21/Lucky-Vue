package com.lucky.ai.domain.query.chatRole;

import lombok.Data;

/**
 * AI 聊天角色分页【我的】请求对象
 *
 * @author lucky
 */
@Data
public class AiChatRoleMyQuery {

    /**
     * 角色名称
     */
    private String name;

    /**
     * 是否公开（Y是 N否）
     */
    private String isPublic;

}
