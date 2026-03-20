package com.lucky.ai.controller.chat;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.lucky.ai.controller.chat.vo.message.AiChatMessagePageReqVO;
import com.lucky.ai.controller.chat.vo.message.AiChatMessageRespVO;
import com.lucky.ai.controller.chat.vo.message.AiChatMessageSendReqVO;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.domain.AiChatMessage;
import com.lucky.ai.service.IAiChatConversationService;
import com.lucky.ai.service.IAiChatMessageService;
import com.lucky.common.annotation.Log;
import com.lucky.common.core.controller.BaseController;
import com.lucky.common.core.domain.AjaxResult;
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
    private IAiChatMessageService aiChatMessageService;

    @Resource
    private IAiChatConversationService aiChatConversationService;

    /**
     * 发送消息（流式）
     */
    @PostMapping(value = "/send-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AjaxResult> sendChatMessageStream(@Validated @RequestBody AiChatMessageSendReqVO sendReqVO) {
        return aiChatMessageService.sendChatMessageStream(sendReqVO, getUserId());
    }

    /**
     * 获得指定对话的消息列表
     */
    @GetMapping("/list-by-conversation-id")
    public AjaxResult getChatMessageListByConversationId(@RequestParam("conversationId") Long conversationId) {
        AiChatConversation conversation = aiChatConversationService.getChatConversation(conversationId);
        if (conversation == null || ObjUtil.notEqual(conversation.getUserId(), getUserId())) {
            return success(Collections.emptyList());
        }
        // 1. 获取消息列表
        List<AiChatMessage> messageList = aiChatMessageService.getChatMessageListByConversationId(conversationId);
        if (CollUtil.isEmpty(messageList)) {
            return success(Collections.emptyList());
        }

        // 2. 拼接数据，主要是知识库段落信息

        List<AiChatMessageRespVO> messageVOList = BeanUtil.copyToList(messageList, AiChatMessageRespVO.class);
        return success(messageVOList);
    }

    /**
     * 删除消息
     */
    @Log(title = "删除消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete")
    public AjaxResult deleteChatMessage(@RequestParam("id") Long id) {
        return toAjax(aiChatMessageService.deleteChatMessage(id, getUserId()));
    }

    /**
     * 删除指定对话的消息
     */
    @Log(title = "删除指定对话的消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete-by-conversation-id")
    public AjaxResult deleteChatMessageByConversationId(@RequestParam("conversationId") Long conversationId) {
        return toAjax(aiChatMessageService.deleteChatMessageByConversationId(conversationId, getUserId()));
    }

    // ========== 对话管理 ==========

    /**
     * 获得消息分页
     */
    @PreAuthorize("@ss.hasPermi('ai:chat-conversation:list')")
    @GetMapping("/page")
    public TableDataInfo getChatMessagePage(AiChatMessagePageReqVO pageReqVO) {
        startPage();
        List<AiChatMessageRespVO> list = aiChatMessageService.getChatMessagePage(pageReqVO);
        return getDataTable(list);
    }

    /**
     * 删除消息（管理员）
     */
    @Log(title = "删除消息（管理员）", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('ai:chat-message:delete')")
    @DeleteMapping("/delete-by-admin")
    public AjaxResult deleteChatMessageByAdmin(@RequestParam("id") Long id) {
        return toAjax(aiChatMessageService.deleteChatMessageByAdmin(id));
    }

}
