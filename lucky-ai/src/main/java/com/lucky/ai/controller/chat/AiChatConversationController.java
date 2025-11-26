package com.lucky.ai.controller.chat;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import com.lucky.ai.controller.chat.vo.conversation.AiChatConversationRespVO;
import com.lucky.ai.controller.chat.vo.conversation.AiChatConversationUpdateMyReqVO;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.service.IAiChatConversationService;
import com.lucky.common.annotation.Log;
import com.lucky.common.core.controller.BaseController;
import com.lucky.common.core.domain.AjaxResult;
import com.lucky.common.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private IAiChatConversationService aiChatConversationService;

    /**
     * 创建我的聊天对话
     */
    @Log(title = "创建【我的】聊天对话", businessType = BusinessType.INSERT)
    @PostMapping("/create-my")
    public AjaxResult createChatConversationMy() {
        return success(aiChatConversationService.createChatConversationMy(getUserId()));
    }

    /**
     * 更新我的聊天对话
     */
    @Log(title = "更新【我的】聊天对话", businessType = BusinessType.UPDATE)
    @PostMapping("/update-my")
    public AjaxResult updateChatConversationMy(@Validated @RequestBody AiChatConversationUpdateMyReqVO updateReqVO) {
        return toAjax(aiChatConversationService.updateChatConversationMy(updateReqVO, getUserId()));
    }

    /**
     * 获得我的聊天对话列表
     */
    @GetMapping("/my-list")
    public AjaxResult getChatConversationMyList() {
        List<AiChatConversation> list = aiChatConversationService.getChatConversationListByUserId(getUserId());
        return success(BeanUtil.copyToList(list, AiChatConversationRespVO.class));
    }

    /**
     * 获得我的聊天对话
     */
    @GetMapping("/get-my")
    public AjaxResult getChatConversationMy(@RequestParam("id") Long id) {
        AiChatConversation conversation = aiChatConversationService.getChatConversation(id);
        if (conversation != null && ObjUtil.notEqual(conversation.getUserId(), getUserId())) {
            conversation = null;
        }
        return success(BeanUtil.toBean(conversation, AiChatConversationRespVO.class));
    }

    /**
     * 删除我的聊天对话
     */
    @Log(title = "删除【我的】聊天对话", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete-my")
    public AjaxResult deleteChatConversationMy(@RequestParam("id") Long id) {
        return toAjax(aiChatConversationService.deleteChatConversationMy(id, getUserId()));
    }

    /**
     * 删除未置顶的聊天对话
     */
    @Log(title = "删除未置顶的聊天对话", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete-by-unpinned")
    public AjaxResult deleteChatConversationMyByUnpinned() {
        return toAjax(aiChatConversationService.deleteChatConversationMyByUnpinned(getUserId()));
    }

}
