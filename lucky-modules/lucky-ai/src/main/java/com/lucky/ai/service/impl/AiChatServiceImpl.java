package com.lucky.ai.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.domain.query.chat.ChatQuery;
import com.lucky.ai.service.IAiApiKeyService;
import com.lucky.ai.service.IAiChatConversationService;
import com.lucky.ai.service.IAiChatService;
import com.lucky.ai.service.IAiModelService;
import com.lucky.common.ai.domain.request.ChatRequest;
import com.lucky.common.ai.domain.vo.ChatResponseVO;
import com.lucky.common.ai.service.chat.ChatService;
import com.lucky.common.core.constant.AiErrorConstants;
import com.lucky.common.core.exception.ServiceException;
import com.lucky.common.security.utils.SecurityUtils;
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
    private ChatService chatService;

    @Override
    public Flux<ChatResponseVO> chatStream(ChatQuery query) {
        // 校验对话存在
        AiChatConversation conversation = chatConversationService.validateChatConversationExists(query.getConversationId());
        if (ObjUtil.notEqual(conversation.getUserId(), SecurityUtils.getUserId())) {
            throw new ServiceException(AiErrorConstants.CHAT_CONVERSATION_NOT_EXISTS);
        }
        // 校验模型
        AiModel model = modelService.validateModel(conversation.getModelId());
        // 校验key
        AiApiKey apiKey = apiKeyService.validateApiKey(model.getKeyId());

        // 构建聊天请求
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setContent(query.getContent());
        chatRequest.setUseThinking(query.getUseThinking());
        chatRequest.setUseSearch(query.getUseSearch());
        chatRequest.setAttachmentUrls(query.getAttachmentUrls());
        chatRequest.setConversationId(conversation.getId());
        chatRequest.setSystemMessage(conversation.getSystemMessage());
        chatRequest.setTemperature(conversation.getTemperature());
        chatRequest.setMaxTokens(conversation.getMaxTokens());
        chatRequest.setMaxContexts(conversation.getMaxContexts());
        chatRequest.setModel(model.getModel());
        chatRequest.setPlatform(model.getPlatform());
        chatRequest.setApiKey(apiKey.getApiKey());
        chatRequest.setUrl(apiKey.getUrl());
        chatRequest.setUserId(SecurityUtils.getUserId());
        chatRequest.setUserName(SecurityUtils.getUserName());
        chatRequest.setDeptId(SecurityUtils.getDeptId());
        // 调用处理器处理流式聊天
        return chatService.chat(chatRequest);
    }

}
