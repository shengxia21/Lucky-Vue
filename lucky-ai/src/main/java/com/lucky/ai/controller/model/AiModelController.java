package com.lucky.ai.controller.model;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.ai.controller.model.vo.model.AiModelPageReqVO;
import com.lucky.ai.controller.model.vo.model.AiModelRespVO;
import com.lucky.ai.controller.model.vo.model.AiModelSaveReqVO;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.enums.CommonStatusEnum;
import com.lucky.ai.service.IAiModelService;
import com.lucky.common.annotation.Log;
import com.lucky.common.core.controller.BaseController;
import com.lucky.common.core.domain.AjaxResult;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.common.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.lucky.ai.util.CollectionUtils.convertList;

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
     * 创建模型
     */
    @Log(title = "创建模型", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('ai:model:create')")
    @PostMapping("/create")
    public AjaxResult createModel(@Validated @RequestBody AiModelSaveReqVO createReqVO) {
        return success(aiModelService.createModel(createReqVO));
    }

    /**
     * 更新模型
     */
    @Log(title = "更新模型", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('ai:model:update')")
    @PutMapping("/update")
    public AjaxResult updateModel(@Validated @RequestBody AiModelSaveReqVO updateReqVO) {
        return toAjax(aiModelService.updateModel(updateReqVO));
    }

    /**
     * 删除模型
     */
    @Log(title = "删除模型", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('ai:model:delete')")
    @DeleteMapping("/delete")
    public AjaxResult deleteModel(@RequestParam("id") Long id) {
        return toAjax(aiModelService.deleteModel(id));
    }

    /**
     * 获得模型
     */
    @PreAuthorize("@ss.hasPermi('ai:model:query')")
    @GetMapping("/get")
    public AjaxResult getModel(@RequestParam("id") Long id) {
        AiModel model = aiModelService.getModel(id);
        return success(BeanUtil.toBean(model, AiModelRespVO.class));
    }

    /**
     * 获得模型分页
     */
    @PreAuthorize("@ss.hasPermi('ai:model:query')")
    @GetMapping("/page")
    public TableDataInfo getModelPage(AiModelPageReqVO pageReqVO) {
        startPage();
        List<AiModel> pageResult = aiModelService.getModelPage(pageReqVO);
        return getDataTable(BeanUtil.copyToList(pageResult, AiModelRespVO.class));
    }

    /**
     * 获得模型列表
     */
    @GetMapping("/simple-list")
    public AjaxResult getModelSimpleList(
            @RequestParam("type") Integer type,
            @RequestParam(value = "platform", required = false) String platform) {
        List<AiModel> list = aiModelService.getModelListByStatusAndType(
                CommonStatusEnum.ENABLE.getStatus(), type, platform);
        return success(convertList(list, model -> new AiModelRespVO().setId(model.getId())
                .setName(model.getName()).setModel(model.getModel()).setPlatform(model.getPlatform())));
    }

}
