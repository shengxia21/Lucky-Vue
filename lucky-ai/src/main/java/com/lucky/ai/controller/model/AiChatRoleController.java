package com.lucky.ai.controller.model;

import cn.hutool.core.util.ObjUtil;
import com.lucky.ai.domain.query.chatRole.ChatRolePageQuery;
import com.lucky.ai.domain.query.chatRole.ChatRoleSaveMyQuery;
import com.lucky.ai.domain.query.chatRole.ChatRoleSaveQuery;
import com.lucky.ai.domain.vo.chatRole.ChatRoleVO;
import com.lucky.ai.service.AiChatRoleService;
import com.lucky.common.annotation.Log;
import com.lucky.common.core.controller.BaseController;
import com.lucky.common.core.domain.R;
import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.common.enums.BusinessType;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private AiChatRoleService chatRoleService;

    /**
     * 获得【我的】聊天角色分页
     */
    @GetMapping("/my-page")
    public TableDataInfo<ChatRoleVO> getChatRoleMyPage(PageQuery pageQuery, ChatRolePageQuery query) {
        return chatRoleService.getChatRoleMyPage(pageQuery, query, getUserId());
    }

    /**
     * 获得【我的】聊天角色
     */
    @GetMapping("/get-my")
    public R<ChatRoleVO> getChatRoleMy(@RequestParam("id") Long id) {
        ChatRoleVO chatRole = chatRoleService.getChatRoleById(id);
        if (ObjUtil.notEqual(chatRole.getUserId(), getUserId())) {
            return R.fail("聊天角色不属于您");
        }
        return R.ok(chatRole);
    }

    /**
     * 创建【我的】聊天角色
     */
    @Log(title = "创建【我的】聊天角色", businessType = BusinessType.INSERT)
    @PostMapping("/create-my")
    public R<Long> createChatRoleMy(@Validated @RequestBody ChatRoleSaveMyQuery query) {
        return R.ok(chatRoleService.createChatRoleMy(query, getUserId()));
    }

    /**
     * 更新【我的】聊天角色
     */
    @Log(title = "更新【我的】聊天角色", businessType = BusinessType.UPDATE)
    @PutMapping("/update-my")
    public R<Integer> updateChatRoleMy(@Validated @RequestBody ChatRoleSaveMyQuery query) {
        return R.ok(chatRoleService.updateChatRoleMy(query, getUserId()));
    }

    /**
     * 删除【我的】聊天角色
     */
    @Log(title = "删除【我的】聊天角色", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete-my")
    public R<Integer> deleteChatRoleMy(@RequestParam("id") Long id) {
        return R.ok(chatRoleService.deleteChatRoleMy(id, getUserId()));
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
    @PreAuthorize("@ss.hasPermi('ai:chat-role:create')")
    @PostMapping("/create")
    public R<Long> createChatRole(@Validated @RequestBody ChatRoleSaveQuery query) {
        return R.ok(chatRoleService.createChatRole(query));
    }

    /**
     * 更新聊天角色
     */
    @Log(title = "更新聊天角色", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('ai:chat-role:update')")
    @PutMapping("/update")
    public R<Integer> updateChatRole(@Validated @RequestBody ChatRoleSaveQuery query) {
        return R.ok(chatRoleService.updateChatRole(query));
    }

    /**
     * 删除聊天角色
     */
    @Log(title = "删除聊天角色", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('ai:chat-role:delete')")
    @DeleteMapping("/delete")
    public R<Integer> deleteChatRole(@RequestParam("id") Long id) {
        return R.ok(chatRoleService.deleteChatRoleById(id));
    }

    /**
     * 获得聊天角色
     */
    @PreAuthorize("@ss.hasPermi('ai:chat-role:query')")
    @GetMapping("/get")
    public R<ChatRoleVO> getChatRole(@RequestParam("id") Long id) {
        return R.ok(chatRoleService.getChatRoleById(id));
    }

    /**
     * 获得聊天角色分页
     */
    @PreAuthorize("@ss.hasPermi('ai:chat-role:list')")
    @GetMapping("/page")
    public TableDataInfo<ChatRoleVO> getChatRolePage(PageQuery pageQuery, ChatRolePageQuery query) {
        return chatRoleService.getChatRolePage(pageQuery, query);
    }

}