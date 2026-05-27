package com.lucky.ai.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.lucky.ai.core.context.ChatContext;
import com.lucky.ai.core.facade.ChatServiceFacade;
import com.lucky.ai.core.vo.chat.ChatMessageRequest;
import com.lucky.ai.core.vo.chat.ChatMessageResponse;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.service.AiApiKeyService;
import com.lucky.ai.service.AiChatConversationService;
import com.lucky.ai.service.AiChatService;
import com.lucky.ai.service.AiModelService;
import com.lucky.common.core.constant.AiErrorConstants;
import com.lucky.common.core.exception.ServiceException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * AI 聊天Service业务层处理
 *
 * @author lucky
 */
@Service
public class AiChatServiceImpl implements AiChatService {

    @Resource
    private AiChatConversationService chatConversationService;
    @Resource
    private AiModelService modelService;
    @Resource
    private AiApiKeyService apiKeyService;

    @Resource
    private ChatServiceFacade chatService;

    @Override
    public Flux<ChatMessageResponse> sendChatStream(ChatMessageRequest query, Long userId, String userName) {
        // 校验对话存在
        AiChatConversation conversation = chatConversationService.validateChatConversationExists(query.getConversationId());
        if (ObjUtil.notEqual(conversation.getUserId(), userId)) {
            throw new ServiceException(AiErrorConstants.CHAT_CONVERSATION_NOT_EXISTS);
        }
        // 校验模型
        AiModel model = modelService.validateModel(conversation.getModelId());
        // 校验key
        AiApiKey apiKey = apiKeyService.validateApiKey(model.getKeyId());

        // 构建聊天上下文
        ChatContext chatContext = new ChatContext();
        chatContext.setRequest(query);
        chatContext.setConversation(conversation);
        chatContext.setModel(model);
        chatContext.setApiKey(apiKey);
        chatContext.setUserId(userId);
        chatContext.setUserName(userName);
        // 调用处理器处理流式聊天
        return chatService.chat(chatContext);
    }

}
