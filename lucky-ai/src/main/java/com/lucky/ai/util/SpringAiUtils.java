package com.lucky.ai.util;

import com.lucky.ai.domain.AiChatMessage;
import com.lucky.common.utils.StringUtils;
import org.springframework.ai.chat.messages.*;

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
    public static Message convertMessage(AiChatMessage message) {
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

}
