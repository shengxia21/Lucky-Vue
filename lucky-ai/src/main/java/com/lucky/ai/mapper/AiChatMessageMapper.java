package com.lucky.ai.mapper;

import com.lucky.ai.controller.chat.vo.message.AiChatMessagePageReqVO;
import com.lucky.ai.controller.chat.vo.message.AiChatMessageRespVO;
import com.lucky.ai.domain.AiChatMessage;

import java.util.List;

/**
 * AI 聊天消息Mapper接口
 *
 * @author lucky
 */
public interface AiChatMessageMapper {

    /**
     * 查询AI 聊天消息
     *
     * @param id AI 聊天消息主键
     * @return AI 聊天消息
     */
    AiChatMessage selectAiChatMessageById(Long id);

    /**
     * 新增AI 聊天消息
     *
     * @param aiChatMessage AI 聊天消息
     * @return 结果
     */
    int insertAiChatMessage(AiChatMessage aiChatMessage);

    /**
     * 修改AI 聊天消息
     *
     * @param aiChatMessage AI 聊天消息
     * @return 结果
     */
    int updateAiChatMessage(AiChatMessage aiChatMessage);

    /**
     * 删除AI 聊天消息
     *
     * @param id AI 聊天消息主键
     * @return 结果
     */
    int deleteAiChatMessageById(Long id);

    /**
     * 批量删除AI 聊天消息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteAiChatMessageByIds(List<Long> ids);

    /**
     * 根据会话ID查询聊天消息列表
     *
     * @param id 会话ID
     * @return 聊天消息列表
     */
    List<AiChatMessage> selectListByConversationId(Long id);

    /**
     * 查询聊天消息分页列表
     *
     * @param pageReqVO 分页查询参数
     * @return 聊天消息分页列表
     */
    List<AiChatMessageRespVO> selectChatMessagePage(AiChatMessagePageReqVO pageReqVO);

}
