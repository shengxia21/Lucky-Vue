package com.lucky.ai.core.vo.chat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 聊天消息请求
 *
 * @author lucky
 */
@Data
public class ChatMessageRequest {

    /**
     * 聊天对话编号
     */
    @NotNull(message = "聊天对话编号不能为空")
    private Long conversationId;

    /**
     * 聊天内容
     */
    @NotEmpty(message = "聊天内容不能为空")
    private String content;

    /**
     * 是否携带上下文
     */
    private Boolean useContext;

    /**
     * 是否联网搜索
     */
    private Boolean useSearch;

    /**
     * 附件 URL 数组
     */
    private List<String> attachmentUrls;

}
