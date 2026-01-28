package com.lucky.ai.service;

import com.lucky.ai.controller.model.vo.chatRole.AiChatRolePageReqVO;
import com.lucky.ai.controller.model.vo.chatRole.AiChatRoleRespVO;
import com.lucky.ai.controller.model.vo.chatRole.AiChatRoleSaveMyReqVO;
import com.lucky.ai.controller.model.vo.chatRole.AiChatRoleSaveReqVO;
import com.lucky.ai.domain.AiChatRole;

import java.util.Collection;
import java.util.List;

/**
 * AI 聊天角色 Service 接口
 *
 * @author lucky
 */
public interface IAiChatRoleService {

    /**
     * 创建聊天角色
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createChatRole(AiChatRoleSaveReqVO createReqVO);

    /**
     * 创建【我的】聊天角色
     *
     * @param createReqVO 创建信息
     * @param userId      用户编号
     * @return 编号
     */
    Long createChatRoleMy(AiChatRoleSaveMyReqVO createReqVO, Long userId);

    /**
     * 更新聊天角色
     *
     * @param updateReqVO 更新信息
     */
    int updateChatRole(AiChatRoleSaveReqVO updateReqVO);

    /**
     * 更新【我的】聊天角色
     *
     * @param updateReqVO 更新信息
     * @param userId      用户编号
     */
    int updateChatRoleMy(AiChatRoleSaveMyReqVO updateReqVO, Long userId);

    /**
     * 删除聊天角色
     *
     * @param id 编号
     */
    int deleteChatRole(Long id);

    /**
     * 删除【我的】聊天角色
     *
     * @param id     编号
     * @param userId 用户编号
     */
    int deleteChatRoleMy(Long id, Long userId);

    /**
     * 获得聊天角色
     *
     * @param id 编号
     * @return AI 聊天角色
     */
    AiChatRoleRespVO getChatRole(Long id);

    /**
     * 获得聊天角色列表
     *
     * @param ids 编号数组
     * @return 聊天角色列表
     */
    List<AiChatRole> getChatRoleList(Collection<Long> ids);

    /**
     * 校验聊天角色是否合法
     *
     * @param id 角色编号
     */
    AiChatRole validateChatRole(Long id);

    /**
     * 获得聊天角色分页
     *
     * @param pageReqVO 分页查询
     * @return 聊天角色分页
     */
    List<AiChatRoleRespVO> getChatRolePage(AiChatRolePageReqVO pageReqVO);

    /**
     * 获得【我的】聊天角色分页
     *
     * @param pageReqVO 分页查询
     * @param userId    用户编号
     * @return 聊天角色分页
     */
    List<AiChatRoleRespVO> getChatRoleMyPage(AiChatRolePageReqVO pageReqVO, Long userId);

    /**
     * 获得聊天角色的分类列表
     *
     * @return 分类列表
     */
    List<String> getChatRoleCategoryList();

    /**
     * 根据名字获得聊天角色
     *
     * @param name 名字
     * @return 聊天角色列表
     */
    List<AiChatRole> getChatRoleListByName(String name);

}
