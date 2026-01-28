package com.lucky.ai.mapper;

import com.lucky.ai.controller.model.vo.chatRole.AiChatRolePageReqVO;
import com.lucky.ai.controller.model.vo.chatRole.AiChatRoleRespVO;
import com.lucky.ai.domain.AiChatRole;

import java.util.Collection;
import java.util.List;

/**
 * AI 聊天角色Mapper接口
 *
 * @author lucky
 */
public interface AiChatRoleMapper {

    /**
     * 通过主键查询AI 聊天角色
     *
     * @param id 主键
     * @return AI 聊天角色
     */
    AiChatRole selectById(Long id);

    /**
     * 通过主键查询AI 聊天角色
     *
     * @param id 主键
     * @return AI 聊天角色
     */
    AiChatRoleRespVO selectAiChatRoleById(Long id);

    /**
     * 新增AI 聊天角色
     *
     * @param aiChatRole AI 聊天角色
     * @return 结果
     */
    int insertAiChatRole(AiChatRole aiChatRole);

    /**
     * 更新AI 聊天角色
     *
     * @param aiChatRole AI 聊天角色
     * @return 结果
     */
    int updateAiChatRoleById(AiChatRole aiChatRole);

    /**
     * 删除AI 聊天角色
     *
     * @param id 主键
     * @return 结果
     */
    int deleteAiChatRoleById(Long id);

    /**
     * 批量查询AI 聊天角色
     *
     * @param ids 主键数组
     * @return AI 聊天角色列表
     */
    List<AiChatRole> selectByIds(Collection<Long> ids);

    /**
     * 查询AI 聊天角色分页
     *
     * @param pageReqVO 分页查询
     * @return AI 聊天角色分页
     */
    List<AiChatRoleRespVO> selectPage(AiChatRolePageReqVO pageReqVO);

    /**
     * 查询【我的】聊天角色分页
     *
     * @param pageReqVO 分页查询
     * @param userId    用户编号
     * @return AI 聊天角色分页
     */
    List<AiChatRoleRespVO> selectPageByMy(AiChatRolePageReqVO pageReqVO, Long userId);

    /**
     * 查询AI 聊天角色列表
     *
     * @param status 角色状态
     * @return AI 聊天角色列表
     */
    List<String> selectListGroupByCategory(Integer status);

    /**
     * 查询AI 聊天角色列表
     *
     * @param name 名称
     * @return AI 聊天角色列表
     */
    List<AiChatRole> selectListByName(String name);

}
