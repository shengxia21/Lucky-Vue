package com.lucky.web.controller.system;

import com.lucky.common.annotation.Log;
import com.lucky.common.core.controller.BaseController;
import com.lucky.common.core.domain.R;
import com.lucky.common.core.domain.entity.SysDictType;
import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.common.enums.BusinessType;
import com.lucky.common.utils.poi.ExcelUtil;
import com.lucky.system.domain.query.dict.SysDictTypeQuery;
import com.lucky.system.domain.query.dict.SysDictTypeSaveQuery;
import com.lucky.system.domain.vo.dict.SysDictTypeVO;
import com.lucky.system.service.ISysDictTypeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/dict/type")
public class SysDictTypeController extends BaseController {

    @Resource
    private ISysDictTypeService dictTypeService;

    /**
     * 获取字典类型列表
     */
    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    public TableDataInfo<SysDictTypeVO> list(PageQuery pageQuery, SysDictTypeQuery query) {
        return dictTypeService.selectDictTypeList(pageQuery, query);
    }

    /**
     * 导出字典类型列表
     */
    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @Log(title = "字典类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDictTypeQuery query) {
        List<SysDictType> list = dictTypeService.selectDictTypeList(query);
        ExcelUtil<SysDictType> util = new ExcelUtil<>(SysDictType.class);
        util.exportExcel(response, list, "字典类型");
    }

    /**
     * 根据字典类型编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictId}")
    public R<SysDictTypeVO> getInfo(@PathVariable Long dictId) {
        return R.ok(dictTypeService.selectDictTypeById(dictId));
    }

    /**
     * 新增字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysDictTypeSaveQuery dictType) {
        if (!dictTypeService.checkDictTypeUnique(dictType.getDictId(), dictType.getDictType())) {
            return R.fail("新增字典'" + dictType.getDictName() + "'失败，字典类型已存在");
        }
        return toAjax(dictTypeService.insertDictType(dictType));
    }

    /**
     * 修改字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysDictTypeSaveQuery dictType) {
        if (!dictTypeService.checkDictTypeUnique(dictType.getDictId(), dictType.getDictType())) {
            return R.fail("修改字典'" + dictType.getDictName() + "'失败，字典类型已存在");
        }
        return toAjax(dictTypeService.updateDictType(dictType));
    }

    /**
     * 删除字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictIds}")
    public R<Void> remove(@PathVariable Long[] dictIds) {
        dictTypeService.deleteDictTypeByIds(dictIds);
        return R.ok();
    }

    /**
     * 刷新字典缓存
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public R<Void> refreshCache() {
        dictTypeService.resetDictCache();
        return R.ok();
    }

    /**
     * 获取字典选择框列表
     */
    @GetMapping("/optionSelect")
    public R<List<SysDictTypeVO>> optionSelect() {
        return R.ok(dictTypeService.selectDictTypeAll());
    }

}