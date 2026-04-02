package com.lucky.ai.controller.chat;

import cn.hutool.core.util.ObjUtil;
import com.lucky.ai.domain.query.conversation.AiChatConversationCreateMyQuery;
import com.lucky.ai.domain.query.conversation.AiChatConversationPageQuery;
import com.lucky.ai.domain.query.conversation.AiChatConversationUpdateMyQuery;
import com.lucky.ai.domain.vo.conversation.AiChatConversationVO;
import com.lucky.ai.service.AiChatConversationService;
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
 * AI 聊天对话Controller
 *
 * @author lucky
 */
@RestController
@RequestMapping("/ai/chat/conversation")
public class AiChatConversationController extends BaseController {

    @Resource
    private AiChatConversationService chatConversationService;

    /**
     * 创建我的聊天对话
     */
    @Log(title = "创建【我的】聊天对话", businessType = BusinessType.INSERT)
    @PostMapping("/create-my")
    public R<Long> createChatConversationMy(@RequestBody AiChatConversationCreateMyQuery query) {
        return R.ok(chatConversationService.createChatConversationMy(query, getUserId()));
    }

    /**
     * 更新我的聊天对话
     */
    @Log(title = "更新【我的】聊天对话", businessType = BusinessType.UPDATE)
    @PutMapping("/update-my")
    public R<Integer> updateChatConversationMy(@Validated @RequestBody AiChatConversationUpdateMyQuery query) {
        return R.ok(chatConversationService.updateChatConversationMy(query, getUserId()));
    }

    /**
     * 获得我的聊天对话列表
     */
    @GetMapping("/my-list")
    public R<List<AiChatConversationVO>> getChatConversationMyList() {
        return R.ok(chatConversationService.getChatConversationListByUserId(getUserId()));
    }

    /**
     * 获得我的聊天对话
     */
    @GetMapping("/get-my")
    public R<AiChatConversationVO> getChatConversationMy(@RequestParam("id") Long id) {
        AiChatConversationVO conversation = chatConversationService.getChatConversationById(id);
        if (conversation != null && ObjUtil.notEqual(conversation.getUserId(), getUserId())) {
            return R.fail("对话不存在或不属于当前用户");
        }
        return R.ok(conversation);
    }

    /**
     * 删除我的聊天对话
     */
    @Log(title = "删除【我的】聊天对话", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete-my")
    public R<Integer> deleteChatConversationMy(@RequestParam("id") Long id) {
        return R.ok(chatConversationService.deleteChatConversationMyById(id, getUserId()));
    }

    /**
     * 删除未置顶的聊天对话
     */
    @Log(title = "删除未置顶的聊天对话", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete-by-unpinned")
    public R<Integer> deleteChatConversationMyByUnpinned() {
        return R.ok(chatConversationService.deleteChatConversationMy(getUserId()));
    }

    // ========== 对话管理 ==========

    /**
     * 获取对话分页列表
     */
    @PreAuthorize("@ss.hasPermi('ai:chat-conversation:list')")
    @GetMapping("/page")
    public TableDataInfo<AiChatConversationVO> getChatConversationPage(PageQuery pageQuery, AiChatConversationPageQuery query) {
        return chatConversationService.getChatConversationPage(pageQuery, query);
    }

    /**
     * 管理员删除对话
     */
    @Log(title = "管理员删除对话", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete-by-admin")
    @PreAuthorize("@ss.hasPermi('ai:chat-conversation:delete')")
    public R<Integer> deleteChatConversationById(@RequestParam("id") Long id) {
        return R.ok(chatConversationService.deleteChatConversationById(id));
    }

}
