package com.lucky.ai.controller.image;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.ObjUtil;
import com.lucky.ai.core.vo.image.ImageDrawRequest;
import com.lucky.ai.domain.query.image.AiImagePagePublicQuery;
import com.lucky.ai.domain.query.image.AiImagePageQuery;
import com.lucky.ai.domain.query.image.AiImageUpdateQuery;
import com.lucky.ai.domain.vo.image.AiImageVO;
import com.lucky.ai.service.AiImageService;
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

    @Resource
    private AiImageService AiImageService;

    /**
     * 获取【我的】绘图分页
     */
    @GetMapping("/my-page")
    public TableDataInfo<AiImageVO> getImagePageMy(PageQuery pageQuery, AiImagePageQuery query) {
        return AiImageService.getImagePageMy(pageQuery, query, getUserId());
    }

    /**
     * 获取公开的绘图分页
     */
    @GetMapping("/public-page")
    public TableDataInfo<AiImageVO> getImagePagePublic(PageQuery pageQuery, AiImagePagePublicQuery query) {
        return AiImageService.getImagePagePublic(pageQuery, query);
    }

    /**
     * 获取【我的】绘图记录
     */
    @GetMapping("/get-my")
    public R<AiImageVO> getImageMy(@RequestParam("id") Long id) {
        AiImageVO image = AiImageService.getImageById(id);
        if (image == null || ObjUtil.notEqual(getUserId(), image.getUserId())) {
            return R.fail("绘图记录不存在或不属于当前用户");
        }
        return R.ok(image);
    }

    /**
     * 获取【我的】绘图记录列表
     */
    @GetMapping("/my-list-by-ids")
    public R<List<AiImageVO>> getImageListMyByIds(@RequestParam("ids") List<Long> ids) {
        return R.ok(AiImageService.getImageListByIdsAndUserId(ids, getUserId()));
    }

    /**
     * 生成图片
     */
    @PostMapping("/draw")
    public R<Long> drawImage(@Validated @RequestBody ImageDrawRequest request) {
        return R.ok(AiImageService.drawImage(getUserId(), request));
    }

    /**
     * 删除【我的】绘图记录
     */
    @Log(title = "删除【我的】绘图记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete-my")
    public R<Integer> deleteImageMy(@RequestParam("id") Long id) {
        return R.ok(AiImageService.deleteImageMyById(id, getUserId()));
    }

    // ================ 绘图管理 ================

    /**
     * 获得绘画分页
     */
    @SaCheckPermission("ai:image:list")
    @GetMapping("/page")
    public TableDataInfo<AiImageVO> getImagePage(PageQuery pageQuery, AiImagePageQuery query) {
        return AiImageService.getImagePage(pageQuery, query);
    }

    /**
     * 更新绘画
     */
    @Log(title = "更新绘画", businessType = BusinessType.UPDATE)
    @SaCheckPermission("ai:image:update")
    @PutMapping("/update")
    public R<Integer> updateImage(@Validated @RequestBody AiImageUpdateQuery query) {
        return R.ok(AiImageService.updateImage(query));
    }

    /**
     * 删除绘画
     */
    @Log(title = "删除绘画", businessType = BusinessType.DELETE)
    @SaCheckPermission("ai:image:delete")
    @DeleteMapping("/delete")
    public R<Integer> deleteImage(@RequestParam("id") Long id) {
        return R.ok(AiImageService.deleteImageById(id));
    }

}
