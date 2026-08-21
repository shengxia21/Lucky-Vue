package com.lucky.ai.chain.handler.impl;

import com.lucky.ai.chain.context.ChatStreamContext;
import com.lucky.ai.chain.handler.ChatStreamHandler;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.service.IAiApiKeyService;
import jakarta.annotation.Resource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * APIKEY 校验处理器
 *
 * @author lucky
 */
@Component
@Order(4)
public class ApiKeyValidationHandler implements ChatStreamHandler {

    @Resource
    private IAiApiKeyService apiKeyService;

    @Override
    public String getName() {
        return "APIKEY校验处理器";
    }

    @Override
    public void handle(ChatStreamContext context) {
        // 校验apikey是否有效（依赖前序处理器写入的模型信息，不存在或已禁用时抛出业务异常）
        AiApiKey apiKey = apiKeyService.validateApiKey(context.getModel().getKeyId());
        // 校验通过，写入上下文供后续处理器使用
        context.setApiKey(apiKey);
    }

}
