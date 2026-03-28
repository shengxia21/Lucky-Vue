package com.lucky.ai.domain;

import com.lucky.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
 * AI 聊天角色 ai_chat_role
 *
 * @author lucky
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AiChatRole extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色头像
     */
    private String avatar;

    /**
     * 角色分类
     */
    private String category;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 角色设定
     */
    private String systemMessage;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 模型编号
     */
    private Long modelId;

    /**
     * 引用的知识库编号列表
     */
    private List<Long> knowledgeIds;

    /**
     * 引用的工具编号列表
     */
    private List<Long> toolIds;

    /**
     * 引用的 MCP Client 名字列表
     */
    private List<String> mcpClientNames;

    /**
     * 是否公开
     */
    private Boolean publicStatus;

    /**
     * 排序值
     */
    private Integer sort;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

}
