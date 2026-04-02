package com.lucky.ai.mapper;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.ai.domain.AiChatMessage;
import com.lucky.ai.domain.query.message.AiChatMessagePageQuery;
import com.lucky.ai.domain.vo.message.AiChatMessageVO;
import com.lucky.common.core.mybatis.BaseMapperX;
import com.lucky.common.utils.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AI 聊天消息Mapper接口
 *
 * @author lucky
 */
public interface AiChatMessageMapper extends BaseMapperX<AiChatMessage, AiChatMessageVO> {

    default List<AiChatMessage> selectListByConversationId(Long conversationId) {
        LambdaQueryWrapper<AiChatMessage> wrapper = Wrappers.<AiChatMessage>lambdaQuery()
                .eq(AiChatMessage::getConversationId, conversationId)
                .orderByAsc(AiChatMessage::getCreateTime);
        return selectList(wrapper);
    }

    default List<AiChatMessageVO> selectVoListByConversationId(Long conversationId) {
        LambdaQueryWrapper<AiChatMessage> wrapper = Wrappers.<AiChatMessage>lambdaQuery()
                .eq(AiChatMessage::getConversationId, conversationId)
                .orderByAsc(AiChatMessage::getCreateTime);
        return selectVoList(wrapper);
    }

    default Map<Long, Integer> selectCountMapByConversationIds(Collection<Long> conversationIds) {
        QueryWrapper<AiChatMessage> wrapper = Wrappers.<AiChatMessage>query()
                .select("conversation_id AS conversationId", "COUNT(id) AS count")
                .in("conversation_id", conversationIds)
                .groupBy("conversation_id");
        List<Map<String, Object>> mapList = selectMaps(wrapper);
        return mapList.stream().collect(Collectors.toMap(key -> MapUtil.getLong(key, "conversationId"), key -> MapUtil.getInt(key, "count")));
    }

    default IPage<AiChatMessageVO> selectPage(IPage<AiChatMessage> page, AiChatMessagePageQuery query) {
        LambdaQueryWrapper<AiChatMessage> wrapper = Wrappers.<AiChatMessage>lambdaQuery()
                .eq(StringUtils.isNotNull(query.getConversationId()), AiChatMessage::getConversationId, query.getConversationId())
                .eq(StringUtils.isNotNull(query.getUserId()), AiChatMessage::getUserId, query.getUserId())
                .like(StringUtils.isNotNull(query.getContent()), AiChatMessage::getContent, query.getContent())
                .between(!query.getParams().isEmpty(), AiChatMessage::getCreateTime, query.getParams().get("beginTime"), query.getParams().get("endTime"))
                .orderByDesc(AiChatMessage::getCreateTime);
        return selectVoPage(page, wrapper);
    }

}