package com.lucky.common.ai.event;

import com.lucky.common.ai.domain.dto.ChatMessageUpdateDTO;
import com.lucky.common.ai.enums.MessageRole;

import java.time.LocalDateTime;

/**
 * 消息更新事件负载（event=update）
 *
 * @param role             消息角色（user / assistant）
 * @param id               消息编号（雪花 id 转字符串）
 * @param createTime       创建时间
 * @param provider         提供商
 * @param model            模型标志
 * @param promptTokens     提示词 Token 数量（用户消息为 null）
 * @param completionTokens 生成 Token 数量（用户消息为 null）
 * @param totalTokens      总 Token 数量（用户消息为 null）
 * @author lucky
 */
public record UpdateEvent(
        String role,
        String id,
        LocalDateTime createTime,
        String provider,
        String model,
        Integer promptTokens,
        Integer completionTokens,
        Integer totalTokens
) {

    /**
     * 依据消息元数据与角色构造事件负载
     *
     * @param role 消息角色
     * @param meta 消息元数据
     * @return 消息更新事件负载
     */
    public static UpdateEvent from(MessageRole role, ChatMessageUpdateDTO meta) {
        return new UpdateEvent(
                role.getValue(),
                meta.getId(),
                meta.getCreateTime(),
                meta.getProvider(),
                meta.getModel(),
                meta.getPromptTokens(),
                meta.getCompletionTokens(),
                meta.getTotalTokens()
        );
    }

}
