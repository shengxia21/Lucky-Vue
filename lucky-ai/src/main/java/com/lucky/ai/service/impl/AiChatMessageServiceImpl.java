package com.lucky.ai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.lucky.ai.controller.chat.vo.message.AiChatMessagePageReqVO;
import com.lucky.ai.controller.chat.vo.message.AiChatMessageSendReqVO;
import com.lucky.ai.controller.chat.vo.message.AiChatMessageSendRespVO;
import com.lucky.ai.core.websearch.AiWebSearchResponse;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.AiChatMessage;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.enums.model.AiPlatformEnum;
import com.lucky.ai.mapper.AiChatMessageMapper;
import com.lucky.ai.service.IAiChatConversationService;
import com.lucky.ai.service.IAiChatMessageService;
import com.lucky.ai.service.IAiModelService;
import com.lucky.ai.util.AiUtils;
import com.lucky.common.constant.AiErrorConstants;
import com.lucky.common.core.domain.AjaxResult;
import com.lucky.common.exception.ServiceException;
import com.lucky.common.utils.DateUtils;
import com.lucky.common.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.lucky.ai.util.CollectionUtils.convertList;

/**
 * AI 聊天消息Service业务层处理
 *
 * @author lucky
 */
@Service
public class AiChatMessageServiceImpl implements IAiChatMessageService {

    private static final Logger log = LoggerFactory.getLogger(AiChatMessageServiceImpl.class);

    @Autowired
    private AiChatMessageMapper aiChatMessageMapper;

    @Autowired
    private IAiChatConversationService aiChatConversationService;
    @Autowired
    private IAiModelService aiModelService;

    /**
     * 联网搜索的结束数
     */
    private static final Integer WEB_SEARCH_COUNT = 10;

    /**
     * 知识库转 {@link UserMessage} 的内容模版
     */
    private static final String WEB_SEARCH_USER_MESSAGE_TEMPLATE = "使用 <WebSearch></WebSearch> 标记中的内容作为本次对话的参考:\n\n" +
            "%s\n\n" + // 多个 <WebSearch></WebSearch> 的拼接
            "回答要求：\n- 避免提及你是从 <WebSearch></WebSearch> 获取的知识。";

    /**
     * 附件转 ${@link UserMessage} 的内容模版
     */
    @SuppressWarnings("TextBlockMigration")
    private static final String Attachment_USER_MESSAGE_TEMPLATE = "使用 <Attachment></Attachment> 标记用户对话上传的附件内容:\n\n" +
            "%s\n\n" + // 多个 <Attachment></Attachment> 的拼接
            "回答要求：\n- 避免提及 <Attachment></Attachment> 附件的编码格式。";

    /**
     * 发送消息（段式）
     *
     * @param sendReqVO 发送消息（段式）请求VO
     * @param userId    用户ID
     * @return 发送消息（段式）响应VO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AiChatMessageSendRespVO sendMessage(AiChatMessageSendReqVO sendReqVO, Long userId) {
        // 1.1 校验对话存在
        AiChatConversation conversation = aiChatConversationService.validateChatConversationExists(sendReqVO.getConversationId());
        if (ObjUtil.notEqual(conversation.getUserId(), userId)) {
            throw new ServiceException(AiErrorConstants.CHAT_CONVERSATION_NOT_EXISTS);
        }
        List<AiChatMessage> historyMessages = aiChatMessageMapper.selectListByConversationId(conversation.getId());
        // 1.2 校验模型
        AiModel model = aiModelService.validateModel(conversation.getModelId());
        ChatModel chatModel = aiModelService.getChatModel(model.getId());

        // 2.1 知识库召回
        // 2.2 联网搜索

        // 3. 插入 user 发送消息
        AiChatMessage userMessage = createChatMessage(conversation.getId(), null, model,
                userId, conversation.getRoleId(), MessageType.USER, sendReqVO.getContent(), sendReqVO.getUseContext(),
                sendReqVO.getAttachmentUrls(), null);

        // 4.1 插入 assistant 接收消息
        AiChatMessage assistantMessage = createChatMessage(conversation.getId(), userMessage.getId(), model,
                userId, conversation.getRoleId(), MessageType.ASSISTANT, "", sendReqVO.getUseContext(),
                null, null);

        // 4.2 创建 chat 需要的 Prompt
        Prompt prompt = buildPrompt(conversation, historyMessages, null, model, sendReqVO);
        ChatResponse chatResponse = chatModel.call(prompt);

        // 4.3 更新响应内容
        String newContent = AiUtils.getChatResponseContent(chatResponse);
        String newReasoningContent = AiUtils.getChatResponseReasoningContent(chatResponse);
        updateOrCreateAssistantMessage(assistantMessage, newContent, newReasoningContent, SecurityUtils.getUsername());
        // 4.4 响应结果
        AiChatMessageSendRespVO respVO = new AiChatMessageSendRespVO();
        respVO.setSend(BeanUtil.toBean(userMessage, AiChatMessageSendRespVO.Message.class));
        respVO.setReceive(BeanUtil.toBean(assistantMessage, AiChatMessageSendRespVO.Message.class).setContent(newContent));
        return respVO;
    }

    /**
     * 发送消息（流式）
     *
     * @param sendReqVO 发送消息（流式）请求VO
     * @param userId    用户ID
     * @return 发送消息（流式）响应VO
     */
    @Override
    public Flux<AjaxResult> sendChatMessageStream(AiChatMessageSendReqVO sendReqVO, Long userId) {
        // 1.1 校验对话存在
        AiChatConversation conversation = aiChatConversationService.validateChatConversationExists(sendReqVO.getConversationId());
        if (ObjUtil.notEqual(conversation.getUserId(), userId)) {
            throw new ServiceException(AiErrorConstants.CHAT_CONVERSATION_NOT_EXISTS);
        }
        List<AiChatMessage> historyMessages = aiChatMessageMapper.selectListByConversationId(conversation.getId());
        // 1.2 校验模型
        AiModel model = aiModelService.validateModel(conversation.getModelId());
        ChatModel chatModel = aiModelService.getChatModel(model.getId());

        // 2.1 知识库召回
        // 2.2 联网搜索

        // 3. 插入 user 发送消息
        AiChatMessage userMessage = createChatMessage(conversation.getId(), null, model,
                userId, conversation.getRoleId(), MessageType.USER, sendReqVO.getContent(), sendReqVO.getUseContext(),
                sendReqVO.getAttachmentUrls(), null);

        // 4.1 插入 assistant 接收消息
        AiChatMessage assistantMessage = createChatMessage(conversation.getId(), userMessage.getId(), model,
                userId, conversation.getRoleId(), MessageType.ASSISTANT, "", sendReqVO.getUseContext(),
                null, null);

        // 4.2 构建 Prompt，并进行调用
        Prompt prompt = buildPrompt(conversation, historyMessages, null, model, sendReqVO);
        Flux<ChatResponse> streamResponse = chatModel.stream(prompt);

        String username = SecurityUtils.getUsername();

        // 4.3 流式返回
        StringBuffer contentBuffer = new StringBuffer();
        StringBuffer reasoningContentBuffer = new StringBuffer();
        return streamResponse.map(chunk -> {
            // 响应结果
            String newContent = AiUtils.getChatResponseContent(chunk);
            String newReasoningContent = AiUtils.getChatResponseReasoningContent(chunk);
            if (StrUtil.isNotEmpty(newContent)) {
                contentBuffer.append(newContent);
            }
            if (StrUtil.isNotEmpty(newReasoningContent)) {
                reasoningContentBuffer.append(newReasoningContent);
            }
            return AjaxResult.success(new AiChatMessageSendRespVO()
                    .setSend(BeanUtil.toBean(userMessage, AiChatMessageSendRespVO.Message.class))
                    .setReceive(BeanUtil.toBean(assistantMessage, AiChatMessageSendRespVO.Message.class)
                            .setContent(StrUtil.nullToDefault(newContent, "")) // 避免 null 的 情况
                            .setReasoningContent(StrUtil.nullToDefault(newReasoningContent, "")) // 避免 null 的 情况
                    ));
        }).doOnComplete(() -> updateOrCreateAssistantMessage(assistantMessage, contentBuffer.toString(), reasoningContentBuffer.toString(), username))
        .doOnError(throwable -> {
            log.error("[sendChatMessageStream][userId({}) sendReqVO({}) 发生异常]", userId, sendReqVO, throwable);
            handleStreamMessage(assistantMessage, contentBuffer.toString(), reasoningContentBuffer.toString(), username);
        }).doOnCancel(() -> {
            log.info("[sendChatMessageStream][userId({}) sendReqVO({}) 取消请求]", userId, sendReqVO);
            handleStreamMessage(assistantMessage, contentBuffer.toString(), reasoningContentBuffer.toString(), username);
        }).onErrorResume(error -> Flux.just(AjaxResult.error(AiErrorConstants.CHAT_STREAM_ERROR)));
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
    public List<AiChatMessage> getChatMessagePage(AiChatMessagePageReqVO pageReqVO) {
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

    private AiChatMessage createChatMessage(Long conversationId, Long replyId,
                                            AiModel model, Long userId, Long roleId,
                                            MessageType messageType, String content,
                                            Boolean useContext, List<String> attachmentUrls,
                                            AiWebSearchResponse webSearchResponse) {
        AiChatMessage message = new AiChatMessage();
        message.setConversationId(conversationId);
        message.setReplyId(replyId);
        message.setModel(model.getModel());
        message.setModelId(model.getId());
        message.setUserId(userId);
        message.setRoleId(roleId);
        message.setType(messageType.getValue());
        message.setContent(content);
        message.setUseContext(useContext);
        message.setAttachmentUrls(attachmentUrls);
        if (webSearchResponse != null) {
            message.setWebSearchPages(webSearchResponse.getLists());
        }
        message.setCreateBy(SecurityUtils.getUsername());
        message.setCreateTime(DateUtils.getNowDate());
        aiChatMessageMapper.insertAiChatMessage(message);
        return message;
    }

    private Prompt buildPrompt(AiChatConversation conversation, List<AiChatMessage> messages,
                               AiWebSearchResponse webSearchResponse,
                               AiModel model, AiChatMessageSendReqVO sendReqVO) {
        List<Message> chatMessages = new ArrayList<>();
        // 1.1 System Context 角色设定
        if (StrUtil.isNotBlank(conversation.getSystemMessage())) {
            chatMessages.add(new SystemMessage(conversation.getSystemMessage()));
        }

        // 1.2 历史 history message 历史消息
        List<AiChatMessage> contextMessages = filterContextMessages(messages, conversation, sendReqVO);
        contextMessages.forEach(message -> {
            chatMessages.add(AiUtils.buildMessage(message.getType(), message.getContent()));
//            UserMessage attachmentUserMessage = buildAttachmentUserMessage(message.getAttachmentUrls());
//            if (attachmentUserMessage != null) {
//                chatMessages.add(attachmentUserMessage);
//            }
        });

        // 1.3 当前 user message 新发送消息
        chatMessages.add(new UserMessage(sendReqVO.getContent()));

        // 1.4 知识库，通过 UserMessage 实现

        // 1.5 联网搜索，通过 UserMessage 实现
        if (webSearchResponse != null && CollUtil.isNotEmpty(webSearchResponse.getLists())) {
            String webSearch = webSearchResponse.getLists().stream()
                    .map(page -> {
                        String summary = StrUtil.isNotEmpty(page.getSummary()) ?
                                "\nSummary: " + page.getSummary() : "";
                        return "<WebSearch title=\"" + page.getTitle() + "\" url=\"" + page.getUrl() + "\">"
                                + StrUtil.blankToDefault(page.getSummary(), page.getSnippet()) + "</WebSearch>";
                    })
                    .collect(Collectors.joining("\n\n"));
            chatMessages.add(new UserMessage(String.format(WEB_SEARCH_USER_MESSAGE_TEMPLATE, webSearch)));
        }

        // 1.6 附件，通过 UserMessage 实现

        // 2.1 查询 tool 工具

        // 2.2 构建 ChatOptions 对象
        AiPlatformEnum platform = AiPlatformEnum.validatePlatform(model.getPlatform());
        ChatOptions chatOptions = AiUtils.buildChatOptions(platform, model.getModel(),
                conversation.getTemperature(), conversation.getMaxTokens(),
                null, null);
        return new Prompt(chatMessages, chatOptions);
    }

    private List<AiChatMessage> filterContextMessages(List<AiChatMessage> messages,
                                                      AiChatConversation conversation,
                                                      AiChatMessageSendReqVO sendReqVO) {
        if (conversation.getMaxContexts() == null || ObjUtil.notEqual(sendReqVO.getUseContext(), Boolean.TRUE)) {
            return Collections.emptyList();
        }
        List<AiChatMessage> contextMessages = new ArrayList<>(conversation.getMaxContexts() * 2);
        for (int i = messages.size() - 1; i >= 0; i--) {
            AiChatMessage assistantMessage = CollUtil.get(messages, i);
            if (assistantMessage == null || assistantMessage.getReplyId() == null) {
                continue;
            }
            AiChatMessage userMessage = CollUtil.get(messages, i - 1);
            if (userMessage == null
                    || ObjUtil.notEqual(assistantMessage.getReplyId(), userMessage.getId())
                    || StrUtil.isEmpty(assistantMessage.getContent())) {
                continue;
            }
            // 由于后续要 reverse 反转，所以先添加 assistantMessage
            contextMessages.add(assistantMessage);
            contextMessages.add(userMessage);
            // 超过最大上下文，结束
            if (contextMessages.size() >= conversation.getMaxContexts() * 2) {
                break;
            }
        }
        Collections.reverse(contextMessages);
        return contextMessages;
    }

    private void handleStreamMessage(AiChatMessage assistantMessage, String content, String reasoningContent, String username) {
        // 如果有内容，则更新内容
        if (StrUtil.isNotEmpty(content)) {
            updateOrCreateAssistantMessage(assistantMessage, content, reasoningContent, username);
        } else {
            // 否则，则进行删除
            aiChatMessageMapper.deleteAiChatMessageById(assistantMessage.getId());
        }
    }

    private void updateOrCreateAssistantMessage(AiChatMessage assistantMessage, String content, String reasoningContent, String username) {
        AiChatMessage message = new AiChatMessage();
        message.setId(assistantMessage.getId());
        message.setContent(content);
        message.setReasoningContent(reasoningContent);
        message.setUpdateBy(username);
        message.setUpdateTime(DateUtils.getNowDate());
        aiChatMessageMapper.updateAiChatMessage(message);
    }

}
