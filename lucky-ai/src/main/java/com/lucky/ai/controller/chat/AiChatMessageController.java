package com.lucky.ai.controller.chat;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.lucky.ai.core.vo.chat.ChatMessageRequest;
import com.lucky.ai.core.vo.chat.ChatMessageResponse;
import com.lucky.ai.domain.query.message.ChatMessagePageQuery;
import com.lucky.ai.domain.vo.conversation.ChatConversationVO;
import com.lucky.ai.domain.vo.message.ChatMessageVO;
import com.lucky.ai.service.AiChatConversationService;
import com.lucky.ai.service.AiChatMessageService;
import com.lucky.common.annotation.Log;
import com.lucky.common.core.controller.BaseController;
import com.lucky.common.core.domain.R;
import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.common.enums.BusinessType;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

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
     * 发送消息（流式）
     */
    @PostMapping(value = "/send-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatMessageResponse> sendChatMessageStream(@Validated @RequestBody ChatMessageRequest query) {
        return chatMessageService.sendChatMessageStream(query, getUserId());
    }

    /**
     * 获得指定对话的消息列表
     */
    @GetMapping("/list-by-conversation-id")
    public R<List<ChatMessageVO>> getChatMessageListByConversationId(@RequestParam("conversationId") Long conversationId) {
        ChatConversationVO conversation = chatConversationService.getChatConversationById(conversationId);
        if (conversation == null || ObjUtil.notEqual(conversation.getUserId(), getUserId())) {
            return R.fail("对话不存在或不属于当前用户");
        }
        // 1. 获取消息列表
        List<ChatMessageVO> messageList = chatMessageService.getChatMessageListByConversationId(conversationId);
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
    public R<Integer> deleteChatMessage(@RequestParam("id") Long id) {
        return R.ok(chatMessageService.deleteChatMessageByIdAndUserId(id, getUserId()));
    }

    /**
     * 删除指定对话的消息
     */
    @Log(title = "删除指定对话的消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete-by-conversation-id")
    public R<Integer> deleteChatMessageByConversationId(@RequestParam("conversationId") Long conversationId) {
        return R.ok(chatMessageService.deleteChatMessageByConversationIdAndUserId(conversationId, getUserId()));
    }

    // ========== 对话管理 ==========

    /**
     * 获得消息分页
     */
    @PreAuthorize("@ss.hasPermi('ai:chat-conversation:list')")
    @GetMapping("/page")
    public TableDataInfo<ChatMessageVO> getChatMessagePage(PageQuery pageQuery, ChatMessagePageQuery query) {
        return chatMessageService.getChatMessagePage(pageQuery, query);
    }

    /**
     * 删除消息（管理员）
     */
    @Log(title = "删除消息（管理员）", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('ai:chat-message:delete')")
    @DeleteMapping("/delete-by-admin")
    public R<Integer> deleteChatMessageByAdmin(@RequestParam("id") Long id) {
        return R.ok(chatMessageService.deleteChatMessageById(id));
    }

}