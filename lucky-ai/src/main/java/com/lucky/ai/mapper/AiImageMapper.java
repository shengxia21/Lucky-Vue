package com.lucky.ai.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.ai.controller.image.vo.AiImagePagePublicReqVO;
import com.lucky.ai.controller.image.vo.AiImagePageReqVO;
import com.lucky.ai.domain.AiImage;
import com.lucky.common.utils.StringUtils;

/**
 * AI 绘画Mapper接口
 *
 * @author lucky
 */
public interface AiImageMapper extends BaseMapper<AiImage> {

    default IPage<AiImage> selectPageMy(IPage<AiImage> page, AiImagePageReqVO pageReqVO, Long userId) {
        LambdaQueryWrapper<AiImage> wrapper = Wrappers.<AiImage>lambdaQuery()
                .like(StringUtils.isNotEmpty(pageReqVO.getPrompt()), AiImage::getPrompt, pageReqVO.getPrompt())
                .eq(StringUtils.isNotNull(pageReqVO.getPublicStatus()), AiImage::getPublicStatus, pageReqVO.getPublicStatus())
                .eq(AiImage::getUserId, userId)
                .orderByDesc(AiImage::getCreateTime);
        return selectPage(page, wrapper);
    }

    default IPage<AiImage> selectPagePublic(IPage<AiImage> page, AiImagePagePublicReqVO pageReqVO) {
        LambdaQueryWrapper<AiImage> wrapper = Wrappers.<AiImage>lambdaQuery()
                .like(StringUtils.isNotEmpty(pageReqVO.getPrompt()), AiImage::getPrompt, pageReqVO.getPrompt())
                .eq(AiImage::getPublicStatus, true)
                .orderByDesc(AiImage::getCreateTime);
        return selectPage(page, wrapper);
    }

    default IPage<AiImage> selectPage(IPage<AiImage> page, AiImagePageReqVO pageReqVO) {
        LambdaQueryWrapper<AiImage> wrapper = Wrappers.<AiImage>lambdaQuery()
                .eq(StringUtils.isNotNull(pageReqVO.getUserId()), AiImage::getUserId, pageReqVO.getUserId())
                .eq(StringUtils.isNotEmpty(pageReqVO.getPlatform()), AiImage::getPlatform, pageReqVO.getPlatform())
                .eq(StringUtils.isNotNull(pageReqVO.getStatus()), AiImage::getStatus, pageReqVO.getStatus())
                .eq(StringUtils.isNotNull(pageReqVO.getPublicStatus()), AiImage::getPublicStatus, pageReqVO.getPublicStatus())
                .between(!pageReqVO.getParams().isEmpty(), AiImage::getCreateTime, pageReqVO.getParams().get("beginTime"), pageReqVO.getParams().get("endTime"))
                .orderByDesc(AiImage::getCreateTime);
        return selectPage(page, wrapper);
    }

}