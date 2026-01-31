package com.lucky.ai.controller.model;

import cn.hutool.core.util.ObjUtil;
import com.lucky.ai.controller.model.vo.chatRole.AiChatRolePageReqVO;
import com.lucky.ai.controller.model.vo.chatRole.AiChatRoleRespVO;
import com.lucky.ai.controller.model.vo.chatRole.AiChatRoleSaveMyReqVO;
import com.lucky.ai.controller.model.vo.chatRole.AiChatRoleSaveReqVO;
import com.lucky.ai.service.IAiChatRoleService;
import com.lucky.common.annotation.Log;
import com.lucky.common.core.controller.BaseController;
import com.lucky.common.core.domain.AjaxResult;
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
    private IAiChatRoleService aiChatRoleService;

    /**
     * 获得【我的】聊天角色分页
     */
    @GetMapping("/my-page")
    public TableDataInfo getChatRoleMyPage(AiChatRolePageReqVO pageReqVO) {
        startPage();
        List<AiChatRoleRespVO> list = aiChatRoleService.getChatRoleMyPage(pageReqVO, getUserId());
        return getDataTable(list);
    }

    /**
     * 获得【我的】聊天角色
     */
    @GetMapping("/get-my")
    public AjaxResult getChatRoleMy(@RequestParam("id") Long id) {
        AiChatRoleRespVO chatRole = aiChatRoleService.getChatRole(id);
        if (ObjUtil.notEqual(chatRole.getUserId(), getUserId())) {
            return success(null);
        }
        return success(chatRole);
    }

    /**
     * 创建【我的】聊天角色
     */
    @Log(title = "创建【我的】聊天角色", businessType = BusinessType.INSERT)
    @PostMapping("/create-my")
    public AjaxResult createChatRoleMy(@Validated @RequestBody AiChatRoleSaveMyReqVO createReqVO) {
        return success(aiChatRoleService.createChatRoleMy(createReqVO, getUserId()));
    }

    /**
     * 更新【我的】聊天角色
     */
    @Log(title = "更新【我的】聊天角色", businessType = BusinessType.UPDATE)
    @PutMapping("/update-my")
    public AjaxResult updateChatRoleMy(@Validated @RequestBody AiChatRoleSaveMyReqVO updateReqVO) {
        return success(aiChatRoleService.updateChatRoleMy(updateReqVO, getUserId()));
    }

    /**
     * 删除【我的】聊天角色
     */
    @Log(title = "删除【我的】聊天角色", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete-my")
    public AjaxResult deleteChatRoleMy(@RequestParam("id") Long id) {
        return success(aiChatRoleService.deleteChatRoleMy(id, getUserId()));
    }

    /**
     * 获得聊天角色的分类列表
     */
    @GetMapping("/category-list")
    public AjaxResult getChatRoleCategoryList() {
        return success(aiChatRoleService.getChatRoleCategoryList());
    }

    // ========== 角色管理 ==========

    /**
     * 创建聊天角色
     */
    @Log(title = "创建聊天角色", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('ai:chat-role:create')")
    @PostMapping("/create")
    public AjaxResult createChatRole(@Validated @RequestBody AiChatRoleSaveReqVO createReqVO) {
        return success(aiChatRoleService.createChatRole(createReqVO));
    }

    /**
     * 更新聊天角色
     */
    @Log(title = "更新聊天角色", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('ai:chat-role:update')")
    @PutMapping("/update")
    public AjaxResult updateChatRole(@Validated @RequestBody AiChatRoleSaveReqVO updateReqVO) {
        return success(aiChatRoleService.updateChatRole(updateReqVO));
    }

    /**
     * 删除聊天角色
     */
    @Log(title = "删除聊天角色", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('ai:chat-role:delete')")
    @DeleteMapping("/delete")
    public AjaxResult deleteChatRole(@RequestParam("id") Long id) {
        return success(aiChatRoleService.deleteChatRole(id));
    }

    /**
     * 获得聊天角色
     */
    @PreAuthorize("@ss.hasPermi('ai:chat-role:query')")
    @GetMapping("/get")
    public AjaxResult getChatRole(@RequestParam("id") Long id) {
        AiChatRoleRespVO chatRole = aiChatRoleService.getChatRole(id);
        return success(chatRole);
    }

    /**
     * 获得聊天角色分页
     */
    @PreAuthorize("@ss.hasPermi('ai:chat-role:list')")
    @GetMapping("/page")
    public TableDataInfo getChatRolePage(AiChatRolePageReqVO pageReqVO) {
        startPage();
        List<AiChatRoleRespVO> list = aiChatRoleService.getChatRolePage(pageReqVO);
        return getDataTable(list);
    }

}
