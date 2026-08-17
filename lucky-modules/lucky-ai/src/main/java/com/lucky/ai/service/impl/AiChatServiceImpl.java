package com.lucky.ai.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.AiChatRole;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.domain.query.chat.ChatQuery;
import com.lucky.ai.service.*;
import com.lucky.common.ai.domain.request.ChatRequest;
import com.lucky.common.ai.enums.AiModelTypeEnum;
import com.lucky.common.ai.service.chat.ChatService;
import com.lucky.common.core.exception.ServiceException;
import com.lucky.common.security.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.http.codec.ServerSentEvent;
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
    private IAiChatRoleService chatRoleService;
    @Resource
    private IAiModelService modelService;
    @Resource
    private IAiApiKeyService apiKeyService;

    @Resource
    private ChatService chatService;

    @Override
    public Flux<ServerSentEvent<String>> chatStream(ChatQuery query) {
        // 校验对话是否存在，不存在则创建
        AiChatConversation conversation;
        if (ObjUtil.isNull(query.getConversationId())) {
            conversation = chatConversationService.insertMyChatConversation();
        } else {
            conversation = chatConversationService.validateChatConversationExists(query.getConversationId());
            if (ObjUtil.notEqual(conversation.getUserId(), SecurityUtils.getUserId())) {
                throw new ServiceException("对话不属于当前用户");
            }
        }
        // 校验会话角色是否有效
        AiChatRole role = new AiChatRole();
        if (ObjUtil.isNotNull(conversation.getRoleId())) {
            role = chatRoleService.validateChatRole(conversation.getRoleId());
        }
        // 校验模型是否有效
        AiModel model = modelService.validateModel(query.getModelId());
        // 校验模型类型是否匹配
        if (ObjUtil.notEqual(model.getType(), AiModelTypeEnum.CHAT.getType())) {
            throw new ServiceException("模型类型不匹配");
        }
        // 校验apikey是否有效
        AiApiKey apiKey = apiKeyService.validateApiKey(model.getKeyId());

        // 构建聊天请求
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setContent(query.getContent());
        chatRequest.setUseThinking(query.getUseThinking());
        chatRequest.setUseSearch(query.getUseSearch());
        chatRequest.setAttachmentUrls(query.getAttachmentUrls());
        chatRequest.setConversationId(conversation.getId());
        chatRequest.setPersona(role.getPersona());
        chatRequest.setHistoryMessageCount(conversation.getHistoryMessageCount());
        chatRequest.setPlatform(model.getPlatform());
        chatRequest.setModel(model.getModel());
        chatRequest.setApiKey(apiKey.getApiKey());
        chatRequest.setUrl(apiKey.getUrl());
        chatRequest.setUserId(SecurityUtils.getUserId());
        chatRequest.setUserName(SecurityUtils.getUserName());
        chatRequest.setDeptId(SecurityUtils.getDeptId());
        // 调用处理器处理流式聊天
        return chatService.chat(chatRequest);
    }

}
