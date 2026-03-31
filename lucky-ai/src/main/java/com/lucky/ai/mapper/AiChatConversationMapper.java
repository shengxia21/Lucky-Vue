package com.lucky.ai.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.ai.controller.chat.vo.conversation.AiChatConversationPageReqVO;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.common.utils.StringUtils;

import java.util.List;

/**
 * AI 聊天对话Mapper接口
 *
 * @author lucky
 */
public interface AiChatConversationMapper extends BaseMapper<AiChatConversation> {

    default List<AiChatConversation> selectListByUserId(Long userId) {
        LambdaQueryWrapper<AiChatConversation> wrapper = Wrappers.<AiChatConversation>lambdaQuery()
                .eq(AiChatConversation::getUserId, userId);
        return selectList(wrapper);
    }

    default List<AiChatConversation> selectListByUserIdAndPinned(Long userId, Boolean pinned) {
        LambdaQueryWrapper<AiChatConversation> wrapper = Wrappers.<AiChatConversation>lambdaQuery()
                .eq(AiChatConversation::getUserId, userId)
                .eq(AiChatConversation::getPinned, pinned);
        return selectList(wrapper);
    }

    default IPage<AiChatConversation> selectPage(IPage<AiChatConversation> page, AiChatConversationPageReqVO pageReqVO) {
        LambdaQueryWrapper<AiChatConversation> wrapper = Wrappers.<AiChatConversation>lambdaQuery()
                .eq(StringUtils.isNotNull(pageReqVO.getUserId()), AiChatConversation::getUserId, pageReqVO.getUserId())
                .like(StringUtils.isNotEmpty(pageReqVO.getTitle()), AiChatConversation::getTitle, pageReqVO.getTitle())
                .between(!pageReqVO.getParams().isEmpty(), AiChatConversation::getCreateTime, pageReqVO.getParams().get("beginTime"), pageReqVO.getParams().get("endTime"));
        return selectPage(page, wrapper);
    }

}