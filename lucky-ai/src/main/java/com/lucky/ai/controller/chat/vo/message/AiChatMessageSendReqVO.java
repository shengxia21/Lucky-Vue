package com.lucky.ai.controller.chat.vo.message;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * AI 聊天消息发送请求VO
 *
 * @author lucky
 */
public class AiChatMessageSendReqVO {

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

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getUseContext() {
        return useContext;
    }

    public void setUseContext(Boolean useContext) {
        this.useContext = useContext;
    }

    public Boolean getUseSearch() {
        return useSearch;
    }

    public void setUseSearch(Boolean useSearch) {
        this.useSearch = useSearch;
    }

    public List<String> getAttachmentUrls() {
        return attachmentUrls;
    }

    public void setAttachmentUrls(List<String> attachmentUrls) {
        this.attachmentUrls = attachmentUrls;
    }

}
