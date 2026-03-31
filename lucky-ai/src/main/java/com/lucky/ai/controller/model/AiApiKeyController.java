package com.lucky.ai.controller.model;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.ai.controller.model.vo.apikey.AiApiKeyPageReqVO;
import com.lucky.ai.controller.model.vo.apikey.AiApiKeyRespVO;
import com.lucky.ai.controller.model.vo.apikey.AiApiKeySaveReqVO;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.service.AiApiKeyService;
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
 * AI API 秘钥Controller
 *
 * @author lucky
 */
@RestController
@RequestMapping("/ai/api-key")
public class AiApiKeyController extends BaseController {

    @Resource
    private AiApiKeyService apiKeyService;

    /**
     * 创建 API 密钥
     */
    @Log(title = "创建 API 密钥", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('ai:api-key:create')")
    @PostMapping("/create")
    public R<Long> createApiKey(@Validated @RequestBody AiApiKeySaveReqVO createReqVO) {
        return R.ok(apiKeyService.createApiKey(createReqVO));
    }

    /**
     * 更新 API 密钥
     */
    @Log(title = "更新 API 密钥", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('ai:api-key:update')")
    @PutMapping("/update")
    public R<Integer> updateApiKey(@Validated @RequestBody AiApiKeySaveReqVO updateReqVO) {
        return R.ok(apiKeyService.updateApiKey(updateReqVO));
    }

    /**
     * 删除 API 密钥
     */
    @Log(title = "删除 API 密钥", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('ai:api-key:delete')")
    @DeleteMapping("/delete")
    public R<Integer> deleteApiKey(@RequestParam("id") Long id) {
        return R.ok(apiKeyService.deleteApiKeyById(id));
    }

    /**
     * 获取 API 密钥
     */
    @PreAuthorize("@ss.hasPermi('ai:api-key:query')")
    @GetMapping("/get")
    public R<AiApiKeyRespVO> getApiKey(@RequestParam("id") Long id) {
        AiApiKey apiKey = apiKeyService.getApiKeyById(id);
        return R.ok(BeanUtil.toBean(apiKey, AiApiKeyRespVO.class));
    }

    /**
     * 获得 API 密钥分页
     */
    @PreAuthorize("@ss.hasPermi('ai:api-key:list')")
    @GetMapping("/page")
    public TableDataInfo<AiApiKeyRespVO> getApiKeyPage(PageQuery pageQuery, AiApiKeyPageReqVO pageReqVO) {
        return apiKeyService.getApiKeyPage(pageQuery, pageReqVO);
    }

    /**
     * 获得 API 密钥列表
     */
    @GetMapping("/simple-list")
    public R<List<AiApiKeyRespVO>> getApiKeySimpleList() {
        List<AiApiKey> list = apiKeyService.getApiKeyList();
        return R.ok(convertList(list, key -> {
            AiApiKeyRespVO respVO = new AiApiKeyRespVO();
            respVO.setId(key.getId());
            respVO.setName(key.getName());
            return respVO;
        }));
    }

}
