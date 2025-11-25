package com.lucky.ai.controller.model;

import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.service.IAiApiKeyService;
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
 * AI API 秘钥Controller
 * 
 * @author lucky
 */
@RestController
@RequestMapping("/ai/key")
public class AiApiKeyController extends BaseController {

    @Autowired
    private IAiApiKeyService aiApiKeyService;

    /**
     * 查询AI API 秘钥列表
     */
    @PreAuthorize("@ss.hasPermi('ai:key:list')")
    @GetMapping("/list")
    public TableDataInfo list(AiApiKey aiApiKey) {
        startPage();
        List<AiApiKey> list = aiApiKeyService.selectAiApiKeyList(aiApiKey);
        return getDataTable(list);
    }

    /**
     * 导出AI API 秘钥列表
     */
    @PreAuthorize("@ss.hasPermi('ai:key:export')")
    @Log(title = "AI API 秘钥", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AiApiKey aiApiKey) {
        List<AiApiKey> list = aiApiKeyService.selectAiApiKeyList(aiApiKey);
        ExcelUtil<AiApiKey> util = new ExcelUtil<AiApiKey>(AiApiKey.class);
        util.exportExcel(response, list, "AI API 秘钥数据");
    }

    /**
     * 获取AI API 秘钥详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:key:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(aiApiKeyService.selectAiApiKeyById(id));
    }

    /**
     * 新增AI API 秘钥
     */
    @PreAuthorize("@ss.hasPermi('ai:key:add')")
    @Log(title = "AI API 秘钥", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AiApiKey aiApiKey) {
        return toAjax(aiApiKeyService.insertAiApiKey(aiApiKey));
    }

    /**
     * 修改AI API 秘钥
     */
    @PreAuthorize("@ss.hasPermi('ai:key:edit')")
    @Log(title = "AI API 秘钥", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AiApiKey aiApiKey) {
        return toAjax(aiApiKeyService.updateAiApiKey(aiApiKey));
    }

    /**
     * 删除AI API 秘钥
     */
    @PreAuthorize("@ss.hasPermi('ai:key:remove')")
    @Log(title = "AI API 秘钥", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(aiApiKeyService.deleteAiApiKeyByIds(ids));
    }

}
