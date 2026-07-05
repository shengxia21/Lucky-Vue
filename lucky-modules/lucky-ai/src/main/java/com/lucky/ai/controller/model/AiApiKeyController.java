package com.lucky.ai.controller.model;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lucky.ai.domain.query.apiKey.AiApiKeyPageQuery;
import com.lucky.ai.domain.query.apiKey.AiApiKeySaveQuery;
import com.lucky.ai.domain.vo.apikey.AiApiKeyVO;
import com.lucky.ai.service.IAiApiKeyService;
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
 * AI API 秘钥Controller
 *
 * @author lucky
 */
@RestController
@RequestMapping("/ai/apiKey")
public class AiApiKeyController extends BaseController {

    @Resource
    private IAiApiKeyService apiKeyService;

    /**
     * 创建 API 密钥
     */
    @Log(title = "创建 API 密钥", businessType = BusinessType.INSERT)
    @SaCheckPermission("ai:api-key:add")
    @PostMapping
    public R<Void> add(@Validated @RequestBody AiApiKeySaveQuery query) {
        return toAjax(apiKeyService.insertApiKey(query));
    }

    /**
     * 更新 API 密钥
     */
    @Log(title = "更新 API 密钥", businessType = BusinessType.UPDATE)
    @SaCheckPermission("ai:api-key:edit")
    @PutMapping
    public R<Void> edit(@Validated @RequestBody AiApiKeySaveQuery query) {
        return toAjax(apiKeyService.updateApiKey(query));
    }

    /**
     * 删除 API 密钥
     */
    @Log(title = "删除 API 密钥", businessType = BusinessType.DELETE)
    @SaCheckPermission("ai:api-key:remove")
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(apiKeyService.deleteApiKeyByIds(ids));
    }

    /**
     * 获取 API 密钥
     */
    @SaCheckPermission("ai:api-key:query")
    @GetMapping("/{id}")
    public R<AiApiKeyVO> getInfo(@PathVariable Long id) {
        return R.ok(apiKeyService.selectApiKeyById(id));
    }

    /**
     * 获得 API 密钥分页
     */
    @SaCheckPermission("ai:api-key:list")
    @GetMapping("/list")
    public TableDataInfo<AiApiKeyVO> list(PageQuery pageQuery, AiApiKeyPageQuery query) {
        return apiKeyService.selectApiKeyList(pageQuery, query);
    }

    /**
     * 获得 API 密钥列表
     */
    @GetMapping("/optionSelect")
    public R<List<AiApiKeyVO>> optionSelect() {
        return R.ok(apiKeyService.selectApiKeyAll());
    }

}
