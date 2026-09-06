package com.lucky.common.ai.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 更新聊天消息DTO
 *
 * @author lucky
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageUpdateDTO {

    /**
     * 编号（已转字符串：与全局 Jackson 的 Long → String 序列化行为对齐，规避雪花 id 超出 JS 安全整数范围）
     */
    private String id;

    /**
     * 创建时间（由 JsonUtils 统一按 yyyy-MM-dd HH:mm:ss 序列化）
     */
    private LocalDateTime createTime;

    /**
     * 提供商
     */
    private String provider;

    /**
     * 模型标志
     */
    private String model;

    /**
     * 提示词 Token 数量（用户消息为 null，序列化时忽略）
     */
    private Integer promptTokens;

    /**
     * 生成 Token 数量（用户消息为 null，序列化时忽略）
     */
    private Integer completionTokens;

    /**
     * 总 Token 数量（用户消息为 null，序列化时忽略）
     */
    private Integer totalTokens;

}
