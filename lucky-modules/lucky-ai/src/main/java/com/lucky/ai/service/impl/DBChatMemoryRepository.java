package com.lucky.ai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.ai.domain.AiChatMessage;
import com.lucky.ai.mapper.AiChatMessageMapper;
import com.lucky.common.ai.chat.memory.LuckyChatMemoryRepository;
import com.lucky.common.ai.domain.dto.ChatMessageDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 数据库聊天记忆存储库
 *
 * @author lucky
 */
@Component
public class DBChatMemoryRepository implements LuckyChatMemoryRepository {

    @Resource
    private AiChatMessageMapper chatMessageMapper;

    @Override
    public void save(ChatMessageDTO chatMessage) {
        AiChatMessage message = BeanUtil.toBean(chatMessage, AiChatMessage.class);
        // 线程切换，非web主线程，这里手动设值
        message.setCreateDept(chatMessage.getDeptId());
        message.setCreateBy(chatMessage.getUserName());
        message.setUpdateBy(chatMessage.getUserName());
        chatMessageMapper.insert(message);
    }

    @Override
    public List<ChatMessageDTO> findRecentByConversationId(Long conversationId, Integer limit) {
        // 取最近 limit 条，避免长会话全量查询：先按创建时间倒序（新 → 旧）查出最近 limit 条，再反转为正序（旧 → 新）
        List<AiChatMessage> messageList = chatMessageMapper.selectList(Wrappers.<AiChatMessage>lambdaQuery()
                .eq(AiChatMessage::getConversationId, conversationId)
                .orderByDesc(AiChatMessage::getCreateTime)
                .last("limit " + limit));
        Collections.reverse(messageList);
        return BeanUtil.copyToList(messageList, ChatMessageDTO.class);
    }

}
