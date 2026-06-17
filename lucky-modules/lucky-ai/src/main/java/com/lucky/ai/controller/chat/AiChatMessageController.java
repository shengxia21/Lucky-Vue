package com.lucky.ai.controller.chat;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.lucky.ai.domain.query.message.AiChatMessagePageQuery;
import com.lucky.ai.domain.vo.conversation.AiChatConversationVO;
import com.lucky.ai.domain.vo.message.AiChatMessageVO;
import com.lucky.ai.service.IAiChatConversationService;
import com.lucky.ai.service.IAiChatMessageService;
import com.lucky.common.core.domain.R;
import com.lucky.common.log.annotation.Log;
import com.lucky.common.log.enums.BusinessType;
import com.lucky.common.mybatis.core.controller.BaseController;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import com.lucky.common.security.utils.SecurityUtils;
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
    private IAiChatMessageService chatMessageService;
    @Resource
    private IAiChatConversationService chatConversationService;

    /**
     * 获得我的指定对话的消息列表
     */
    @GetMapping("/my/list/{conversationId}")
    public R<List<AiChatMessageVO>> listByConversationId(@PathVariable Long conversationId) {
        AiChatConversationVO conversation = chatConversationService.selectChatConversationById(conversationId);
        if (conversation == null || ObjUtil.notEqual(conversation.getUserId(), SecurityUtils.getUserId())) {
            return R.fail("对话不存在或不属于当前用户");
        }
        // 1. 获取消息列表
        List<AiChatMessageVO> messageList = chatMessageService.selectChatMessageListByConversationId(conversationId);
        if (CollUtil.isEmpty(messageList)) {
            return R.ok(Collections.emptyList());
        }
        transService.transBatch(messageList);
        return R.ok(messageList);
    }

    /**
     * 删除我的消息
     */
    @Log(title = "删除我的消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/my/{id}")
    public R<Void> removeMy(@PathVariable Long id) {
        return toAjax(chatMessageService.deleteMyChatMessageById(id));
    }

    /**
     * 删除我的指定对话的消息
     */
    @Log(title = "删除我的指定对话的消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/my/conversation/{conversationId}")
    public R<Void> removeByConversationId(@PathVariable Long conversationId) {
        return toAjax(chatMessageService.deleteMyChatMessageByConversationId(conversationId));
    }

    // ========== 消息管理 ==========

    /**
     * 获得消息分页
     */
    @SaCheckPermission("ai:chat-conversation:list")
    @GetMapping("/list")
    public TableDataInfo<AiChatMessageVO> list(PageQuery pageQuery, AiChatMessagePageQuery query) {
        TableDataInfo<AiChatMessageVO> page = chatMessageService.selectChatMessageList(pageQuery, query);
        transService.transBatch(page.getRows());
        return page;
    }

    /**
     * 管理员删除消息
     */
    @Log(title = "管理员删除消息", businessType = BusinessType.DELETE)
    @SaCheckPermission("ai:chat-message:remove")
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(chatMessageService.deleteChatMessageByIds(ids));
    }

}