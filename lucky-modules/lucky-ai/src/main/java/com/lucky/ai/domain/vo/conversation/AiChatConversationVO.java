package com.lucky.ai.domain.vo.conversation;

import com.fhs.core.trans.vo.VO;
import com.lucky.ai.domain.AiChatConversation;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * AI 聊天对话响应VO
 *
 * @author lucky
 */
@Data
@AutoMapper(target = AiChatConversation.class)
public class AiChatConversationVO implements VO {

    /**
     * 对话编号
     */
    private Long id;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 对话标题
     */
    private String title;

    /**
     * 是否置顶
     */
    private Boolean pinned;

    /**
     * 角色头像
     */
    private String avatar;

    /**
     * 角色设定
     */
    private String systemMessage;

    /**
     * 温度参数
     */
    private Double temperature;

    /**
     * 单条回复的最大 Token 数量
     */
    private Integer maxTokens;

    /**
     * 携带历史消息数
     */
    private Integer messageCount;

    /**
     * 消息总数
     */
    private Integer messageTotal;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}