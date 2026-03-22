package com.lucky.ai.util;

import cn.hutool.core.util.StrUtil;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.deepseek.DeepSeekAssistantMessage;

/**
 * Spring AI 工具类
 *
 * @author lucky
 */
public class AiUtils {

    public static Message buildMessage(String type, String content) {
        if (MessageType.USER.getValue().equals(type)) {
            return new UserMessage(content);
        }
        if (MessageType.ASSISTANT.getValue().equals(type)) {
            return new AssistantMessage(content);
        }
        if (MessageType.SYSTEM.getValue().equals(type)) {
            return new SystemMessage(content);
        }
        if (MessageType.TOOL.getValue().equals(type)) {
            throw new UnsupportedOperationException("暂不支持 tool 消息：" + content);
        }
        throw new IllegalArgumentException(StrUtil.format("未知消息类型({})", type));
    }

    public static String getChatResponseContent(ChatResponse response) {
        if (response == null) {
            return null;
        }
        return response.getResult().getOutput().getText();
    }

    public static String getChatResponseReasoningContent(ChatResponse response) {
        if (response == null) {
            return null;
        }
        if (response.getResult().getOutput() instanceof DeepSeekAssistantMessage) {
            return ((DeepSeekAssistantMessage) (response.getResult().getOutput())).getReasoningContent();
        } else {
            return (String) response.getResult().getOutput().getMetadata().getOrDefault("reasoningContent", "");
        }
    }

}
