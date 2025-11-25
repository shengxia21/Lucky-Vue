package com.lucky.ai.controller.model;

import com.lucky.ai.domain.AiModel;
import com.lucky.ai.service.IAiModelService;
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
 * AI 模型Controller
 * 
 * @author lucky
 */
@RestController
@RequestMapping("/ai/model")
public class AiModelController extends BaseController {

    @Autowired
    private IAiModelService aiModelService;

    /**
     * 查询AI 模型列表
     */
    @PreAuthorize("@ss.hasPermi('ai:model:list')")
    @GetMapping("/list")
    public TableDataInfo list(AiModel aiModel) {
        startPage();
        List<AiModel> list = aiModelService.selectAiModelList(aiModel);
        return getDataTable(list);
    }

    /**
     * 导出AI 模型列表
     */
    @PreAuthorize("@ss.hasPermi('ai:model:export')")
    @Log(title = "AI 模型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AiModel aiModel) {
        List<AiModel> list = aiModelService.selectAiModelList(aiModel);
        ExcelUtil<AiModel> util = new ExcelUtil<AiModel>(AiModel.class);
        util.exportExcel(response, list, "AI 模型数据");
    }

    /**
     * 获取AI 模型详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:model:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(aiModelService.selectAiModelById(id));
    }

    /**
     * 新增AI 模型
     */
    @PreAuthorize("@ss.hasPermi('ai:model:add')")
    @Log(title = "AI 模型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AiModel aiModel) {
        return toAjax(aiModelService.insertAiModel(aiModel));
    }

    /**
     * 修改AI 模型
     */
    @PreAuthorize("@ss.hasPermi('ai:model:edit')")
    @Log(title = "AI 模型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AiModel aiModel) {
        return toAjax(aiModelService.updateAiModel(aiModel));
    }

    /**
     * 删除AI 模型
     */
    @PreAuthorize("@ss.hasPermi('ai:model:remove')")
    @Log(title = "AI 模型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(aiModelService.deleteAiModelByIds(ids));
    }

}
