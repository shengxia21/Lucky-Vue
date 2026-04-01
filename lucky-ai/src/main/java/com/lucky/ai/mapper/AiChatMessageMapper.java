package com.lucky.ai.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.ai.domain.AiChatMessage;
import com.lucky.ai.domain.query.message.ChatMessagePageQuery;
import com.lucky.ai.domain.vo.message.ChatMessageCountVO;
import com.lucky.common.utils.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * AI 聊天消息Mapper接口
 *
 * @author lucky
 */
public interface AiChatMessageMapper extends BaseMapper<AiChatMessage> {

    default List<AiChatMessage> selectListByConversationId(Long conversationId) {
        LambdaQueryWrapper<AiChatMessage> wrapper = Wrappers.<AiChatMessage>lambdaQuery()
                .eq(AiChatMessage::getConversationId, conversationId)
                .orderByAsc(AiChatMessage::getCreateTime);
        return selectList(wrapper);
    }

    List<ChatMessageCountVO> selectCountMapByConversationIds(Collection<Long> conversationIds);

    default IPage<AiChatMessage> selectPage(IPage<AiChatMessage> page, ChatMessagePageQuery query) {
        LambdaQueryWrapper<AiChatMessage> wrapper = Wrappers.<AiChatMessage>lambdaQuery()
                .eq(StringUtils.isNotNull(query.getConversationId()), AiChatMessage::getConversationId, query.getConversationId())
                .eq(StringUtils.isNotNull(query.getUserId()), AiChatMessage::getUserId, query.getUserId())
                .like(StringUtils.isNotNull(query.getContent()), AiChatMessage::getContent, query.getContent())
                .between(!query.getParams().isEmpty(), AiChatMessage::getCreateTime, query.getParams().get("beginTime"), query.getParams().get("endTime"))
                .orderByDesc(AiChatMessage::getCreateTime);
        return selectPage(page, wrapper);
    }

}