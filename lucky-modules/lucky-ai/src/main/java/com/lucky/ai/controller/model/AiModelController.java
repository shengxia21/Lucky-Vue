package com.lucky.ai.controller.model;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lucky.ai.domain.query.model.AiModelPageQuery;
import com.lucky.ai.domain.query.model.AiModelSaveQuery;
import com.lucky.ai.domain.vo.model.AiModelVO;
import com.lucky.ai.service.IAiModelService;
import com.lucky.common.ai.enums.CommonStatusEnum;
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
     * 创建模型
     */
    @Log(title = "创建模型", businessType = BusinessType.INSERT)
    @SaCheckPermission("ai:model:create")
    @PostMapping("/create")
    public R<Long> createModel(@Validated @RequestBody AiModelSaveQuery query) {
        return R.ok(modelService.createModel(query));
    }

    /**
     * 更新模型
     */
    @Log(title = "更新模型", businessType = BusinessType.UPDATE)
    @SaCheckPermission("ai:model:update")
    @PutMapping("/update")
    public R<Void> updateModel(@Validated @RequestBody AiModelSaveQuery query) {
        return toAjax(modelService.updateModel(query));
    }

    /**
     * 删除模型
     */
    @Log(title = "删除模型", businessType = BusinessType.DELETE)
    @SaCheckPermission("ai:model:delete")
    @DeleteMapping("/delete")
    public R<Void> deleteModel(@RequestParam("id") Long id) {
        return toAjax(modelService.deleteModelById(id));
    }

    /**
     * 获得模型
     */
    @SaCheckPermission("ai:model:query")
    @GetMapping("/get")
    public R<AiModelVO> getModel(@RequestParam("id") Long id) {
        return R.ok(modelService.getModelById(id));
    }

    /**
     * 获得模型分页
     */
    @SaCheckPermission("ai:model:list")
    @GetMapping("/page")
    public TableDataInfo<AiModelVO> getModelPage(PageQuery pageQuery, AiModelPageQuery query) {
        return modelService.getModelPage(pageQuery, query);
    }

    /**
     * 获得模型列表
     */
    @GetMapping("/simple-list")
    public R<List<AiModelVO>> getModelSimpleList(
            @RequestParam("type") Integer type,
            @RequestParam(value = "platform", required = false) String platform) {
        List<AiModelVO> list = modelService.getModelList(CommonStatusEnum.ENABLE.getStatus(), type, platform);
        return R.ok(list);
    }

}
