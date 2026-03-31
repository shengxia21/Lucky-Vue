package com.lucky.ai.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.ai.controller.chat.vo.message.AiChatMessageCountRespVO;
import com.lucky.ai.controller.chat.vo.message.AiChatMessagePageReqVO;
import com.lucky.ai.domain.AiChatMessage;
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

    List<AiChatMessageCountRespVO> selectCountMapByConversationIds(Collection<Long> conversationIds);

    default IPage<AiChatMessage> selectPage(IPage<AiChatMessage> page, AiChatMessagePageReqVO pageReqVO) {
        LambdaQueryWrapper<AiChatMessage> wrapper = Wrappers.<AiChatMessage>lambdaQuery()
                .eq(StringUtils.isNotNull(pageReqVO.getConversationId()), AiChatMessage::getConversationId, pageReqVO.getConversationId())
                .eq(StringUtils.isNotNull(pageReqVO.getUserId()), AiChatMessage::getUserId, pageReqVO.getUserId())
                .like(StringUtils.isNotNull(pageReqVO.getContent()), AiChatMessage::getContent, pageReqVO.getContent())
                .between(!pageReqVO.getParams().isEmpty(), AiChatMessage::getCreateTime, pageReqVO.getParams().get("beginTime"), pageReqVO.getParams().get("endTime"))
                .orderByDesc(AiChatMessage::getCreateTime);
        return selectPage(page, wrapper);
    }

}