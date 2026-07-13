package com.lucky.ai.domain.vo.message;

import com.fhs.core.trans.vo.VO;
import com.lucky.ai.domain.AiChatMessage;
import com.lucky.ai.domain.search.WebSearchResponse;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AI 聊天消息响应VO
 *
 * @author lucky
 */
@Data
@AutoMapper(target = AiChatMessage.class)
public class AiChatMessageVO implements VO {

    /**
     * 编号
     */
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
     * 消息类型
     */
    private String type;

    /**
     * 模型标志
     */
    private String model;

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
    private LocalDateTime createTime;

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