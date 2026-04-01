package com.lucky.ai.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.ai.domain.AiImage;
import com.lucky.ai.domain.query.image.ImagePagePublicQuery;
import com.lucky.ai.domain.query.image.ImagePageQuery;
import com.lucky.common.utils.StringUtils;

/**
 * AI 绘画Mapper接口
 *
 * @author lucky
 */
public interface AiImageMapper extends BaseMapper<AiImage> {

    default IPage<AiImage> selectPageMy(IPage<AiImage> page, ImagePageQuery query, Long userId) {
        LambdaQueryWrapper<AiImage> wrapper = Wrappers.<AiImage>lambdaQuery()
                .like(StringUtils.isNotEmpty(query.getPrompt()), AiImage::getPrompt, query.getPrompt())
                .eq(StringUtils.isNotNull(query.getPublicStatus()), AiImage::getPublicStatus, query.getPublicStatus())
                .eq(AiImage::getUserId, userId)
                .orderByDesc(AiImage::getCreateTime);
        return selectPage(page, wrapper);
    }

    default IPage<AiImage> selectPagePublic(IPage<AiImage> page, ImagePagePublicQuery query) {
        LambdaQueryWrapper<AiImage> wrapper = Wrappers.<AiImage>lambdaQuery()
                .like(StringUtils.isNotEmpty(query.getPrompt()), AiImage::getPrompt, query.getPrompt())
                .eq(AiImage::getPublicStatus, true)
                .orderByDesc(AiImage::getCreateTime);
        return selectPage(page, wrapper);
    }

    default IPage<AiImage> selectPage(IPage<AiImage> page, ImagePageQuery query) {
        LambdaQueryWrapper<AiImage> wrapper = Wrappers.<AiImage>lambdaQuery()
                .eq(StringUtils.isNotNull(query.getUserId()), AiImage::getUserId, query.getUserId())
                .eq(StringUtils.isNotEmpty(query.getPlatform()), AiImage::getPlatform, query.getPlatform())
                .eq(StringUtils.isNotNull(query.getStatus()), AiImage::getStatus, query.getStatus())
                .eq(StringUtils.isNotNull(query.getPublicStatus()), AiImage::getPublicStatus, query.getPublicStatus())
                .between(!query.getParams().isEmpty(), AiImage::getCreateTime, query.getParams().get("beginTime"), query.getParams().get("endTime"))
                .orderByDesc(AiImage::getCreateTime);
        return selectPage(page, wrapper);
    }

}