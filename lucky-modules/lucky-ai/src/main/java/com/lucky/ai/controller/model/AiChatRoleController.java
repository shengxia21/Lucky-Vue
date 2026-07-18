package com.lucky.ai.controller.model;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lucky.ai.domain.query.chatRole.AiChatRoleMyQuery;
import com.lucky.ai.domain.query.chatRole.AiChatRoleQuery;
import com.lucky.ai.domain.query.chatRole.AiChatRoleSaveMyQuery;
import com.lucky.ai.domain.query.chatRole.AiChatRoleSaveQuery;
import com.lucky.ai.domain.vo.chatRole.AiChatRoleVO;
import com.lucky.ai.service.IAiChatRoleService;
import com.lucky.common.core.domain.R;
import com.lucky.common.core.validate.Update;
import com.lucky.common.log.annotation.Log;
import com.lucky.common.log.enums.BusinessType;
import com.lucky.common.mybatis.core.controller.BaseController;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * AI 聊天角色 Controller
 *
 * @author lucky
 */
@RestController
@RequestMapping("/ai/chatRole")
public class AiChatRoleController extends BaseController {

    @Resource
    private IAiChatRoleService chatRoleService;

    /**
     * 获得【我的】聊天角色分页
     */
    @GetMapping("/my/list")
    public TableDataInfo<AiChatRoleVO> myList(PageQuery pageQuery, AiChatRoleMyQuery query) {
        return chatRoleService.selectMyChatRoleList(pageQuery, query);
    }

    /**
     * 获得【我的】聊天角色
     */
    @GetMapping("/my/{id}")
    public R<AiChatRoleVO> getMyInfo(@PathVariable Long id) {
        return R.ok(chatRoleService.selectMyChatRoleById(id));
    }

    /**
     * 创建【我的】聊天角色
     */
    @Log(title = "创建【我的】聊天角色", businessType = BusinessType.INSERT)
    @PostMapping("/my")
    public R<Void> addMy(@Validated @RequestBody AiChatRoleSaveMyQuery query) {
        return toAjax(chatRoleService.insertMyChatRole(query));
    }

    /**
     * 更新【我的】聊天角色
     */
    @Log(title = "更新【我的】聊天角色", businessType = BusinessType.UPDATE)
    @PutMapping("/my")
    public R<Void> editMy(@Validated({Update.class}) @RequestBody AiChatRoleSaveMyQuery query) {
        return toAjax(chatRoleService.updateMyChatRole(query));
    }

    /**
     * 删除【我的】聊天角色
     */
    @Log(title = "删除【我的】聊天角色", businessType = BusinessType.DELETE)
    @DeleteMapping("/my/{id}")
    public R<Void> removeMy(@PathVariable Long id) {
        return toAjax(chatRoleService.deleteMyChatRoleById(id));
    }

    // ========== 角色管理 ==========

    /**
     * 获得聊天角色分页
     */
    @SaCheckPermission("ai:chat-role:list")
    @GetMapping("/list")
    public TableDataInfo<AiChatRoleVO> list(PageQuery pageQuery, AiChatRoleQuery query) {
        return chatRoleService.selectChatRoleList(pageQuery, query);
    }

    /**
     * 获得聊天角色
     */
    @SaCheckPermission("ai:chat-role:query")
    @GetMapping("/{id}")
    public R<AiChatRoleVO> getInfo(@PathVariable Long id) {
        return R.ok(chatRoleService.selectChatRoleById(id));
    }

    /**
     * 创建聊天角色
     */
    @Log(title = "创建聊天角色", businessType = BusinessType.INSERT)
    @SaCheckPermission("ai:chat-role:add")
    @PostMapping
    public R<Void> add(@Validated @RequestBody AiChatRoleSaveQuery query) {
        return toAjax(chatRoleService.insertChatRole(query));
    }

    /**
     * 更新聊天角色
     */
    @Log(title = "更新聊天角色", businessType = BusinessType.UPDATE)
    @SaCheckPermission("ai:chat-role:edit")
    @PutMapping
    public R<Void> edit(@Validated({Update.class}) @RequestBody AiChatRoleSaveQuery query) {
        return toAjax(chatRoleService.updateChatRole(query));
    }

    /**
     * 删除聊天角色
     */
    @Log(title = "删除聊天角色", businessType = BusinessType.DELETE)
    @SaCheckPermission("ai:chat-role:remove")
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(chatRoleService.deleteChatRoleByIds(ids));
    }

}