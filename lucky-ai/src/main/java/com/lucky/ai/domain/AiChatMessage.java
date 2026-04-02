package com.lucky.ai.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lucky.ai.core.vo.search.WebSearchResponse;
import com.lucky.common.core.domain.BaseEntity;
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
     * 对话编号
     */
    private Long conversationId;

    /**
     * 回复消息编号
     */
    private Long replyId;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 角色编号
     */
    private Long roleId;

    /**
     * 模型标志
     */
    private String model;

    /**
     * 模型编号
     */
    private Long modelId;

    /**
     * 聊天内容
     */
    private String content;

    /**
     * 推理内容
     */
    private String reasoningContent;

    /**
     * 是否携带上下文（0否 1是）
     */
    private Boolean useContext;

    /**
     * 知识库段落编号数组
     */
    private List<Long> segmentIds;

    /**
     * 联网搜索的网页内容数组
     */
    private List<WebSearchResponse.WebPage> webSearchPages;

    /**
     * 附件 URL 数组
     */
    private List<String> attachmentUrls;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

}
