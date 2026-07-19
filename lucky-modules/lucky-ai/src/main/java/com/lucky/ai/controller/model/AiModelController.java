package com.lucky.ai.controller.model;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lucky.ai.domain.query.model.AiModelQuery;
import com.lucky.ai.domain.query.model.AiModelSaveQuery;
import com.lucky.ai.domain.vo.model.AiModelVO;
import com.lucky.ai.service.IAiModelService;
import com.lucky.common.core.domain.R;
import com.lucky.common.core.validate.Update;
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
 * AI 模型Controller
 *
 * @author lucky
 */
@RestController
@RequestMapping("/ai/model")
public class AiModelController extends BaseController {

    @Resource
    private IAiModelService modelService;

    /**
     * 获得模型分页
     */
    @SaCheckPermission("ai:model:list")
    @GetMapping("/list")
    public TableDataInfo<AiModelVO> list(PageQuery pageQuery, AiModelQuery query) {
        return modelService.selectModelList(pageQuery, query);
    }

    /**
     * 获得模型
     */
    @SaCheckPermission("ai:model:query")
    @GetMapping("/{id}")
    public R<AiModelVO> getInfo(@PathVariable Long id) {
        return R.ok(modelService.selectModelById(id));
    }

    /**
     * 创建模型
     */
    @Log(title = "创建模型", businessType = BusinessType.INSERT)
    @SaCheckPermission("ai:model:add")
    @PostMapping
    public R<Void> add(@Validated @RequestBody AiModelSaveQuery query) {
        return toAjax(modelService.insertModel(query));
    }

    /**
     * 更新模型
     */
    @Log(title = "更新模型", businessType = BusinessType.UPDATE)
    @SaCheckPermission("ai:model:edit")
    @PutMapping
    public R<Void> edit(@Validated({Update.class}) @RequestBody AiModelSaveQuery query) {
        return toAjax(modelService.updateModel(query));
    }

    /**
     * 删除模型
     */
    @Log(title = "删除模型", businessType = BusinessType.DELETE)
    @SaCheckPermission("ai:model:remove")
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(modelService.deleteModelByIds(ids));
    }

    /**
     * 获得模型选择框列表
     */
    @GetMapping("/optionSelect")
    public R<List<AiModelVO>> optionSelect(@RequestParam Integer type) {
        return R.ok(modelService.selectModelAll(type));
    }

}
