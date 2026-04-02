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

    /**
     * 发送消息（流式）
     *
     * @param query  发送消息（流式）请求
     * @param userId 用户ID
     * @return 发送消息（流式）响应VO
     */
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

    /**
     * 根据会话ID查询聊天消息列表
     *
     * @param conversationId 会话ID
     * @return 聊天消息列表
     */
    @Override
    public List<AiChatMessageVO> getChatMessageListByConversationId(Long conversationId) {
        return chatMessageMapper.selectVoListByConversationId(conversationId);
    }

    /**
     * 删除用户的聊天消息
     *
     * @param id     消息ID
     * @param userId 用户ID
     * @return 删除的消息数量
     */
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

    /**
     * 删除用户的聊天消息（根据会话ID）
     *
     * @param conversationId 会话ID
     * @param userId         用户ID
     * @return 删除的消息数量
     */
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

    /**
     * 查询用户的聊天消息分页列表
     *
     * @param pageQuery 分页查询参数
     * @param query     查询参数
     * @return 聊天消息分页列表
     */
    @Override
    public TableDataInfo<AiChatMessageVO> getChatMessagePage(PageQuery pageQuery, AiChatMessagePageQuery query) {
        IPage<AiChatMessageVO> page = chatMessageMapper.selectPage(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    /**
     * 删除管理员的聊天消息
     *
     * @param id 聊天消息ID
     * @return 删除的消息数量
     */
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

    /**
     * 获得聊天对话的消息数量 Map
     *
     * @param conversationIds 对话编号数组
     * @return 消息数量 Map
     */
    @Override
    public Map<Long, Integer> getChatMessageCountMap(Collection<Long> conversationIds) {
        return chatMessageMapper.selectCountMapByConversationIds(conversationIds);
    }

}