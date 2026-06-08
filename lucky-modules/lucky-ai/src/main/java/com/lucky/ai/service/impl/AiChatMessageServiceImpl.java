package com.lucky.ai.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.ai.domain.AiChatMessage;
import com.lucky.ai.domain.query.message.AiChatMessagePageQuery;
import com.lucky.ai.domain.vo.message.AiChatMessageVO;
import com.lucky.ai.mapper.AiChatMessageMapper;
import com.lucky.ai.service.IAiChatMessageService;
import com.lucky.common.core.constant.AiErrorConstants;
import com.lucky.common.core.exception.ServiceException;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import com.lucky.common.security.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * AI 聊天消息Service业务层处理
 *
 * @author lucky
 */
@Service
public class AiChatMessageServiceImpl implements IAiChatMessageService {

    @Resource
    private AiChatMessageMapper chatMessageMapper;

    @Override
    public List<AiChatMessageVO> getChatMessageListByConversationId(Long conversationId) {
        return chatMessageMapper.selectVoListByConversationId(conversationId);
    }

    @Override
    public int deleteChatMessageByIdAndUserId(Long id) {
        Long userId = SecurityUtils.getUserId();
        // 1. 校验消息存在
        AiChatMessage message = chatMessageMapper.selectById(id);
        if (message == null || ObjUtil.notEqual(message.getUserId(), userId)) {
            throw new ServiceException(AiErrorConstants.CHAT_MESSAGE_NOT_EXIST);
        }
        // 2. 执行删除
        return chatMessageMapper.deleteById(id);
    }

    @Override
    public int deleteChatMessageByConversationIdAndUserId(Long conversationId) {
        Long userId = SecurityUtils.getUserId();
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