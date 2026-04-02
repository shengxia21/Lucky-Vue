package com.lucky.ai.service;

import com.lucky.ai.domain.AiChatRole;
import com.lucky.ai.domain.query.chatRole.AiChatRolePageQuery;
import com.lucky.ai.domain.query.chatRole.AiChatRoleSaveMyQuery;
import com.lucky.ai.domain.query.chatRole.AiChatRoleSaveQuery;
import com.lucky.ai.domain.vo.chatRole.AiChatRoleVO;
import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * AI 聊天角色 Service 接口
 *
 * @author lucky
 */
public interface AiChatRoleService {

    /**
     * 创建聊天角色
     *
     * @param query 创建信息
     * @return 编号
     */
    Long createChatRole(AiChatRoleSaveQuery query);

    /**
     * 创建【我的】聊天角色
     *
     * @param query 创建信息
     * @param userId      用户编号
     * @return 编号
     */
    Long createChatRoleMy(AiChatRoleSaveMyQuery query, Long userId);

    /**
     * 更新聊天角色
     *
     * @param query 更新信息
     */
    int updateChatRole(AiChatRoleSaveQuery query);

    /**
     * 更新【我的】聊天角色
     *
     * @param query 更新信息
     * @param userId      用户编号
     */
    int updateChatRoleMy(AiChatRoleSaveMyQuery query, Long userId);

    /**
     * 删除聊天角色
     *
     * @param id 编号
     */
    int deleteChatRoleById(Long id);

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
    AiChatRoleVO getChatRoleById(Long id);

    /**
     * 获得聊天角色列表
     *
     * @param ids 编号数组
     * @return 聊天角色列表
     */
    List<AiChatRoleVO> getChatRoleList(Collection<Long> ids);

    /**
     * 校验聊天角色是否合法
     *
     * @param id 角色编号
     */
    AiChatRole validateChatRole(Long id);

    /**
     * 获得聊天角色分页
     *
     * @param pageQuery 分页查询对象
     * @param query 查询参数
     * @return 聊天角色分页
     */
    TableDataInfo<AiChatRoleVO> getChatRolePage(PageQuery pageQuery, AiChatRolePageQuery query);

    /**
     * 获得【我的】聊天角色分页
     *
     * @param pageQuery 分页查询对象
     * @param query 查询参数
     * @param userId    用户编号
     * @return 聊天角色分页
     */
    TableDataInfo<AiChatRoleVO> getChatRoleMyPage(PageQuery pageQuery, AiChatRolePageQuery query, Long userId);

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
    List<AiChatRoleVO> getChatRoleListByName(String name);

}