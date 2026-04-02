package com.lucky.ai.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.query.conversation.ChatConversationPageQuery;
import com.lucky.ai.domain.vo.conversation.ChatConversationVO;
import com.lucky.common.core.mybatis.BaseMapperX;
import com.lucky.common.utils.StringUtils;

import java.util.List;

/**
 * AI 聊天对话Mapper接口
 *
 * @author lucky
 */
public interface AiChatConversationMapper extends BaseMapperX<AiChatConversation, ChatConversationVO> {

    default List<ChatConversationVO> selectListByUserId(Long userId) {
        LambdaQueryWrapper<AiChatConversation> wrapper = Wrappers.<AiChatConversation>lambdaQuery()
                .eq(AiChatConversation::getUserId, userId);
        return selectVoList(wrapper);
    }

    default List<AiChatConversation> selectListByUserIdAndPinned(Long userId, Boolean pinned) {
        LambdaQueryWrapper<AiChatConversation> wrapper = Wrappers.<AiChatConversation>lambdaQuery()
                .eq(AiChatConversation::getUserId, userId)
                .eq(AiChatConversation::getPinned, pinned);
        return selectList(wrapper);
    }

    default IPage<ChatConversationVO> selectPage(IPage<AiChatConversation> page, ChatConversationPageQuery query) {
        LambdaQueryWrapper<AiChatConversation> wrapper = Wrappers.<AiChatConversation>lambdaQuery()
                .eq(StringUtils.isNotNull(query.getUserId()), AiChatConversation::getUserId, query.getUserId())
                .like(StringUtils.isNotEmpty(query.getTitle()), AiChatConversation::getTitle, query.getTitle())
                .between(!query.getParams().isEmpty(), AiChatConversation::getCreateTime, query.getParams().get("beginTime"), query.getParams().get("endTime"));
        return selectVoPage(page, wrapper);
    }

}