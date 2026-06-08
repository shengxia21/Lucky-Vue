package com.lucky.ai.controller.chat;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.ObjUtil;
import com.lucky.ai.domain.query.conversation.AiChatConversationCreateMyQuery;
import com.lucky.ai.domain.query.conversation.AiChatConversationPageQuery;
import com.lucky.ai.domain.query.conversation.AiChatConversationUpdateMyQuery;
import com.lucky.ai.domain.vo.conversation.AiChatConversationVO;
import com.lucky.ai.service.IAiChatConversationService;
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
 * AI 聊天对话Controller
 *
 * @author lucky
 */
@RestController
@RequestMapping("/ai/chat/conversation")
public class AiChatConversationController extends BaseController {

    @Resource
    private IAiChatConversationService chatConversationService;

    /**
     * 创建我的聊天对话
     */
    @Log(title = "创建【我的】聊天对话", businessType = BusinessType.INSERT)
    @PostMapping("/create-my")
    public R<Long> createChatConversationMy(@RequestBody AiChatConversationCreateMyQuery query) {
        return R.ok(chatConversationService.createChatConversationMy(query));
    }

    /**
     * 更新我的聊天对话
     */
    @Log(title = "更新【我的】聊天对话", businessType = BusinessType.UPDATE)
    @PutMapping("/update-my")
    public R<Void> updateChatConversationMy(@Validated @RequestBody AiChatConversationUpdateMyQuery query) {
        return toAjax(chatConversationService.updateChatConversationMy(query));
    }

    /**
     * 获得我的聊天对话列表
     */
    @GetMapping("/my-list")
    public R<List<AiChatConversationVO>> getMyChatConversationList() {
        List<AiChatConversationVO> list = chatConversationService.getMyChatConversationList();
        transService.transBatch(list);
        return R.ok(list);
    }

    /**
     * 获得我的聊天对话
     */
    @GetMapping("/get-my")
    public R<AiChatConversationVO> getChatConversationMy(@RequestParam("id") Long id) {
        AiChatConversationVO conversation = chatConversationService.getChatConversationById(id);
        if (conversation != null && ObjUtil.notEqual(conversation.getUserId(), SecurityUtils.getUserId())) {
            return R.fail("对话不存在或不属于当前用户");
        }
        transService.transOne(conversation);
        return R.ok(conversation);
    }

    /**
     * 删除我的聊天对话
     */
    @Log(title = "删除【我的】聊天对话", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete-my")
    public R<Void> deleteChatConversationMy(@RequestParam("id") Long id) {
        return toAjax(chatConversationService.deleteChatConversationMyById(id));
    }

    /**
     * 删除未置顶的聊天对话
     */
    @Log(title = "删除未置顶的聊天对话", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete-by-unpinned")
    public R<Void> deleteChatConversationMyByUnpinned() {
        return toAjax(chatConversationService.deleteChatConversationMy());
    }

    // ========== 对话管理 ==========

    /**
     * 获取对话分页列表
     */
    @SaCheckPermission("ai:chat-conversation:list")
    @GetMapping("/page")
    public TableDataInfo<AiChatConversationVO> getChatConversationPage(PageQuery pageQuery, AiChatConversationPageQuery query) {
        TableDataInfo<AiChatConversationVO> page = chatConversationService.getChatConversationPage(pageQuery, query);
        transService.transBatch(page.getRows());
        return page;
    }

    /**
     * 管理员删除对话
     */
    @Log(title = "管理员删除对话", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete-by-admin")
    @SaCheckPermission("ai:chat-conversation:delete")
    public R<Void> deleteChatConversationById(@RequestParam("id") Long id) {
        return toAjax(chatConversationService.deleteChatConversationById(id));
    }

}
