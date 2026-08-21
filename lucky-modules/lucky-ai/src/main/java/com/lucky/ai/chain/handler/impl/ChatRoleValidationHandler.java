package com.lucky.ai.chain.handler.impl;

import cn.hutool.core.util.ObjUtil;
import com.lucky.ai.chain.context.ChatStreamContext;
import com.lucky.ai.chain.handler.ChatStreamHandler;
import com.lucky.ai.domain.AiChatRole;
import com.lucky.ai.service.IAiChatRoleService;
import jakarta.annotation.Resource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 角色校验处理器
 *
 * @author lucky
 */
@Component
@Order(2)
public class ChatRoleValidationHandler implements ChatStreamHandler {

    @Resource
    private IAiChatRoleService chatRoleService;

    @Override
    public String getName() {
        return "角色校验处理器";
    }

    @Override
    public void handle(ChatStreamContext context) {
        // 未配置角色时使用空对象，避免后续处理器空指针
        AiChatRole role = new AiChatRole();
        // 校验会话角色是否有效（角色存在且未禁用，否则抛出业务异常）
        if (ObjUtil.isNotNull(context.getConversation().getRoleId())) {
            role = chatRoleService.validateChatRole(context.getConversation().getRoleId());
        }
        // 校验通过，写入上下文供后续处理器使用
        context.setRole(role);
    }

}
