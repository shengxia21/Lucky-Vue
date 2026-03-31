package com.lucky.ai.controller.chat;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import com.lucky.ai.controller.chat.vo.conversation.AiChatConversationCreateMyReqVO;
import com.lucky.ai.controller.chat.vo.conversation.AiChatConversationPageReqVO;
import com.lucky.ai.controller.chat.vo.conversation.AiChatConversationRespVO;
import com.lucky.ai.controller.chat.vo.conversation.AiChatConversationUpdateMyReqVO;
import com.lucky.ai.domain.AiChatConversation;
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
    public R<Long> createChatConversationMy(@RequestBody AiChatConversationCreateMyReqVO createReqVO) {
        return R.ok(chatConversationService.createChatConversationMy(createReqVO, getUserId()));
    }

    /**
     * 更新我的聊天对话
     */
    @Log(title = "更新【我的】聊天对话", businessType = BusinessType.UPDATE)
    @PutMapping("/update-my")
    public R<Integer> updateChatConversationMy(@Validated @RequestBody AiChatConversationUpdateMyReqVO updateReqVO) {
        return R.ok(chatConversationService.updateChatConversationMy(updateReqVO, getUserId()));
    }

    /**
     * 获得我的聊天对话列表
     */
    @GetMapping("/my-list")
    public R<List<AiChatConversationRespVO>> getChatConversationMyList() {
        return R.ok(chatConversationService.getChatConversationListByUserId(getUserId()));
    }

    /**
     * 获得我的聊天对话
     */
    @GetMapping("/get-my")
    public R<AiChatConversationRespVO> getChatConversationMy(@RequestParam("id") Long id) {
        AiChatConversation conversation = chatConversationService.getChatConversationById(id);
        if (conversation != null && ObjUtil.notEqual(conversation.getUserId(), getUserId())) {
            return R.fail("对话不存在或不属于当前用户");
        }
        return R.ok(BeanUtil.toBean(conversation, AiChatConversationRespVO.class));
    }

    /**
     * 删除我的聊天对话
     */
    @Log(title = "删除【我的】聊天对话", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete-my")
    public R<Integer> deleteChatConversationMy(@RequestParam("id") Long id) {
        return R.ok(chatConversationService.deleteChatConversationMy(id, getUserId()));
    }

    /**
     * 删除未置顶的聊天对话
     */
    @Log(title = "删除未置顶的聊天对话", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete-by-unpinned")
    public R<Integer> deleteChatConversationMyByUnpinned() {
        return R.ok(chatConversationService.deleteChatConversationMyByUserId(getUserId()));
    }

    // ========== 对话管理 ==========

    /**
     * 获取对话分页列表
     */
    @PreAuthorize("@ss.hasPermi('ai:chat-conversation:list')")
    @GetMapping("/page")
    public TableDataInfo<AiChatConversationRespVO> getChatConversationPage(PageQuery pageQuery, AiChatConversationPageReqVO pageReqVO) {
        return chatConversationService.getChatConversationPage(pageQuery, pageReqVO);
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
