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
    @PostMapping("/my")
    public R<Void> addMy(@RequestBody AiChatConversationCreateMyQuery query) {
        return toAjax(chatConversationService.insertMyChatConversation(query));
    }

    /**
     * 更新我的聊天对话
     */
    @Log(title = "更新【我的】聊天对话", businessType = BusinessType.UPDATE)
    @PutMapping("/my")
    public R<Void> editMy(@Validated @RequestBody AiChatConversationUpdateMyQuery query) {
        return toAjax(chatConversationService.updateMyChatConversation(query));
    }

    /**
     * 获得我的聊天对话列表
     */
    @GetMapping("/my/list")
    public R<List<AiChatConversationVO>> myList() {
        List<AiChatConversationVO> list = chatConversationService.selectMyChatConversationList();
        transService.transBatch(list);
        return R.ok(list);
    }

    /**
     * 获得我的聊天对话
     */
    @GetMapping("/my/{id}")
    public R<AiChatConversationVO> getMyInfo(@PathVariable Long id) {
        AiChatConversationVO conversation = chatConversationService.selectChatConversationById(id);
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
    @DeleteMapping("/my/{id}")
    public R<Void> removeMy(@PathVariable Long id) {
        return toAjax(chatConversationService.deleteMyChatConversationById(id));
    }

    /**
     * 删除我的未置顶聊天对话
     */
    @Log(title = "删除【我的】未置顶聊天对话", businessType = BusinessType.DELETE)
    @DeleteMapping("/my/unpinned")
    public R<Void> removeMyUnpinned() {
        return toAjax(chatConversationService.deleteMyUnpinnedChatConversation());
    }

    // ========== 对话管理 ==========

    /**
     * 获取对话分页列表
     */
    @SaCheckPermission("ai:chat-conversation:list")
    @GetMapping("/list")
    public TableDataInfo<AiChatConversationVO> list(PageQuery pageQuery, AiChatConversationPageQuery query) {
        TableDataInfo<AiChatConversationVO> page = chatConversationService.selectChatConversationList(pageQuery, query);
        transService.transBatch(page.getRows());
        return page;
    }

    /**
     * 管理员删除对话
     */
    @Log(title = "管理员删除对话", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @SaCheckPermission("ai:chat-conversation:remove")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(chatConversationService.deleteChatConversationByIds(ids));
    }

}
