package com.lucky.ai.controller.chat;

import com.lucky.ai.domain.AiChatMessage;
import com.lucky.ai.service.IAiChatMessageService;
import com.lucky.common.annotation.Log;
import com.lucky.common.core.controller.BaseController;
import com.lucky.common.core.domain.AjaxResult;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.common.enums.BusinessType;
import com.lucky.common.utils.poi.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AI 聊天消息Controller
 * 
 * @author lucky
 */
@RestController
@RequestMapping("/ai/message")
public class AiChatMessageController extends BaseController {

    @Autowired
    private IAiChatMessageService aiChatMessageService;

    /**
     * 查询AI 聊天消息列表
     */
    @PreAuthorize("@ss.hasPermi('ai:message:list')")
    @GetMapping("/list")
    public TableDataInfo list(AiChatMessage aiChatMessage) {
        startPage();
        List<AiChatMessage> list = aiChatMessageService.selectAiChatMessageList(aiChatMessage);
        return getDataTable(list);
    }

    /**
     * 导出AI 聊天消息列表
     */
    @PreAuthorize("@ss.hasPermi('ai:message:export')")
    @Log(title = "AI 聊天消息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AiChatMessage aiChatMessage) {
        List<AiChatMessage> list = aiChatMessageService.selectAiChatMessageList(aiChatMessage);
        ExcelUtil<AiChatMessage> util = new ExcelUtil<AiChatMessage>(AiChatMessage.class);
        util.exportExcel(response, list, "AI 聊天消息数据");
    }

    /**
     * 获取AI 聊天消息详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:message:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(aiChatMessageService.selectAiChatMessageById(id));
    }

    /**
     * 新增AI 聊天消息
     */
    @PreAuthorize("@ss.hasPermi('ai:message:add')")
    @Log(title = "AI 聊天消息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AiChatMessage aiChatMessage) {
        return toAjax(aiChatMessageService.insertAiChatMessage(aiChatMessage));
    }

    /**
     * 修改AI 聊天消息
     */
    @PreAuthorize("@ss.hasPermi('ai:message:edit')")
    @Log(title = "AI 聊天消息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AiChatMessage aiChatMessage) {
        return toAjax(aiChatMessageService.updateAiChatMessage(aiChatMessage));
    }

    /**
     * 删除AI 聊天消息
     */
    @PreAuthorize("@ss.hasPermi('ai:message:remove')")
    @Log(title = "AI 聊天消息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(aiChatMessageService.deleteAiChatMessageByIds(ids));
    }

}
