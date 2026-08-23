package com.lucky.ai.chain.handler.impl;

import com.lucky.ai.chain.context.ChatStreamContext;
import com.lucky.ai.chain.handler.ChatStreamHandler;
import com.lucky.ai.domain.query.chat.ChatQuery;
import com.lucky.common.core.exception.ServiceException;
import jakarta.annotation.Resource;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Set;

/**
 * 请求参数校验处理器
 *
 * @author lucky
 */
@Component
@Order(0)
public class ParamValidationHandler implements ChatStreamHandler {

    @Resource
    private Validator validator;

    @Override
    public String getName() {
        return "参数校验处理器";
    }

    @Override
    public void handle(ChatStreamContext context) {
        // 手动触发 Bean Validation 校验（等价于 Controller 上原 @Validated 注解的效果）
        Set<ConstraintViolation<ChatQuery>> violations = validator.validate(context.getQuery());
        if (violations.isEmpty()) {
            return;
        }
        // 按属性名排序保证提示稳定，取第一条错误消息（与原全局异常处理行为保持一致）
        String message = violations.stream()
                .min(Comparator.comparing(v -> v.getPropertyPath().toString()))
                .map(ConstraintViolation::getMessage)
                .orElse("请求参数不合法");
        throw new ServiceException(message);
    }

}
