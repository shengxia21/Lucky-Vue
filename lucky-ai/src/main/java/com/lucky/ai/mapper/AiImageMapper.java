package com.lucky.ai.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.ai.domain.AiImage;
import com.lucky.ai.domain.query.image.AiImagePagePublicQuery;
import com.lucky.ai.domain.query.image.AiImagePageQuery;
import com.lucky.ai.domain.vo.image.AiImageVO;
import com.lucky.common.core.mybatis.BaseMapperX;
import com.lucky.common.utils.StringUtils;

import java.util.List;

/**
 * AI 绘画Mapper接口
 *
 * @author lucky
 */
public interface AiImageMapper extends BaseMapperX<AiImage, AiImageVO> {

    default IPage<AiImageVO> selectPageMy(IPage<AiImage> page, AiImagePageQuery query, Long userId) {
        LambdaQueryWrapper<AiImage> wrapper = Wrappers.<AiImage>lambdaQuery()
                .like(StringUtils.isNotEmpty(query.getPrompt()), AiImage::getPrompt, query.getPrompt())
                .eq(StringUtils.isNotNull(query.getPublicStatus()), AiImage::getPublicStatus, query.getPublicStatus())
                .eq(AiImage::getUserId, userId)
                .orderByDesc(AiImage::getCreateTime);
        return selectVoPage(page, wrapper);
    }

    default IPage<AiImageVO> selectPagePublic(IPage<AiImage> page, AiImagePagePublicQuery query) {
        LambdaQueryWrapper<AiImage> wrapper = Wrappers.<AiImage>lambdaQuery()
                .like(StringUtils.isNotEmpty(query.getPrompt()), AiImage::getPrompt, query.getPrompt())
                .eq(AiImage::getPublicStatus, true)
                .orderByDesc(AiImage::getCreateTime);
        return selectVoPage(page, wrapper);
    }

    default IPage<AiImageVO> selectPage(IPage<AiImage> page, AiImagePageQuery query) {
        LambdaQueryWrapper<AiImage> wrapper = Wrappers.<AiImage>lambdaQuery()
                .eq(StringUtils.isNotNull(query.getUserId()), AiImage::getUserId, query.getUserId())
                .eq(StringUtils.isNotEmpty(query.getPlatform()), AiImage::getPlatform, query.getPlatform())
                .eq(StringUtils.isNotNull(query.getStatus()), AiImage::getStatus, query.getStatus())
                .eq(StringUtils.isNotNull(query.getPublicStatus()), AiImage::getPublicStatus, query.getPublicStatus())
                .between(!query.getParams().isEmpty(), AiImage::getCreateTime, query.getParams().get("beginTime"), query.getParams().get("endTime"))
                .orderByDesc(AiImage::getCreateTime);
        return selectVoPage(page, wrapper);
    }

    default List<AiImageVO> selectListByIdsAndUserId(List<Long> ids, Long userId) {
        LambdaQueryWrapper<AiImage> wrapper = Wrappers.<AiImage>lambdaQuery()
                .in(AiImage::getId, ids)
                .eq(AiImage::getUserId, userId);
        return selectVoList(wrapper);
    }

}