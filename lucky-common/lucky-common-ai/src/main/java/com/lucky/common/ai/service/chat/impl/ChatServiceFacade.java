package com.lucky.common.ai.service.chat.impl;

import cn.hutool.core.util.StrUtil;
import com.lucky.common.ai.chat.advisor.LuckyMessageChatMemoryAdvisor;
import com.lucky.common.ai.chat.memory.LuckyChatMemory;
import com.lucky.common.ai.domain.request.ChatRequest;
import com.lucky.common.ai.factory.ChatServiceFactory;
import com.lucky.common.ai.service.chat.AbstractChatService;
import com.lucky.common.ai.service.chat.ChatService;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天服务外观类
 *
 * @author lucky
 */
public class ChatServiceFacade implements ChatService {

    @Resource
    private ChatServiceFactory chatFactory;

    @Resource
    private LuckyChatMemory luckyChatMemory;

    @Override
    public Flux<ChatResponse> chat(ChatRequest chatRequest) {
        // 参数校验（attachmentUrls、systemMessage、url 允许为 null）
        this.validateChatRequest(chatRequest);
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
        return chatClient.prompt()
                .messages(messages)
                .options(chatOptions)
                .advisors(a -> a.param(LuckyChatMemory.REQUEST, chatRequest))
                .stream()
                .chatResponse();
    }

    /**
     * 校验聊天请求参数
     * <p>除 attachmentUrls、systemMessage、url 外，其余参数均不可为 null</p>
     *
     * @param chatRequest 聊天请求
     */
    private void validateChatRequest(ChatRequest chatRequest) {
        if (chatRequest == null) {
            throw new IllegalArgumentException("聊天请求参数不能为空");
        }
        if (chatRequest.getContent() == null) {
            throw new IllegalArgumentException("聊天内容(content)不能为空");
        }
        if (chatRequest.getUseThinking() == null) {
            throw new IllegalArgumentException("是否深度思考(useThinking)不能为空");
        }
        if (chatRequest.getUseSearch() == null) {
            throw new IllegalArgumentException("是否联网搜索(useSearch)不能为空");
        }
        if (chatRequest.getConversationId() == null) {
            throw new IllegalArgumentException("会话ID(conversationId)不能为空");
        }
        if (chatRequest.getTemperature() == null) {
            throw new IllegalArgumentException("温度参数(temperature)不能为空");
        }
        if (chatRequest.getMaxTokens() == null) {
            throw new IllegalArgumentException("最大Token数(maxTokens)不能为空");
        }
        if (chatRequest.getMessageCount() == null) {
            throw new IllegalArgumentException("携带历史消息数(messageCount)不能为空");
        }
        if (chatRequest.getModel() == null) {
            throw new IllegalArgumentException("模型(model)不能为空");
        }
        if (chatRequest.getPlatform() == null) {
            throw new IllegalArgumentException("平台(platform)不能为空");
        }
        if (chatRequest.getApiKey() == null) {
            throw new IllegalArgumentException("密钥(apiKey)不能为空");
        }
        if (chatRequest.getUserId() == null) {
            throw new IllegalArgumentException("用户ID(userId)不能为空");
        }
        if (chatRequest.getDeptId() == null) {
            throw new IllegalArgumentException("部门ID(deptId)不能为空");
        }
        if (chatRequest.getUserName() == null) {
            throw new IllegalArgumentException("用户名称(userName)不能为空");
        }
    }

}
