package com.lucky.ai.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.ai.core.context.ChatContext;
import com.lucky.ai.core.facade.ChatServiceFacade;
import com.lucky.ai.core.vo.chat.ChatMessageRequest;
import com.lucky.ai.core.vo.chat.ChatMessageResponse;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.AiChatMessage;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.domain.query.message.AiChatMessagePageQuery;
import com.lucky.ai.domain.vo.message.AiChatMessageVO;
import com.lucky.ai.mapper.AiChatMessageMapper;
import com.lucky.ai.service.AiApiKeyService;
import com.lucky.ai.service.AiChatConversationService;
import com.lucky.ai.service.AiChatMessageService;
import com.lucky.ai.service.AiModelService;
import com.lucky.common.constant.AiErrorConstants;
import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.common.exception.ServiceException;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * AI 聊天消息Service业务层处理
 *
 * @author lucky
 */
@Service
public class AiChatMessageServiceImpl implements AiChatMessageService {

    @Resource
    private AiChatMessageMapper chatMessageMapper;

    @Lazy
    @Resource
    private AiChatConversationService chatConversationService;
    @Resource
    private AiModelService modelService;
    @Resource
    private AiApiKeyService apiKeyService;

    @Resource
    private ChatServiceFacade chatService;

    @Override
    public Flux<ChatMessageResponse> sendChatMessageStream(ChatMessageRequest query, Long userId) {
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
        // 调用处理器处理流式聊天
        return chatService.chat(chatContext);
    }

    @Override
    public List<AiChatMessageVO> getChatMessageListByConversationId(Long conversationId) {
        return chatMessageMapper.selectVoListByConversationId(conversationId);
    }

    @Override
    public int deleteChatMessageByIdAndUserId(Long id, Long userId) {
        // 1. 校验消息存在
        AiChatMessage message = chatMessageMapper.selectById(id);
        if (message == null || ObjUtil.notEqual(message.getUserId(), userId)) {
            throw new ServiceException(AiErrorConstants.CHAT_MESSAGE_NOT_EXIST);
        }
        // 2. 执行删除
        return chatMessageMapper.deleteById(id);
    }

    @Override
    public int deleteChatMessageByConversationIdAndUserId(Long conversationId, Long userId) {
        List<AiChatMessage> messages = chatMessageMapper.selectListByConversationId(conversationId);
        // 校验消息存在
        if (CollUtil.isEmpty(messages) || ObjUtil.notEqual(messages.get(0).getUserId(), userId)) {
            throw new ServiceException(AiErrorConstants.CHAT_MESSAGE_NOT_EXIST);
        }
        // 执行删除
        List<Long> ids = messages.stream().map(AiChatMessage::getId).toList();
        return chatMessageMapper.deleteByIds(ids);
    }

    @Override
    public TableDataInfo<AiChatMessageVO> getChatMessagePage(PageQuery pageQuery, AiChatMessagePageQuery query) {
        IPage<AiChatMessageVO> page = chatMessageMapper.selectPage(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    @Override
    public int deleteChatMessageById(Long id) {
        // 1. 校验消息存在
        AiChatMessage message = chatMessageMapper.selectById(id);
        if (message == null) {
            throw new ServiceException(AiErrorConstants.CHAT_MESSAGE_NOT_EXIST);
        }
        // 2. 执行删除
        return chatMessageMapper.deleteById(id);
    }

    @Override
    public Map<Long, Integer> getChatMessageCountMap(Collection<Long> conversationIds) {
        return chatMessageMapper.selectCountMapByConversationIds(conversationIds);
    }

}