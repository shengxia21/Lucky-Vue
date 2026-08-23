package com.lucky.ai.domain.query.chat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 聊天查询对象
 *
 * @author lucky
 */
@Data
public class ChatQuery {

    /**
     * 聊天对话编号
     */
    @NotNull(message = "对话编号不能为空")
    private Long conversationId;

    /**
     * 模型编号
     */
    @NotNull(message = "模型编号不能为空")
    private Long modelId;

    /**
     * 聊天内容
     */
    @NotEmpty(message = "聊天内容不能为空")
    private String content;

    /**
     * 是否开启深度思考
     */
    @NotNull(message = "是否开启深度思考不能为空")
    private Boolean enableThinking;

    /**
     * 是否开启联网搜索
     */
    @NotNull(message = "是否开启联网搜索不能为空")
    private Boolean enableSearch;

    /**
     * 附件 URL 数组
     */
    private List<String> attachmentUrls;

}
