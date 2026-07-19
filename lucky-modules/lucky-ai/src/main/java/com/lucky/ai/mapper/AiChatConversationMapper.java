package com.lucky.ai.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.query.conversation.AiChatConversationQuery;
import com.lucky.ai.domain.vo.conversation.AiChatConversationVO;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.mybatis.core.mapper.BaseMapperX;
import com.lucky.common.security.utils.SecurityUtils;

import java.util.List;

/**
 * AI 聊天对话Mapper接口
 *
 * @author lucky
 */
public interface AiChatConversationMapper extends BaseMapperX<AiChatConversation, AiChatConversationVO> {

    default List<AiChatConversationVO> selectMyList() {
        LambdaQueryWrapper<AiChatConversation> wrapper = Wrappers.<AiChatConversation>lambdaQuery()
                .eq(AiChatConversation::getUserId, SecurityUtils.getUserId());
        return selectVoList(wrapper);
    }

    default AiChatConversationVO selectMyById(Long id) {
        LambdaQueryWrapper<AiChatConversation> wrapper = Wrappers.<AiChatConversation>lambdaQuery()
                .eq(AiChatConversation::getId, id)
                .eq(AiChatConversation::getUserId, SecurityUtils.getUserId());
        return selectVoOne(wrapper, false);
    }

    default int deleteMyById(Long id) {
        LambdaQueryWrapper<AiChatConversation> wrapper = Wrappers.<AiChatConversation>lambdaQuery()
                .eq(AiChatConversation::getId, id)
                .eq(AiChatConversation::getUserId, SecurityUtils.getUserId());
        return delete(wrapper);
    }

    default int deleteMyUnpinned() {
        LambdaQueryWrapper<AiChatConversation> wrapper = Wrappers.<AiChatConversation>lambdaQuery()
                .eq(AiChatConversation::getUserId, SecurityUtils.getUserId())
                .eq(AiChatConversation::getPinned, false);
        return delete(wrapper);
    }

    default IPage<AiChatConversationVO> selectPage(IPage<AiChatConversation> page, AiChatConversationQuery query) {
        LambdaQueryWrapper<AiChatConversation> wrapper = Wrappers.<AiChatConversation>lambdaQuery()
                .eq(StringUtils.isNotNull(query.getUserId()), AiChatConversation::getUserId, query.getUserId())
                .like(StringUtils.isNotEmpty(query.getTitle()), AiChatConversation::getTitle, query.getTitle())
                .between(!query.getParams().isEmpty(), AiChatConversation::getCreateTime, query.getParams().get("beginTime"), query.getParams().get("endTime"))
                .orderByDesc(AiChatConversation::getCreateTime);
        return selectVoPage(page, wrapper);
    }

}