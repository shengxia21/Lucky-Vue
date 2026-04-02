package com.lucky.ai.controller.model;

import com.lucky.ai.domain.query.model.ModelPageQuery;
import com.lucky.ai.domain.query.model.ModelSaveQuery;
import com.lucky.ai.domain.vo.model.ModelVO;
import com.lucky.ai.enums.CommonStatusEnum;
import com.lucky.ai.service.AiModelService;
import com.lucky.common.annotation.Log;
import com.lucky.common.core.controller.BaseController;
import com.lucky.common.core.domain.R;
import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.common.enums.BusinessType;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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

    @Resource
    private AiModelService modelService;

    /**
     * 创建模型
     */
    @Log(title = "创建模型", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('ai:model:create')")
    @PostMapping("/create")
    public R<Long> createModel(@Validated @RequestBody ModelSaveQuery query) {
        return R.ok(modelService.createModel(query));
    }

    /**
     * 更新模型
     */
    @Log(title = "更新模型", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('ai:model:update')")
    @PutMapping("/update")
    public R<Integer> updateModel(@Validated @RequestBody ModelSaveQuery query) {
        return R.ok(modelService.updateModel(query));
    }

    /**
     * 删除模型
     */
    @Log(title = "删除模型", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('ai:model:delete')")
    @DeleteMapping("/delete")
    public R<Integer> deleteModel(@RequestParam("id") Long id) {
        return R.ok(modelService.deleteModelById(id));
    }

    /**
     * 获得模型
     */
    @PreAuthorize("@ss.hasPermi('ai:model:query')")
    @GetMapping("/get")
    public R<ModelVO> getModel(@RequestParam("id") Long id) {
        return R.ok(modelService.getModelById(id));
    }

    /**
     * 获得模型分页
     */
    @PreAuthorize("@ss.hasPermi('ai:model:list')")
    @GetMapping("/page")
    public TableDataInfo<ModelVO> getModelPage(PageQuery pageQuery, ModelPageQuery query) {
        return modelService.getModelPage(pageQuery, query);
    }

    /**
     * 获得模型列表
     */
    @GetMapping("/simple-list")
    public R<List<ModelVO>> getModelSimpleList(
            @RequestParam("type") Integer type,
            @RequestParam(value = "platform", required = false) String platform) {
        List<ModelVO> list = modelService.getModelList(CommonStatusEnum.ENABLE.getStatus(), type, platform);
        return R.ok(list);
    }

}
