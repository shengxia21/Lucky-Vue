package com.lucky.ai.controller.chat;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.ai.controller.chat.vo.conversation.AiChatConversationRespVO;
import com.lucky.ai.controller.chat.vo.conversation.AiChatConversationUpdateMyReqVO;
import com.lucky.ai.domain.AiChatConversation;
import com.lucky.ai.service.IAiChatConversationService;
import com.lucky.common.annotation.Log;
import com.lucky.common.core.controller.BaseController;
import com.lucky.common.core.domain.AjaxResult;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.common.enums.BusinessType;
import com.lucky.common.utils.poi.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/ai/conversation")
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
     * 查询AI 聊天对话列表
     */
    @PreAuthorize("@ss.hasPermi('ai:conversation:list')")
    @GetMapping("/list")
    public TableDataInfo list(AiChatConversation aiChatConversation) {
        startPage();
        List<AiChatConversation> list = aiChatConversationService.selectAiChatConversationList(aiChatConversation);
        return getDataTable(list);
    }

    /**
     * 导出AI 聊天对话列表
     */
    @PreAuthorize("@ss.hasPermi('ai:conversation:export')")
    @Log(title = "AI 聊天对话", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AiChatConversation aiChatConversation) {
        List<AiChatConversation> list = aiChatConversationService.selectAiChatConversationList(aiChatConversation);
        ExcelUtil<AiChatConversation> util = new ExcelUtil<AiChatConversation>(AiChatConversation.class);
        util.exportExcel(response, list, "AI 聊天对话数据");
    }

    /**
     * 获取AI 聊天对话详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:conversation:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(aiChatConversationService.selectAiChatConversationById(id));
    }

    /**
     * 新增AI 聊天对话
     */
    @PreAuthorize("@ss.hasPermi('ai:conversation:add')")
    @Log(title = "AI 聊天对话", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AiChatConversation aiChatConversation) {
        return toAjax(aiChatConversationService.insertAiChatConversation(aiChatConversation));
    }

    /**
     * 修改AI 聊天对话
     */
    @PreAuthorize("@ss.hasPermi('ai:conversation:edit')")
    @Log(title = "AI 聊天对话", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AiChatConversation aiChatConversation) {
        return toAjax(aiChatConversationService.updateAiChatConversation(aiChatConversation));
    }

    /**
     * 删除AI 聊天对话
     */
    @PreAuthorize("@ss.hasPermi('ai:conversation:remove')")
    @Log(title = "AI 聊天对话", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(aiChatConversationService.deleteAiChatConversationByIds(ids));
    }

}
