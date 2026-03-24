package com.lucky.ai.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.lucky.ai.controller.chat.vo.message.AiChatMessagePageReqVO;
import com.lucky.ai.controller.chat.vo.message.AiChatMessageRespVO;
import com.lucky.ai.controller.chat.vo.message.AiChatMessageSendReqVO;
import com.lucky.ai.core.context.ChatContext;
import com.lucky.ai.core.processor.chat.ChatProcessor;
import com.lucky.ai.core.processor.chat.ChatProcessorFactory;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.AiChatMessage;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.mapper.AiChatMessageMapper;
import com.lucky.ai.service.IAiApiKeyService;
import com.lucky.ai.service.IAiChatConversationService;
import com.lucky.ai.service.IAiChatMessageService;
import com.lucky.ai.service.IAiModelService;
import com.lucky.common.constant.AiErrorConstants;
import com.lucky.common.core.domain.AjaxResult;
import com.lucky.common.exception.ServiceException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

import static com.lucky.ai.util.CollectionUtils.convertList;

/**
 * AI 聊天消息Service业务层处理
 *
 * @author lucky
 */
@Service
public class AiChatMessageServiceImpl implements IAiChatMessageService {

    @Resource
    private AiChatMessageMapper aiChatMessageMapper;

    @Resource
    private IAiChatConversationService aiChatConversationService;
    @Resource
    private IAiModelService aiModelService;
    @Resource
    private IAiApiKeyService aiApiKeyService;

    @Resource
    private ChatProcessorFactory chatProcessorFactory;

    /**
     * 发送消息（流式）
     *
     * @param sendReqVO 发送消息（流式）请求VO
     * @param userId    用户ID
     * @return 发送消息（流式）响应VO
     */
    @Override
    public Flux<AjaxResult> sendChatMessageStream(AiChatMessageSendReqVO sendReqVO, Long userId) {
        // 校验对话存在
        AiChatConversation conversation = aiChatConversationService.validateChatConversationExists(sendReqVO.getConversationId());
        if (ObjUtil.notEqual(conversation.getUserId(), userId)) {
            throw new ServiceException(AiErrorConstants.CHAT_CONVERSATION_NOT_EXISTS);
        }
        // 校验模型
        AiModel model = aiModelService.validateModel(conversation.getModelId());
        // 校验key
        AiApiKey apiKey = aiApiKeyService.validateApiKey(model.getKeyId());

        // 使用聊天处理器工厂获取对应的处理器
        ChatProcessor chatProcessor = chatProcessorFactory.getOriginalProcessor(model.getPlatform());

        // 构建聊天上下文
        ChatContext chatContext = new ChatContext();
        chatContext.setSendReqVO(sendReqVO);
        chatContext.setConversation(conversation);
        chatContext.setModel(model);
        chatContext.setApiKey(apiKey);
        chatContext.setUserId(userId);

        // 调用处理器处理流式聊天
        return chatProcessor.processStream(chatContext);
    }

    /**
     * 根据会话ID查询聊天消息列表
     *
     * @param conversationId 会话ID
     * @return 聊天消息列表
     */
    @Override
    public List<AiChatMessage> getChatMessageListByConversationId(Long conversationId) {
        return aiChatMessageMapper.selectListByConversationId(conversationId);
    }

    /**
     * 删除用户的聊天消息
     *
     * @param id     消息ID
     * @param userId 用户ID
     * @return 删除的消息数量
     */
    @Override
    public int deleteChatMessage(Long id, Long userId) {
        // 1. 校验消息存在
        AiChatMessage message = aiChatMessageMapper.selectAiChatMessageById(id);
        if (message == null || ObjUtil.notEqual(message.getUserId(), userId)) {
            throw new ServiceException(AiErrorConstants.CHAT_MESSAGE_NOT_EXIST);
        }
        // 2. 执行删除
        return aiChatMessageMapper.deleteAiChatMessageById(id);
    }

    /**
     * 删除用户的聊天消息（根据会话ID）
     *
     * @param conversationId 会话ID
     * @param userId         用户ID
     * @return 删除的消息数量
     */
    @Override
    public int deleteChatMessageByConversationId(Long conversationId, Long userId) {
        // 1. 校验消息存在
        List<AiChatMessage> messages = aiChatMessageMapper.selectListByConversationId(conversationId);
        if (CollUtil.isEmpty(messages) || ObjUtil.notEqual(messages.get(0).getUserId(), userId)) {
            throw new ServiceException(AiErrorConstants.CHAT_MESSAGE_NOT_EXIST);
        }
        // 2. 执行删除
        return aiChatMessageMapper.deleteAiChatMessageByIds(convertList(messages, AiChatMessage::getId));
    }

    /**
     * 查询用户的聊天消息分页列表
     *
     * @param pageReqVO 分页查询请求VO
     * @return 聊天消息分页列表
     */
    @Override
    public List<AiChatMessageRespVO> getChatMessagePage(AiChatMessagePageReqVO pageReqVO) {
        return aiChatMessageMapper.selectChatMessagePage(pageReqVO);
    }

    /**
     * 删除管理员的聊天消息
     *
     * @param id 聊天消息ID
     * @return 删除的消息数量
     */
    @Override
    public int deleteChatMessageByAdmin(Long id) {
        // 1. 校验消息存在
        AiChatMessage message = aiChatMessageMapper.selectAiChatMessageById(id);
        if (message == null) {
            throw new ServiceException(AiErrorConstants.CHAT_MESSAGE_NOT_EXIST);
        }
        // 2. 执行删除
        return aiChatMessageMapper.deleteAiChatMessageById(id);
    }

}