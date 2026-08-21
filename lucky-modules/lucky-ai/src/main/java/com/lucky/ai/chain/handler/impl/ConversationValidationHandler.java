package com.lucky.ai.chain.handler.impl;

import cn.hutool.core.util.ObjUtil;
import com.lucky.ai.chain.context.ChatStreamContext;
import com.lucky.ai.chain.handler.ChatStreamHandler;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.service.IAiChatConversationService;
import com.lucky.common.core.exception.ServiceException;
import com.lucky.common.security.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 对话校验处理器
 *
 * @author lucky
 */
@Component
@Order(1)
public class ConversationValidationHandler implements ChatStreamHandler {

    @Resource
    private IAiChatConversationService chatConversationService;

    @Override
    public String getName() {
        return "对话校验处理器";
    }

    @Override
    public void handle(ChatStreamContext context) {
        context.sendEvent("validation", "正在校验对话");
        // 校验对话是否存在
        AiChatConversation conversation = chatConversationService.validateChatConversationExists(context.getQuery().getConversationId());
        // 校验对话是否属于当前用户
        if (ObjUtil.notEqual(conversation.getUserId(), SecurityUtils.getUserId())) {
            throw new ServiceException("对话不属于当前用户");
        }
        // 校验通过，写入上下文供后续处理器使用
        context.setConversation(conversation);
    }

}
