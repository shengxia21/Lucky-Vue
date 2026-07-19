package com.lucky.ai.controller.chat;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lucky.ai.domain.query.message.AiChatMessageQuery;
import com.lucky.ai.domain.vo.message.AiChatMessageVO;
import com.lucky.ai.service.IAiChatMessageService;
import com.lucky.common.core.domain.R;
import com.lucky.common.log.annotation.Log;
import com.lucky.common.log.enums.BusinessType;
import com.lucky.common.mybatis.core.controller.BaseController;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 获得【我的】指定对话的消息列表
     */
    @GetMapping("/my/list/{conversationId}")
    public R<List<AiChatMessageVO>> myListByConversationId(@PathVariable Long conversationId) {
        return R.ok(chatMessageService.selectMyChatMessageListByConversationId(conversationId));
    }

    /**
     * 删除【我的】消息
     */
    @Log(title = "删除【我的】消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/my/{id}")
    public R<Void> removeMy(@PathVariable Long id) {
        return toAjax(chatMessageService.deleteMyChatMessageById(id));
    }

    /**
     * 删除【我的】指定对话的消息
     */
    @Log(title = "删除【我的】指定对话的消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/my/conversation/{conversationId}")
    public R<Void> removeMyByConversationId(@PathVariable Long conversationId) {
        return toAjax(chatMessageService.deleteMyChatMessageByConversationId(conversationId));
    }

    // ========== 消息管理 ==========

    /**
     * 获得消息分页(需要对话列表的权限)
     */
    @SaCheckPermission("ai:chat-conversation:list")
    @GetMapping("/list")
    public TableDataInfo<AiChatMessageVO> list(PageQuery pageQuery, @Validated AiChatMessageQuery query) {
        return chatMessageService.selectChatMessageList(pageQuery, query);
    }

}