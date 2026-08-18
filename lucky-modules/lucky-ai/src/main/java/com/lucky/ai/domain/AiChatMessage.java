package com.lucky.ai.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.lucky.ai.domain.search.WebSearchResponse;
import com.lucky.common.mybatis.core.domain.BaseEntity;
import com.lucky.common.mybatis.handler.type.LongListTypeHandler;
import com.lucky.common.mybatis.handler.type.StringListTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
 * AI 聊天消息对象 ai_chat_message
 *
 * @author lucky
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_chat_message")
public class AiChatMessage extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 对话编号
     */
    private Long conversationId;

    /**
     * 平台
     */
    private String platform;

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
     * 知识库段落编号数组
     */
    @TableField(typeHandler = LongListTypeHandler.class)
    private List<Long> segmentIds;

    /**
     * 联网搜索的网页内容数组
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<WebSearchResponse.WebPage> webSearchPages;

    /**
     * 附件 URL 数组
     */
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> attachmentUrls;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private String delFlag;

}
