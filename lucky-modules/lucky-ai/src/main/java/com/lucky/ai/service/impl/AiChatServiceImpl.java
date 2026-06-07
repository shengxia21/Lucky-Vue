package com.lucky.ai.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.domain.request.chat.ChatMessageRequest;
import com.lucky.ai.service.IAiApiKeyService;
import com.lucky.ai.service.IAiChatConversationService;
import com.lucky.ai.service.IAiChatService;
import com.lucky.ai.service.IAiModelService;
import com.lucky.common.ai.domain.context.ChatContext;
import com.lucky.common.ai.domain.response.ChatMessageResponse;
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
public class AiChatServiceImpl implements IAiChatService {

    @Resource
    private IAiChatConversationService chatConversationService;
    @Resource
    private IAiModelService modelService;
    @Resource
    private IAiApiKeyService apiKeyService;

    @Resource
    private ChatServiceFacade chatServiceFacade;

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
        chatContext.setContent(query.getContent());
        chatContext.setUseThinking(query.getUseThinking());
        chatContext.setUseSearch(query.getUseSearch());
        chatContext.setAttachmentUrls(query.getAttachmentUrls());
        chatContext.setConversationId(conversation.getId());
        chatContext.setRoleId(conversation.getRoleId());
        chatContext.setSystemMessage(conversation.getSystemMessage());
        chatContext.setTemperature(conversation.getTemperature());
        chatContext.setMaxTokens(conversation.getMaxTokens());
        chatContext.setMaxContexts(conversation.getMaxContexts());
        chatContext.setModelId(model.getId());
        chatContext.setModel(model.getModel());
        chatContext.setPlatform(model.getPlatform());
        chatContext.setApiKey(apiKey.getApiKey());
        chatContext.setUrl(apiKey.getUrl());
        chatContext.setUserId(userId);
        chatContext.setUserName(userName);
        // 调用处理器处理流式聊天
        return chatServiceFacade.chat(chatContext);
    }

}
