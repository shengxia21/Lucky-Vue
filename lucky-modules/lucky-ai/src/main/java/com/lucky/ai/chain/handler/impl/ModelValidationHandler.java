package com.lucky.ai.chain.handler.impl;

import cn.hutool.core.util.ObjUtil;
import com.lucky.ai.chain.context.ChatStreamContext;
import com.lucky.ai.chain.handler.ChatStreamHandler;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.enums.ModelType;
import com.lucky.ai.service.IAiModelService;
import com.lucky.common.core.exception.ServiceException;
import jakarta.annotation.Resource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 模型校验处理器
 *
 * @author lucky
 */
@Component
@Order(3)
public class ModelValidationHandler implements ChatStreamHandler {

    @Resource
    private IAiModelService modelService;

    @Override
    public String getName() {
        return "模型校验处理器";
    }

    @Override
    public void handle(ChatStreamContext context) {
        // 校验模型是否有效（模型存在且未禁用，否则抛出业务异常）
        AiModel model = modelService.validateModel(context.getQuery().getModelId());
        // 校验模型类型是否匹配
        if (ObjUtil.notEqual(model.getType(), ModelType.CHAT.getCode())) {
            throw new ServiceException("模型类型不匹配");
        }
        // 校验通过，写入上下文供后续处理器使用
        context.setModel(model);
    }

}
