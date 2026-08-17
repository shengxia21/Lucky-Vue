package com.lucky.ai.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.ai.domain.AiImage;
import com.lucky.ai.domain.query.image.AiImageMyQuery;
import com.lucky.ai.domain.query.image.AiImageQuery;
import com.lucky.ai.domain.vo.image.AiImageVO;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.mybatis.core.mapper.BaseMapperX;
import com.lucky.common.security.utils.SecurityUtils;

import java.util.List;

/**
 * AI 绘画Mapper接口
 *
 * @author lucky
 */
public interface AiImageMapper extends BaseMapperX<AiImage, AiImageVO> {

    default IPage<AiImageVO> selectMyPage(IPage<AiImage> page, AiImageMyQuery query) {
        LambdaQueryWrapper<AiImage> wrapper = Wrappers.<AiImage>lambdaQuery()
                .like(StringUtils.isNotEmpty(query.getPrompt()), AiImage::getPrompt, query.getPrompt())
                .eq(StringUtils.isNotNull(query.getIsPublic()), AiImage::getIsPublic, query.getIsPublic())
                .eq(AiImage::getUserId, SecurityUtils.getUserId())
                .orderByDesc(AiImage::getCreateTime);
        return selectVoPage(page, wrapper);
    }

    default AiImageVO selectMyById(Long id) {
        LambdaQueryWrapper<AiImage> wrapper = Wrappers.<AiImage>lambdaQuery()
                .eq(AiImage::getId, id)
                .eq(AiImage::getUserId, SecurityUtils.getUserId());
        return selectVoOne(wrapper, false);
    }

    default List<AiImageVO> selectMyListByIds(List<Long> ids) {
        LambdaQueryWrapper<AiImage> wrapper = Wrappers.<AiImage>lambdaQuery()
                .in(AiImage::getId, ids)
                .eq(AiImage::getUserId, SecurityUtils.getUserId());
        return selectVoList(wrapper);
    }

    default int deleteMyById(Long id) {
        LambdaQueryWrapper<AiImage> wrapper = Wrappers.<AiImage>lambdaQuery()
                .eq(AiImage::getId, id)
                .eq(AiImage::getUserId, SecurityUtils.getUserId());
        return delete(wrapper);
    }

    default IPage<AiImageVO> selectPage(IPage<AiImage> page, AiImageQuery query) {
        LambdaQueryWrapper<AiImage> wrapper = Wrappers.<AiImage>lambdaQuery()
                .eq(StringUtils.isNotNull(query.getUserId()), AiImage::getUserId, query.getUserId())
                .eq(StringUtils.isNotEmpty(query.getPlatform()), AiImage::getPlatform, query.getPlatform())
                .eq(StringUtils.isNotNull(query.getGenerateStatus()), AiImage::getGenerateStatus, query.getGenerateStatus())
                .eq(StringUtils.isNotNull(query.getIsPublic()), AiImage::getIsPublic, query.getIsPublic())
                .between(!query.getParams().isEmpty(), AiImage::getCreateTime, query.getParams().get("beginTime"), query.getParams().get("endTime"))
                .orderByDesc(AiImage::getCreateTime);
        return selectVoPage(page, wrapper);
    }

}
