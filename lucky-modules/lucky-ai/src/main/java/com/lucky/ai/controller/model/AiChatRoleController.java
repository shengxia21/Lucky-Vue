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
import com.lucky.common.security.utils.SecurityUtils;
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
@RequestMapping("/ai/chatRole")
public class AiChatRoleController extends BaseController {

    @Resource
    private IAiChatRoleService chatRoleService;

    /**
     * 获得【我的】聊天角色分页
     */
    @GetMapping("/my/list")
    public TableDataInfo<AiChatRoleVO> myList(PageQuery pageQuery, AiChatRolePageQuery query) {
        TableDataInfo<AiChatRoleVO> page = chatRoleService.selectMyChatRoleList(pageQuery, query);
        transService.transBatch(page.getRows());
        return page;
    }

    /**
     * 获得【我的】聊天角色
     */
    @GetMapping("/my/{id}")
    public R<AiChatRoleVO> getMyInfo(@PathVariable Long id) {
        AiChatRoleVO chatRole = chatRoleService.selectChatRoleById(id);
        if (ObjUtil.notEqual(chatRole.getUserId(), SecurityUtils.getUserId())) {
            return R.fail("聊天角色不属于您");
        }
        transService.transOne(chatRole);
        return R.ok(chatRole);
    }

    /**
     * 创建【我的】聊天角色
     */
    @Log(title = "创建【我的】聊天角色", businessType = BusinessType.INSERT)
    @PostMapping("/my")
    public R<Long> addMy(@Validated @RequestBody AiChatRoleSaveMyQuery query) {
        return R.ok(chatRoleService.insertMyChatRole(query));
    }

    /**
     * 更新【我的】聊天角色
     */
    @Log(title = "更新【我的】聊天角色", businessType = BusinessType.UPDATE)
    @PutMapping("/my")
    public R<Void> editMy(@Validated @RequestBody AiChatRoleSaveMyQuery query) {
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

    /**
     * 获得聊天角色的分类列表
     */
    @GetMapping("/categoryList")
    public R<List<String>> categoryList() {
        return R.ok(chatRoleService.selectChatRoleCategoryList());
    }

    // ========== 角色管理 ==========

    /**
     * 创建聊天角色
     */
    @Log(title = "创建聊天角色", businessType = BusinessType.INSERT)
    @SaCheckPermission("ai:chat-role:add")
    @PostMapping
    public R<Long> add(@Validated @RequestBody AiChatRoleSaveQuery query) {
        return R.ok(chatRoleService.insertChatRole(query));
    }

    /**
     * 更新聊天角色
     */
    @Log(title = "更新聊天角色", businessType = BusinessType.UPDATE)
    @SaCheckPermission("ai:chat-role:edit")
    @PutMapping
    public R<Void> edit(@Validated @RequestBody AiChatRoleSaveQuery query) {
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

    /**
     * 获得聊天角色
     */
    @SaCheckPermission("ai:chat-role:query")
    @GetMapping("/{id}")
    public R<AiChatRoleVO> getInfo(@PathVariable Long id) {
        AiChatRoleVO vo = chatRoleService.selectChatRoleById(id);
        transService.transOne(vo);
        return R.ok(vo);
    }

    /**
     * 获得聊天角色分页
     */
    @SaCheckPermission("ai:chat-role:list")
    @GetMapping("/list")
    public TableDataInfo<AiChatRoleVO> list(PageQuery pageQuery, AiChatRolePageQuery query) {
        TableDataInfo<AiChatRoleVO> page = chatRoleService.selectChatRoleList(pageQuery, query);
        transService.transBatch(page.getRows());
        return page;
    }

}