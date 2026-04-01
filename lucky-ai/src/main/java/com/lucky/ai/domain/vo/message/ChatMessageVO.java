package com.lucky.ai.domain.vo.message;

import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.trans.vo.VO;
import com.lucky.ai.core.vo.search.WebSearchResponse;
import com.lucky.ai.domain.AiChatRole;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * AI 聊天消息响应VO
 *
 * @author lucky
 */
@Data
public class ChatMessageVO implements VO {

    /**
     * 编号
     */
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
    @Trans(type = TransType.SIMPLE, target = AiChatRole.class, fields = {"name"}, refs = {"roleName"})
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
     * 是否携带上下文
     */
    private Boolean useContext;

    /**
     * 知识库段落编号数组
     */
    private List<Long> segmentIds;

    /**
     * 知识库段落数组
     */
    private List<KnowledgeSegment> segments;

    /**
     * 联网搜索的网页内容数组
     */
    private List<WebSearchResponse.WebPage> webSearchPages;

    /**
     * 附件 URL 数组
     */
    private List<String> attachmentUrls;

    /**
     * 创建时间
     */
    private Date createTime;

    // ========== 仅在【对话管理】时加载 ==========

    /**
     * 角色名字
     */
    private String roleName;

    /**
     * 知识库段落
     */
    @Data
    public static class KnowledgeSegment {

        /**
         * 段落编号
         */
        private Long id;

        /**
         * 切片内容
         */
        private String content;

        /**
         * 文档编号
         */
        private Long documentId;

        /**
         * 文档名称
         */
        private String documentName;

    }

}