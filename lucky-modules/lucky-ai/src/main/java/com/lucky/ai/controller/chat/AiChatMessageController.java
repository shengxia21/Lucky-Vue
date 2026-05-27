package com.lucky.ai.controller.chat;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.lucky.ai.domain.query.message.AiChatMessagePageQuery;
import com.lucky.ai.domain.vo.conversation.AiChatConversationVO;
import com.lucky.ai.domain.vo.message.AiChatMessageVO;
import com.lucky.ai.service.AiChatConversationService;
import com.lucky.ai.service.AiChatMessageService;
import com.lucky.common.core.domain.R;
import com.lucky.common.log.annotation.Log;
import com.lucky.common.log.enums.BusinessType;
import com.lucky.common.mybatis.core.controller.BaseController;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * AI 聊天消息Controller
 *
 * @author lucky
 */
@RestController
@RequestMapping("/ai/chat/message")
public class AiChatMessageController extends BaseController {

    @Resource
    private AiChatMessageService chatMessageService;
    @Resource
    private AiChatConversationService chatConversationService;

    /**
     * 获得指定对话的消息列表
     */
    @GetMapping("/list-by-conversation-id")
    public R<List<AiChatMessageVO>> getChatMessageListByConversationId(@RequestParam("conversationId") Long conversationId) {
        AiChatConversationVO conversation = chatConversationService.getChatConversationById(conversationId);
        if (conversation == null || ObjUtil.notEqual(conversation.getUserId(), getUserId())) {
            return R.fail("对话不存在或不属于当前用户");
        }
        // 1. 获取消息列表
        List<AiChatMessageVO> messageList = chatMessageService.getChatMessageListByConversationId(conversationId);
        if (CollUtil.isEmpty(messageList)) {
            return R.ok(Collections.emptyList());
        }
        // 2. 拼接数据，主要是知识库段落信息

        return R.ok(messageList);
    }

    /**
     * 删除消息
     */
    @Log(title = "删除消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete")
    public R<Void> deleteChatMessage(@RequestParam("id") Long id) {
        return toAjax(chatMessageService.deleteChatMessageByIdAndUserId(id, getUserId()));
    }

    /**
     * 删除指定对话的消息
     */
    @Log(title = "删除指定对话的消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete-by-conversation-id")
    public R<Void> deleteChatMessageByConversationId(@RequestParam("conversationId") Long conversationId) {
        return toAjax(chatMessageService.deleteChatMessageByConversationIdAndUserId(conversationId, getUserId()));
    }

    // ========== 对话管理 ==========

    /**
     * 获得消息分页
     */
    @SaCheckPermission("ai:chat-conversation:list")
    @GetMapping("/page")
    public TableDataInfo<AiChatMessageVO> getChatMessagePage(PageQuery pageQuery, AiChatMessagePageQuery query) {
        return chatMessageService.getChatMessagePage(pageQuery, query);
    }

    /**
     * 删除消息（管理员）
     */
    @Log(title = "删除消息（管理员）", businessType = BusinessType.DELETE)
    @SaCheckPermission("ai:chat-message:delete")
    @DeleteMapping("/delete-by-admin")
    public R<Void> deleteChatMessageByAdmin(@RequestParam("id") Long id) {
        return toAjax(chatMessageService.deleteChatMessageById(id));
    }

}