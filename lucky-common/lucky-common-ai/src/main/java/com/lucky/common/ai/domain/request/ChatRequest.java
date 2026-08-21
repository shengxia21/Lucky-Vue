package com.lucky.common.ai.domain.request;

import lombok.Data;
import org.springframework.ai.chat.messages.Message;

import java.util.List;

/**
 * 聊天请求
 * 用于存储聊天相关的请求信息，例如请求参数、会话信息、模型信息等
 *
 * @author lucky
 */
@Data
public class ChatRequest {

    // =======原始请求参数相关=======

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
     * 携带历史消息数
     */
    private Integer historyMessageCount;

    // =======聊天角色相关=======

    /**
     * 角色设定
     */
    private String persona;

    // =======模型信息相关=======

    /**
     * 提供商
     */
    private String provider;

    /**
     * 模型标志
     */
    private String model;

    // =======api key相关=======

    /**
     * 密钥
     */
    private String apiKey;

    /**
     * API 地址
     */
    private String url;

    // =======LLM大模型调用相关=======

    /**
     * 消息列表
     */
    private List<Message> messages;

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
