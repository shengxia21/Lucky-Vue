package com.lucky.ai.controller.model.vo.chatRole;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * AI 聊天角色响应VO
 *
 * @author lucky
 */
@Data
public class AiChatRoleRespVO {

    /**
     * 编号
     */
    private Long id;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 模型编号
     */
    private Long modelId;

    /**
     * 模型名字
     */
    private String modelName;

    /**
     * 模型标识
     */
    private String model;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色头像
     */
    private String avatar;

    /**
     * 角色类别
     */
    private String category;

    /**
     * 角色排序
     */
    private Integer sort;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 角色设定
     */
    private String systemMessage;

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
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}