package com.lucky.ai.controller.model;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.ai.controller.model.vo.model.AiModelPageReqVO;
import com.lucky.ai.controller.model.vo.model.AiModelRespVO;
import com.lucky.ai.controller.model.vo.model.AiModelSaveReqVO;
import com.lucky.ai.domain.AiModel;
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

import static com.lucky.ai.util.CollectionUtils.convertList;

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
    public R<Long> createModel(@Validated @RequestBody AiModelSaveReqVO createReqVO) {
        return R.ok(modelService.createModel(createReqVO));
    }

    /**
     * 更新模型
     */
    @Log(title = "更新模型", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('ai:model:update')")
    @PutMapping("/update")
    public R<Integer> updateModel(@Validated @RequestBody AiModelSaveReqVO updateReqVO) {
        return R.ok(modelService.updateModel(updateReqVO));
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
    public R<AiModelRespVO> getModel(@RequestParam("id") Long id) {
        AiModel model = modelService.getModelById(id);
        return R.ok(BeanUtil.toBean(model, AiModelRespVO.class));
    }

    /**
     * 获得模型分页
     */
    @PreAuthorize("@ss.hasPermi('ai:model:list')")
    @GetMapping("/page")
    public TableDataInfo<AiModelRespVO> getModelPage(PageQuery pageQuery, AiModelPageReqVO pageReqVO) {
        return modelService.getModelPage(pageQuery, pageReqVO);
    }

    /**
     * 获得模型列表
     */
    @GetMapping("/simple-list")
    public R<List<AiModelRespVO>> getModelSimpleList(
            @RequestParam("type") Integer type,
            @RequestParam(value = "platform", required = false) String platform) {
        List<AiModel> list = modelService.getModelListByStatusAndType(
                CommonStatusEnum.ENABLE.getStatus(), type, platform);
        return R.ok(convertList(list, model -> {
            AiModelRespVO respVO = new AiModelRespVO();
            respVO.setId(model.getId());
            respVO.setName(model.getName());
            respVO.setModel(model.getModel());
            respVO.setPlatform(model.getPlatform());
            return respVO;
        }));
    }

}
