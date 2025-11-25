package com.lucky.ai.controller.image;

import com.lucky.ai.domain.AiImage;
import com.lucky.ai.service.IAiImageService;
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
 * AI 绘画Controller
 * 
 * @author lucky
 */
@RestController
@RequestMapping("/ai/image")
public class AiImageController extends BaseController {

    @Autowired
    private IAiImageService aiImageService;

    /**
     * 查询AI 绘画列表
     */
    @PreAuthorize("@ss.hasPermi('ai:image:list')")
    @GetMapping("/list")
    public TableDataInfo list(AiImage aiImage) {
        startPage();
        List<AiImage> list = aiImageService.selectAiImageList(aiImage);
        return getDataTable(list);
    }

    /**
     * 导出AI 绘画列表
     */
    @PreAuthorize("@ss.hasPermi('ai:image:export')")
    @Log(title = "AI 绘画", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AiImage aiImage) {
        List<AiImage> list = aiImageService.selectAiImageList(aiImage);
        ExcelUtil<AiImage> util = new ExcelUtil<AiImage>(AiImage.class);
        util.exportExcel(response, list, "AI 绘画数据");
    }

    /**
     * 获取AI 绘画详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:image:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(aiImageService.selectAiImageById(id));
    }

    /**
     * 新增AI 绘画
     */
    @PreAuthorize("@ss.hasPermi('ai:image:add')")
    @Log(title = "AI 绘画", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AiImage aiImage) {
        return toAjax(aiImageService.insertAiImage(aiImage));
    }

    /**
     * 修改AI 绘画
     */
    @PreAuthorize("@ss.hasPermi('ai:image:edit')")
    @Log(title = "AI 绘画", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AiImage aiImage) {
        return toAjax(aiImageService.updateAiImage(aiImage));
    }

    /**
     * 删除AI 绘画
     */
    @PreAuthorize("@ss.hasPermi('ai:image:remove')")
    @Log(title = "AI 绘画", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(aiImageService.deleteAiImageByIds(ids));
    }

}
