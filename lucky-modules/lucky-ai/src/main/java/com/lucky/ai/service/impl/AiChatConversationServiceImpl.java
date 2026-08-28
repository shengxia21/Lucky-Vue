package com.lucky.ai.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.AiChatRole;
import com.lucky.ai.domain.query.conversation.AiChatConversationCreateMyQuery;
import com.lucky.ai.domain.query.conversation.AiChatConversationQuery;
import com.lucky.ai.domain.query.conversation.AiChatConversationUpdateMyQuery;
import com.lucky.ai.domain.vo.conversation.AiChatConversationVO;
import com.lucky.ai.mapper.AiChatConversationMapper;
import com.lucky.ai.mapper.AiChatMessageMapper;
import com.lucky.ai.service.IAiChatConversationService;
import com.lucky.ai.service.IAiChatRoleService;
import com.lucky.common.core.constant.AiConstants;
import com.lucky.common.core.enums.YesNo;
import com.lucky.common.core.exception.ServiceException;
import com.lucky.common.core.utils.MapstructUtils;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import com.lucky.common.security.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * AI 聊天对话Service业务层处理
 *
 * @author lucky
 */
@Service
public class AiChatConversationServiceImpl implements IAiChatConversationService {

    @Resource
    private AiChatConversationMapper chatConversationMapper;

    @Resource
    private AiChatMessageMapper chatMessageMapper;

    @Resource
    private IAiChatRoleService chatRoleService;

    @Override
    public List<AiChatConversationVO> selectMyChatConversationList() {
        return chatConversationMapper.selectMyList();
    }

    @Override
    public AiChatConversationVO selectMyChatConversationById(Long id) {
        return chatConversationMapper.selectMyById(id);
    }

    @Override
    public Long insertMyChatConversation(AiChatConversationCreateMyQuery query) {
        // 若角色id不为空，校验聊天角色是否有效
        AiChatRole role = query.getRoleId() != null ? chatRoleService.validateChatRole(query.getRoleId()) : null;

        AiChatConversation conversation = new AiChatConversation();
        conversation.setUserId(SecurityUtils.getUserId());
        conversation.setPinned(YesNo.NO.getCode());
        conversation.setRoleId(query.getRoleId());
        conversation.setHistoryMessageCount(10);
        if (role != null) {
            // 有角色id，标题为角色名称
            conversation.setTitle(role.getName());
        } else {
            // 没有角色id，判断聊天内容是否存在字符
            if (StrUtil.isNotBlank(query.getContent())) {
                // 存在字符，标题截取至聊天内容的前12位
                conversation.setTitle(StrUtil.maxLength(query.getContent(), 12));
            } else {
                // 不存在字符，使用默认标题
                conversation.setTitle(AiConstants.TITLE_DEFAULT);
            }
        }
        chatConversationMapper.insert(conversation);
        // 返回创建后的对话id
        return conversation.getId();
    }

    @Override
    public int updateMyChatConversation(AiChatConversationUpdateMyQuery query) {
        AiChatConversation updateObj = MapstructUtils.convert(query, AiChatConversation.class);
        return chatConversationMapper.updateById(updateObj);
    }

    @Override
    public int deleteMyChatConversationById(Long id) {
        return chatConversationMapper.deleteMyById(id);
    }

    @Override
    public int deleteMyUnpinnedChatConversation() {
        return chatConversationMapper.deleteMyUnpinned();
    }

    @Override
    public TableDataInfo<AiChatConversationVO> selectChatConversationList(PageQuery pageQuery, AiChatConversationQuery query) {
        IPage<AiChatConversationVO> page = chatConversationMapper.selectPage(pageQuery.build(), query);
        if (CollUtil.isEmpty(page.getRecords())) {
            return TableDataInfo.build(page);
        }
        // 收集对话ID列表
        List<Long> ids = page.getRecords().stream().map(AiChatConversationVO::getId).toList();
        // 查询对话ID列表的消息数量
        Map<Long, Integer> countMap = chatMessageMapper.selectCountByConversationIds(ids);
        // 添加消息数量
        page.getRecords().forEach(conversation -> conversation.setMessageTotal(countMap.getOrDefault(conversation.getId(), 0)));
        return TableDataInfo.build(page);
    }

    @Override
    public AiChatConversation validateChatConversationExists(Long id) {
        AiChatConversation conversation = chatConversationMapper.selectById(id);
        if (conversation == null) {
            throw new ServiceException(AiConstants.CHAT_CONVERSATION_NOT_EXISTS);
        }
        return conversation;
    }

}