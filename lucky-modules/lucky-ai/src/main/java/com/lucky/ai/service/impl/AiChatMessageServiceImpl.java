package com.lucky.ai.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.ai.domain.query.message.AiChatMessageQuery;
import com.lucky.ai.domain.vo.message.AiChatMessageVO;
import com.lucky.ai.mapper.AiChatMessageMapper;
import com.lucky.ai.service.IAiChatMessageService;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

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
    public List<AiChatMessageVO> selectMyChatMessageListByConversationId(Long conversationId) {
        return chatMessageMapper.selectMyListByConversationId(conversationId);
    }

    @Override
    public int deleteMyChatMessageById(Long id) {
        return chatMessageMapper.deleteMyById(id);
    }

    @Override
    public int deleteMyChatMessageByConversationId(Long conversationId) {
        return chatMessageMapper.deleteMyByConversationId(conversationId);
    }

    @Override
    public TableDataInfo<AiChatMessageVO> selectChatMessageList(PageQuery pageQuery, AiChatMessageQuery query) {
        IPage<AiChatMessageVO> page = chatMessageMapper.selectPage(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    @Override
    public int deleteChatMessageByIds(Long[] ids) {
        return chatMessageMapper.deleteByIds(Arrays.asList(ids));
    }

}