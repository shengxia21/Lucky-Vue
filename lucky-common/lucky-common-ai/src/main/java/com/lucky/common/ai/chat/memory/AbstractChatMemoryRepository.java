package com.lucky.common.ai.chat.memory;

import com.lucky.common.ai.domain.dto.ChatMessageDTO;
import com.lucky.common.ai.domain.dto.ChatMessageUpdateDTO;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

/**
 * 抽象聊天记忆存储库（模板）
 *
 * @author lucky
 */
public abstract class AbstractChatMemoryRepository implements LuckyChatMemoryRepository {

    @Override
    public final ChatMessageUpdateDTO save(ChatMessageDTO chatMessage) {
        // 入口校验收口：消息与会话编号必须存在
        Assert.notNull(chatMessage, "chatMessage cannot be null");
        Assert.notNull(chatMessage.getConversationId(), "conversationId cannot be null");
        // 委托具体存储执行持久化，并获取存储生成的标识
        StoredMessageIdentity identity = this.doSave(chatMessage);
        // 标识校验：存储实现必须提供编号与创建时间，缺失即快速失败并给出明确指引
        Assert.notNull(identity, "doSave must return stored identity (id and createTime)");
        Assert.notNull(identity.id(), "stored identity must contain id");
        Assert.notNull(identity.createTime(), "stored identity must contain createTime");
        // 依据存储生成的标识构造消息元数据返回，消息对象不持有编号与创建时间
        // 编号 Long → String：与全局 Jackson 的 Long → String 序列化行为对齐，规避雪花 id 超出 JS 安全整数范围
        return ChatMessageUpdateDTO.builder()
                .id(String.valueOf(identity.id()))
                .createTime(identity.createTime())
                .provider(chatMessage.getProvider())
                .model(chatMessage.getModel())
                .promptTokens(chatMessage.getPromptTokens())
                .completionTokens(chatMessage.getCompletionTokens())
                .totalTokens(chatMessage.getTotalTokens())
                .build();
    }

    /**
     * 执行存储：将消息写入具体存储
     *
     * @param chatMessage 聊天消息
     * @return 存储生成的标识（编号与创建时间）
     */
    protected abstract StoredMessageIdentity doSave(ChatMessageDTO chatMessage);

    /**
     * 存储生成的消息标识（编号与创建时间）
     */
    public record StoredMessageIdentity(Long id, LocalDateTime createTime) {
    }

}