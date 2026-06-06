package com.lucky.ai.controller.model;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.ObjUtil;
import com.lucky.ai.domain.query.chatRole.AiChatRolePageQuery;
import com.lucky.ai.domain.query.chatRole.AiChatRoleSaveMyQuery;
import com.lucky.ai.domain.query.chatRole.AiChatRoleSaveQuery;
import com.lucky.ai.domain.vo.chatRole.AiChatRoleVO;
import com.lucky.ai.service.IAiChatRoleService;
import com.lucky.common.core.domain.R;
import com.lucky.common.log.annotation.Log;
import com.lucky.common.log.enums.BusinessType;
import com.lucky.common.mybatis.core.controller.BaseController;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AI 聊天角色 Controller
 *
 * @author lucky
 */
@RestController
@RequestMapping("/ai/chat-role")
public class AiChatRoleController extends BaseController {

    @Resource
    private IAiChatRoleService chatRoleService;

    /**
     * 获得【我的】聊天角色分页
     */
    @GetMapping("/my-page")
    public TableDataInfo<AiChatRoleVO> getChatRoleMyPage(PageQuery pageQuery, AiChatRolePageQuery query) {
        TableDataInfo<AiChatRoleVO> page = chatRoleService.getChatRoleMyPage(pageQuery, query, getUserId());
        transService.transBatch(page.getRows());
        return page;
    }

    /**
     * 获得【我的】聊天角色
     */
    @GetMapping("/get-my")
    public R<AiChatRoleVO> getChatRoleMy(@RequestParam("id") Long id) {
        AiChatRoleVO chatRole = chatRoleService.getChatRoleById(id);
        if (ObjUtil.notEqual(chatRole.getUserId(), getUserId())) {
            return R.fail("聊天角色不属于您");
        }
        transService.transOne(chatRole);
        return R.ok(chatRole);
    }

    /**
     * 创建【我的】聊天角色
     */
    @Log(title = "创建【我的】聊天角色", businessType = BusinessType.INSERT)
    @PostMapping("/create-my")
    public R<Long> createChatRoleMy(@Validated @RequestBody AiChatRoleSaveMyQuery query) {
        return R.ok(chatRoleService.createChatRoleMy(query, getUserId()));
    }

    /**
     * 更新【我的】聊天角色
     */
    @Log(title = "更新【我的】聊天角色", businessType = BusinessType.UPDATE)
    @PutMapping("/update-my")
    public R<Void> updateChatRoleMy(@Validated @RequestBody AiChatRoleSaveMyQuery query) {
        return toAjax(chatRoleService.updateChatRoleMy(query, getUserId()));
    }

    /**
     * 删除【我的】聊天角色
     */
    @Log(title = "删除【我的】聊天角色", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete-my")
    public R<Void> deleteChatRoleMy(@RequestParam("id") Long id) {
        return toAjax(chatRoleService.deleteChatRoleMy(id, getUserId()));
    }

    /**
     * 获得聊天角色的分类列表
     */
    @GetMapping("/category-list")
    public R<List<String>> getChatRoleCategoryList() {
        return R.ok(chatRoleService.getChatRoleCategoryList());
    }

    // ========== 角色管理 ==========

    /**
     * 创建聊天角色
     */
    @Log(title = "创建聊天角色", businessType = BusinessType.INSERT)
    @SaCheckPermission("ai:chat-role:create")
    @PostMapping("/create")
    public R<Long> createChatRole(@Validated @RequestBody AiChatRoleSaveQuery query) {
        return R.ok(chatRoleService.createChatRole(query));
    }

    /**
     * 更新聊天角色
     */
    @Log(title = "更新聊天角色", businessType = BusinessType.UPDATE)
    @SaCheckPermission("ai:chat-role:update")
    @PutMapping("/update")
    public R<Void> updateChatRole(@Validated @RequestBody AiChatRoleSaveQuery query) {
        return toAjax(chatRoleService.updateChatRole(query));
    }

    /**
     * 删除聊天角色
     */
    @Log(title = "删除聊天角色", businessType = BusinessType.DELETE)
    @SaCheckPermission("ai:chat-role:delete")
    @DeleteMapping("/delete")
    public R<Void> deleteChatRole(@RequestParam("id") Long id) {
        return toAjax(chatRoleService.deleteChatRoleById(id));
    }

    /**
     * 获得聊天角色
     */
    @SaCheckPermission("ai:chat-role:query")
    @GetMapping("/get")
    public R<AiChatRoleVO> getChatRole(@RequestParam("id") Long id) {
        AiChatRoleVO vo = chatRoleService.getChatRoleById(id);
        transService.transOne(vo);
        return R.ok(vo);
    }

    /**
     * 获得聊天角色分页
     */
    @SaCheckPermission("ai:chat-role:list")
    @GetMapping("/page")
    public TableDataInfo<AiChatRoleVO> getChatRolePage(PageQuery pageQuery, AiChatRolePageQuery query) {
        TableDataInfo<AiChatRoleVO> page = chatRoleService.getChatRolePage(pageQuery, query);
        transService.transBatch(page.getRows());
        return page;
    }

}