package com.lucky.ai.controller.image;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import com.lucky.ai.controller.image.vo.*;
import com.lucky.ai.domain.AiImage;
import com.lucky.ai.service.IAiImageService;
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

/**
 * AI 绘画Controller
 *
 * @author lucky
 */
@RestController
@RequestMapping("/ai/image")
public class AiImageController extends BaseController {

    @Autowired
    private IAiImageService aiImageService;

    /**
     * 获取【我的】绘图分页
     */
    @GetMapping("/my-page")
    public TableDataInfo getImagePageMy(@Validated AiImagePageReqVO pageReqVO) {
        startPage();
        List<AiImage> pageResult = aiImageService.getImagePageMy(pageReqVO);
        return getDataTable(BeanUtil.copyToList(pageResult, AiImageRespVO.class));
    }

    /**
     * 获取公开的绘图分页
     */
    @GetMapping("/public-page")
    public TableDataInfo getImagePagePublic(AiImagePublicPageReqVO pageReqVO) {
        startPage();
        List<AiImage> pageResult = aiImageService.getImagePagePublic(pageReqVO);
        return getDataTable(BeanUtil.copyToList(pageResult, AiImageRespVO.class));
    }

    /**
     * 获取【我的】绘图记录
     */
    @GetMapping("/get-my")
    public AjaxResult getImageMy(@RequestParam("id") Long id) {
        AiImage image = aiImageService.getImage(id);
        if (image == null || ObjUtil.notEqual(getUserId(), image.getUserId())) {
            return success(null);
        }
        return success(BeanUtil.toBean(image, AiImageRespVO.class));
    }

    /**
     * 获取【我的】绘图记录列表
     */
    @GetMapping("/my-list-by-ids")
    public AjaxResult getImageListMyByIds(@RequestParam("ids") List<Long> ids) {
        List<AiImage> imageList = aiImageService.getImageList(ids);
        imageList.removeIf(item -> !ObjUtil.equal(getUserId(), item.getUserId()));
        return success(BeanUtil.copyToList(imageList, AiImageRespVO.class));
    }

    /**
     * 生成图片
     */
    @PostMapping("/draw")
    public AjaxResult drawImage(@Validated @RequestBody AiImageDrawReqVO drawReqVO) {
        return success(aiImageService.drawImage(getUserId(), drawReqVO));
    }

    /**
     * 删除【我的】绘图记录
     */
    @Log(title = "删除【我的】绘图记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete-my")
    public AjaxResult deleteImageMy(@RequestParam("id") Long id) {
        return toAjax(aiImageService.deleteImageMy(id, getUserId()));
    }

    // ================ 绘图管理 ================

    /**
     * 获得绘画分页
     */
    @PreAuthorize("@ss.hasPermi('ai:image:query')")
    @GetMapping("/page")
    public TableDataInfo getImagePage(AiImagePageReqVO pageReqVO) {
        startPage();
        List<AiImage> list = aiImageService.getImagePage(pageReqVO);
        return getDataTable(BeanUtil.copyToList(list, AiImageRespVO.class));
    }

    /**
     * 更新绘画
     */
    @Log(title = "更新绘画", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('ai:image:update')")
    @PutMapping("/update")
    public AjaxResult updateImage(@Validated @RequestBody AiImageUpdateReqVO updateReqVO) {
        return toAjax(aiImageService.updateImage(updateReqVO));
    }

    /**
     * 删除绘画
     */
    @Log(title = "删除绘画", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('ai:image:delete')")
    @DeleteMapping("/delete")
    public AjaxResult deleteImage(@RequestParam("id") Long id) {
        return toAjax(aiImageService.deleteImage(id));
    }

}
