package com.lucky.common.ai.domain.vo;

import lombok.Data;

/**
 * 聊天响应VO
 *
 * @author lucky
 */
@Data
public class ChatResponseVO {

    /**
     * 聊天内容
     */
    private String content;

    /**
     * 推理内容
     */
    private String reasoningContent;

}
