package com.lucky.ai.domain.vo.chatRole;

import com.lucky.ai.domain.AiChatRole;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AI 聊天角色响应VO
 *
 * @author lucky
 */
@Data
@AutoMapper(target = AiChatRole.class)
public class AiChatRoleVO {

    /**
     * 编号
     */
    private Long id;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色头像
     */
    private String avatar;

    /**
     * 角色设定
     */
    private String persona;

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
    private Boolean isPublic;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}