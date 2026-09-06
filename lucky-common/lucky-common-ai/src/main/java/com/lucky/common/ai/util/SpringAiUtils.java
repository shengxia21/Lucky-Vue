package com.lucky.common.ai.util;

import com.lucky.common.ai.domain.dto.ChatMessageDTO;
import com.lucky.common.core.utils.StringUtils;
import org.springframework.ai.chat.messages.*;

import java.util.List;

/**
 * Spring AI 工具类
 *
 * @author lucky
 */
public class SpringAiUtils {

    /**
     * 转换消息
     *
     * @param message 消息实体
     * @return 消息对象
     */
    public static Message convertMessage(ChatMessageDTO message) {
        if (MessageType.USER.getValue().equals(message.getType())) {
            return new UserMessage(message.getContent());
        }
        if (MessageType.ASSISTANT.getValue().equals(message.getType())) {
            return new AssistantMessage(message.getContent());
        }
        if (MessageType.SYSTEM.getValue().equals(message.getType())) {
            return new SystemMessage(message.getContent());
        }
        if (MessageType.TOOL.getValue().equals(message.getType())) {
            throw new UnsupportedOperationException("暂不支持 tool 消息：" + message.getContent());
        }
        throw new IllegalArgumentException(StringUtils.format("未知消息类型({})", message.getType()));
    }

    /**
     * 查找消息列表中的第一条助手消息，不存在时返回内容为空的助手消息
     *
     * @param messages 消息列表
     * @return 第一条助手消息；不存在时返回空助手消息
     */
    public static Message findFirstAssistantOrElseEmpty(List<Message> messages) {
        return messages.stream()
                .filter(message -> message.getMessageType().equals(MessageType.ASSISTANT))
                .findFirst()
                .orElseGet(() -> new AssistantMessage(""));
    }

}
