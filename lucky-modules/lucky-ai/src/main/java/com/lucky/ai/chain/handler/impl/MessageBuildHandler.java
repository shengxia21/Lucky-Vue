package com.lucky.ai.chain.handler.impl;

import cn.hutool.core.util.StrUtil;
import com.lucky.ai.chain.context.ChatStreamContext;
import com.lucky.ai.chain.handler.ChatStreamHandler;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息构建处理器
 *
 * @author lucky
 */
@Component
@Order(100)
public class MessageBuildHandler implements ChatStreamHandler {

    @Override
    public String getName() {
        return "消息构建处理器";
    }

    @Override
    public void handle(ChatStreamContext context) {
        // 添加系统消息（聊天角色）
        List<Message> messages = new ArrayList<>();
        if (StrUtil.isNotBlank(context.getRole().getPersona())) {
            messages.add(new SystemMessage(context.getRole().getPersona()));
        }
        // 添加用户消息
        messages.add(new UserMessage(context.getQuery().getContent()));
        // 写入上下文供 LLM 调用处理器组装聊天请求时使用
        context.setMessages(messages);
    }

}
