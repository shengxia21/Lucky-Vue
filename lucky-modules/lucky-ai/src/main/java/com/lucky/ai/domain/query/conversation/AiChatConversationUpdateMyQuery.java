package com.lucky.ai.domain.query.conversation;

import com.lucky.ai.domain.AiChatConversation;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新我的聊天对话请求对象
 *
 * @author lucky
 */
@Data
@AutoMapper(target = AiChatConversation.class, reverseConvertGenerate = false)
public class AiChatConversationUpdateMyQuery {

    /**
     * 对话编号
     */
    @NotNull(message = "对话编号不能为空")
    private Long id;

    /**
     * 对话标题
     */
    @Size(max = 50, message = "对话标题不能超过50个字符")
    private String title;

    /**
     * 是否置顶
     */
    private Boolean pinned;

    /**
     * 携带历史消息数
     */
    @Max(value = 30, message = "携带历史消息数不能超过30条")
    @Min(value = 0, message = "携带历史消息数不能小于0")
    private Integer historyMessageCount;

}