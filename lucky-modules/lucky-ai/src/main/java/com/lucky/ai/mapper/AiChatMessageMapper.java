package com.lucky.ai.mapper;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.ai.domain.AiChatMessage;
import com.lucky.ai.domain.query.message.AiChatMessageQuery;
import com.lucky.ai.domain.vo.message.AiChatMessageVO;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.mybatis.core.mapper.BaseMapperX;
import com.lucky.common.security.utils.SecurityUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AI 聊天消息Mapper接口
 *
 * @author lucky
 */
public interface AiChatMessageMapper extends BaseMapperX<AiChatMessage, AiChatMessageVO> {

    default List<AiChatMessageVO> selectMyListByConversationId(Long conversationId) {
        LambdaQueryWrapper<AiChatMessage> wrapper = Wrappers.<AiChatMessage>lambdaQuery()
                .eq(AiChatMessage::getConversationId, conversationId)
                .eq(AiChatMessage::getUserId, SecurityUtils.getUserId())
                .orderByAsc(AiChatMessage::getCreateTime);
        return selectVoList(wrapper);
    }

    default int deleteMyById(Long id) {
        LambdaQueryWrapper<AiChatMessage> wrapper = Wrappers.<AiChatMessage>lambdaQuery()
                .eq(AiChatMessage::getId, id)
                .eq(AiChatMessage::getUserId, SecurityUtils.getUserId());
        return delete(wrapper);
    }

    default int deleteMyByConversationId(Long conversationId) {
        LambdaQueryWrapper<AiChatMessage> wrapper = Wrappers.<AiChatMessage>lambdaQuery()
                .eq(AiChatMessage::getConversationId, conversationId)
                .eq(AiChatMessage::getUserId, SecurityUtils.getUserId());
        return delete(wrapper);
    }

    default IPage<AiChatMessageVO> selectPage(IPage<AiChatMessage> page, AiChatMessageQuery query) {
        LambdaQueryWrapper<AiChatMessage> wrapper = Wrappers.<AiChatMessage>lambdaQuery()
                .eq(AiChatMessage::getConversationId, query.getConversationId())
                .like(StringUtils.isNotNull(query.getContent()), AiChatMessage::getContent, query.getContent())
                .between(!query.getParams().isEmpty(), AiChatMessage::getCreateTime, query.getParams().get("beginTime"), query.getParams().get("endTime"))
                .orderByDesc(AiChatMessage::getCreateTime);
        return selectVoPage(page, wrapper);
    }

    default Map<Long, Integer> selectCountByConversationIds(List<Long> conversationIds) {
        QueryWrapper<AiChatMessage> wrapper = Wrappers.<AiChatMessage>query()
                .select("conversation_id AS conversationId", "COUNT(id) AS count")
                .in("conversation_id", conversationIds)
                .groupBy("conversation_id");
        List<Map<String, Object>> mapList = selectMaps(wrapper);
        return mapList.stream().collect(Collectors.toMap(key -> MapUtil.getLong(key, "conversationId"), key -> MapUtil.getInt(key, "count")));
    }

}