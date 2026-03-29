package com.lucky.ai.controller.model;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.ai.controller.model.vo.apikey.AiApiKeyPageReqVO;
import com.lucky.ai.controller.model.vo.apikey.AiApiKeyRespVO;
import com.lucky.ai.controller.model.vo.apikey.AiApiKeySaveReqVO;
import com.lucky.ai.controller.model.vo.model.AiModelRespVO;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.service.IAiApiKeyService;
import com.lucky.common.annotation.Log;
import com.lucky.common.core.controller.BaseController;
import com.lucky.common.core.domain.AjaxResult;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.common.enums.BusinessType;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.lucky.ai.util.CollectionUtils.convertList;

/**
 * AI API 秘钥Controller
 *
 * @author lucky
 */
@RestController
@RequestMapping("/ai/api-key")
public class AiApiKeyController extends BaseController {

    @Resource
    private IAiApiKeyService aiApiKeyService;

    /**
     * 创建 API 密钥
     */
    @Log(title = "创建 API 密钥", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('ai:api-key:create')")
    @PostMapping("/create")
    public AjaxResult createApiKey(@Validated @RequestBody AiApiKeySaveReqVO createReqVO) {
        return success(aiApiKeyService.createApiKey(createReqVO));
    }

    /**
     * 更新 API 密钥
     */
    @Log(title = "更新 API 密钥", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('ai:api-key:update')")
    @PutMapping("/update")
    public AjaxResult updateApiKey(@Validated @RequestBody AiApiKeySaveReqVO updateReqVO) {
        return toAjax(aiApiKeyService.updateApiKey(updateReqVO));
    }

    /**
     * 删除 API 密钥
     */
    @Log(title = "删除 API 密钥", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('ai:api-key:delete')")
    @DeleteMapping("/delete")
    public AjaxResult deleteApiKey(@RequestParam("id") Long id) {
        return toAjax(aiApiKeyService.deleteApiKey(id));
    }

    /**
     * 获取 API 密钥
     */
    @PreAuthorize("@ss.hasPermi('ai:api-key:query')")
    @GetMapping("/get")
    public AjaxResult getApiKey(@RequestParam("id") Long id) {
        AiApiKey apiKey = aiApiKeyService.getApiKey(id);
        return success(BeanUtil.toBean(apiKey, AiApiKeyRespVO.class));
    }

    /**
     * 获得 API 密钥分页
     */
    @PreAuthorize("@ss.hasPermi('ai:api-key:list')")
    @GetMapping("/page")
    public TableDataInfo getApiKeyPage(AiApiKeyPageReqVO pageReqVO) {
        startPage();
        List<AiApiKey> list = aiApiKeyService.getApiKeyPage(pageReqVO);
        return getDataTable(list, AiApiKeyRespVO.class);
    }

    /**
     * 获得 API 密钥分页列表
     */
    @GetMapping("/simple-list")
    public AjaxResult getApiKeySimpleList() {
        List<AiApiKey> list = aiApiKeyService.getApiKeyList();
        return success(convertList(list, key -> {
            AiApiKeyRespVO respVO = new AiApiKeyRespVO();
            respVO.setId(key.getId());
            respVO.setName(key.getName());
            return respVO;
        }));
    }

}
