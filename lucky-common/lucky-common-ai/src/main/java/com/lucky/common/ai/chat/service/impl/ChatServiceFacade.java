package com.lucky.common.ai.chat.service.impl;

import cn.hutool.core.util.StrUtil;
import com.lucky.common.ai.chat.advisor.LuckyMessageChatMemoryAdvisor;
import com.lucky.common.ai.chat.memory.LuckyChatMemory;
import com.lucky.common.ai.chat.service.ChatService;
import com.lucky.common.ai.domain.request.ChatRequest;
import com.lucky.common.ai.domain.vo.ChatResponseVO;
import com.lucky.common.ai.factory.ChatServiceFactory;
import com.lucky.common.ai.service.AbstractChatService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天服务外观类
 *
 * @author lucky
 */
@Slf4j
@Service
public class ChatServiceFacade implements ChatService {

    @Resource
    private ChatServiceFactory chatFactory;

    @Resource
    private LuckyChatMemory luckyChatMemory;

    @Override
    public Flux<ChatResponseVO> chat(ChatRequest chatRequest) {
        // 获取聊天服务
        AbstractChatService service = chatFactory.getOriginalService(chatRequest.getPlatform());
        // 构建聊天选项
        ChatOptions chatOptions = service.buildChatOptions(chatRequest);
        // 构建聊天模型
        ChatModel chatModel = service.buildChatModel(chatRequest.getUrl(), chatRequest.getApiKey());
        // 构建聊天客户端
        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(LuckyMessageChatMemoryAdvisor.builder(luckyChatMemory, service).build())
                .build();

        // 添加系统消息（聊天角色）
        List<Message> messages = new ArrayList<>();
        if (StrUtil.isNotBlank(chatRequest.getSystemMessage())) {
            messages.add(new SystemMessage(chatRequest.getSystemMessage()));
        }
        // 添加用户消息
        messages.add(new UserMessage(chatRequest.getContent()));
        // 响应类
        ChatResponseVO responseVo = new ChatResponseVO();
        return chatClient.prompt()
                .messages(messages)
                .options(chatOptions)
                .advisors(a -> a.param(LuckyChatMemory.REQUEST, chatRequest))
                .stream()
                .chatResponse()
                .map(chatResponse -> {
                    String content = service.extractContent(chatResponse.getResult().getOutput());
                    String reasoningContent = service.extractReasoningContent(chatResponse.getResult().getOutput());
                    responseVo.setContent(content);
                    responseVo.setReasoningContent(reasoningContent);
                    return responseVo;
                });
    }

}
