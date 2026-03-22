package com.lucky.ai.core.processor.chat;

import com.lucky.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 聊天处理器工厂
 * 负责管理和分发不同平台的聊天处理器
 * [工厂模式：自动注入所有实现类]
 *
 * @author lucky
 */
@Component
public class ChatProcessorFactory {

    private final Map<String, ChatProcessor> processorMap;

    @Autowired
    public ChatProcessorFactory(List<ChatProcessor> processors) {
        this.processorMap = processors.stream()
                .collect(Collectors.toMap(
                        ChatProcessor::getProcessorName,
                        processor -> processor)
                );
    }

    /**
     * 根据平台获取聊天处理器
     *
     * @param platform 平台枚举
     * @return 聊天处理器
     */
    public ChatProcessor getProcessor(String platform) {
        ChatProcessor processor = processorMap.get(platform);
        if (processor == null) {
            throw new ServiceException("暂不支持 " + platform + " 平台的聊天功能");
        }
        return processor;
    }

}