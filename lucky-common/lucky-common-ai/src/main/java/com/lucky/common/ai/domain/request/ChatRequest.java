package com.lucky.common.ai.domain.request;

import lombok.Data;

import java.util.List;

/**
 * 聊天请求
 * 用于存储聊天相关的请求信息，例如请求参数、会话信息、模型信息等
 *
 * @author lucky
 */
@Data
public class ChatRequest {

    // =======请求参数相关=======

    /**
     * 聊天内容
     */
    private String content;

    /**
     * 是否深度思考
     */
    private Boolean useThinking;

    /**
     * 是否联网搜索
     */
    private Boolean useSearch;

    /**
     * 附件 URL 数组
     */
    private List<String> attachmentUrls;

    // =======会话信息相关=======

    /**
     * ID
     */
    private Long conversationId;

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

    // =======模型信息相关=======

    /**
     * 模型标志
     */
    private String model;

    /**
     * 平台
     */
    private String platform;

    // =======api key相关=======

    /**
     * 密钥
     */
    private String apiKey;

    /**
     * API 地址
     */
    private String url;

    // =======用户信息相关(异步需要手动填充)=======

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 创建部门
     */
    private Long deptId;

    /**
     * 用户名称
     */
    private String userName;

}
