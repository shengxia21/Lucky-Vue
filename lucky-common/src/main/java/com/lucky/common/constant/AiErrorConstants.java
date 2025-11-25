package com.lucky.common.constant;

/**
 * AI 错误码常量
 *
 * @author lucky
 */
public class AiErrorConstants {

    // ========== API 密钥 ==========
    /**
     * API 密钥不存在
     */
    public static final String API_KEY_NOT_EXISTS = "API 密钥不存在";

    /**
     * API 密钥已禁用
     */
    public static final String API_KEY_DISABLE = "API 密钥已禁用！";

    // ========== API 模型 ==========
    /**
     * 模型不存在
     */
    public static final String MODEL_NOT_EXISTS = "模型不存在!";

    /**
     * 模型({})已禁用
     */
    public static final String MODEL_DISABLE = "模型({})已禁用!";

    /**
     * 操作失败，找不到默认模型
     */
    public static final String MODEL_DEFAULT_NOT_EXISTS = "操作失败，找不到默认模型";

    /**
     * 操作失败，该模型的模型类型不正确
     */
    public static final String MODEL_USE_TYPE_ERROR = "操作失败，该模型的模型类型不正确";

    // ========== API 聊天角色 ==========
    /**
     * 聊天角色不存在
     */
    public static final String CHAT_ROLE_NOT_EXISTS = "聊天角色不存在";

    /**
     * 聊天角色({})已禁用
     */
    public static final String CHAT_ROLE_DISABLE = "聊天角色({})已禁用!";

    // ========== API 聊天会话 ==========
    /**
     * 对话不存在
     */
    public static final String CHAT_CONVERSATION_NOT_EXISTS = "对话不存在!";

    /**
     * 操作失败，该聊天模型的配置不完整
     */
    public static final String CHAT_CONVERSATION_MODEL_ERROR = "操作失败，该聊天模型的配置不完整";

    // ========== API 聊天消息 ==========
    /**
     * 消息不存在
     */
    public static final String CHAT_MESSAGE_NOT_EXIST = "消息不存在!";

    /**
     * 对话生成异常
     */
    public static final String CHAT_STREAM_ERROR = "对话生成异常!";

    // ========== API 绘画 ==========
    /**
     * 图片不存在
     */
    public static final String IMAGE_NOT_EXISTS = "图片不存在!";

    /**
     * Midjourney 提交失败!原因：{}
     */
    public static final String IMAGE_MIDJOURNEY_SUBMIT_FAIL = "Midjourney 提交失败!原因：{}";

    /**
     * Midjourney 按钮 customId 不存在! {}
     */
    public static final String IMAGE_CUSTOM_ID_NOT_EXISTS = "Midjourney 按钮 customId 不存在! {}";

    // ========== API 音乐 ==========
    /**
     * 音乐不存在
     */
    public static final String MUSIC_NOT_EXISTS = "音乐不存在!";

    // ========== API 写作 ==========
    /**
     * 作文不存在
     */
    public static final String WRITE_NOT_EXISTS = "作文不存在!";

    /**
     * 写作生成异常
     */
    public static final String WRITE_STREAM_ERROR = "写作生成异常!";

    // ========== API 思维导图 ==========
    /**
     * 思维导图不存在
     */
    public static final String MIND_MAP_NOT_EXISTS = "思维导图不存在!";

    // ========== API 知识库 ==========
    /**
     * 知识库不存在
     */
    public static final String KNOWLEDGE_NOT_EXISTS = "知识库不存在!";

    /**
     * 文档不存在
     */
    public static final String KNOWLEDGE_DOCUMENT_NOT_EXISTS = "文档不存在!";

    /**
     * 文档内容为空
     */
    public static final String KNOWLEDGE_DOCUMENT_FILE_EMPTY = "文档内容为空!";

    /**
     * 文件下载失败
     */
    public static final String KNOWLEDGE_DOCUMENT_FILE_DOWNLOAD_FAIL = "文件下载失败!";

    /**
     * 文档加载失败
     */
    public static final String KNOWLEDGE_DOCUMENT_FILE_READ_FAIL = "文档加载失败!";

    /**
     * 段落不存在
     */
    public static final String KNOWLEDGE_SEGMENT_NOT_EXISTS = "段落不存在!";

    /**
     * 内容 Token 数为 {}，超过最大限制 {}
     */
    public static final String KNOWLEDGE_SEGMENT_CONTENT_TOO_LONG = "内容 Token 数为 {}，超过最大限制 {}";

    // ========== AI 工具 ==========
    /**
     * 工具不存在
     */
    public static final String TOOL_NOT_EXISTS = "工具不存在";

    /**
     * 工具({})找不到 Bean
     */
    public static final String TOOL_NAME_NOT_EXISTS = "工具({})找不到 Bean";

    // ========== AI 工作流 ==========
    /**
     * 工作流不存在
     */
    public static final String WORKFLOW_NOT_EXISTS = "工作流不存在";

    /**
     * 工作流标识已存在
     */
    public static final String WORKFLOW_CODE_EXISTS = "工作流标识已存在";

}
