package com.lucky.ai.controller.image;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.lucky.ai.domain.query.image.AiImageMyQuery;
import com.lucky.ai.domain.query.image.AiImageQuery;
import com.lucky.ai.domain.query.image.AiImageUpdateQuery;
import com.lucky.ai.domain.vo.image.AiImageVO;
import com.lucky.ai.service.IAiImageService;
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
 * AI 绘画Controller
 *
 * @author lucky
 */
@RestController
@RequestMapping("/ai/image")
public class AiImageController extends BaseController {

    @Resource(name = "aiImageServiceImpl")
    private IAiImageService imageService;

    /**
     * 获取【我的】绘图分页
     */
    @GetMapping("/my/list")
    public TableDataInfo<AiImageVO> myList(PageQuery pageQuery, AiImageMyQuery query) {
        return imageService.selectMyImageList(pageQuery, query);
    }

    /**
     * 获取【我的】绘图记录
     */
    @GetMapping("/my/{id}")
    public R<AiImageVO> getMyInfo(@PathVariable Long id) {
        return R.ok(imageService.selectMyImageById(id));
    }

    /**
     * 获取【我的】绘图记录列表
     */
    @GetMapping("/my/list/{ids}")
    public R<List<AiImageVO>> myListByIds(@PathVariable List<Long> ids) {
        return R.ok(imageService.selectMyImageListByIds(ids));
    }

    /**
     * 删除【我的】绘图记录
     */
    @Log(title = "删除【我的】绘图记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/my/{id}")
    public R<Void> removeMy(@PathVariable Long id) {
        return toAjax(imageService.deleteMyImageById(id));
    }

    // ================ 绘图管理 ================

    /**
     * 获得绘画分页
     */
    @SaCheckPermission("ai:image:list")
    @GetMapping("/list")
    public TableDataInfo<AiImageVO> list(PageQuery pageQuery, AiImageQuery query) {
        return imageService.selectImageList(pageQuery, query);
    }

    /**
     * 更新绘画
     */
    @Log(title = "更新绘画", businessType = BusinessType.UPDATE)
    @SaCheckPermission("ai:image:edit")
    @PutMapping
    public R<Void> edit(@Validated @RequestBody AiImageUpdateQuery query) {
        return toAjax(imageService.updateImage(query));
    }

    /**
     * 删除绘画
     */
    @Log(title = "删除绘画", businessType = BusinessType.DELETE)
    @SaCheckPermission("ai:image:remove")
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(imageService.deleteImageByIds(ids));
    }

}
