package com.lucky.ai.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.ai.domain.AiImage;
import com.lucky.ai.domain.query.image.AiImageMyQuery;
import com.lucky.ai.domain.query.image.AiImageQuery;
import com.lucky.ai.domain.query.image.AiImageUpdateQuery;
import com.lucky.ai.domain.vo.image.AiImageVO;
import com.lucky.ai.mapper.AiImageMapper;
import com.lucky.ai.service.IAiImageService;
import com.lucky.common.core.utils.MapstructUtils;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * AI 绘画Service业务层处理
 *
 * @author lucky
 */
@Service
public class AiImageServiceImpl implements IAiImageService {

    @Resource
    private AiImageMapper imageMapper;

    @Override
    public TableDataInfo<AiImageVO> selectMyImageList(PageQuery pageQuery, AiImageMyQuery query) {
        IPage<AiImageVO> page = imageMapper.selectMyPage(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    @Override
    public AiImageVO selectMyImageById(Long id) {
        return imageMapper.selectMyById(id);
    }

    @Override
    public List<AiImageVO> selectMyImageListByIds(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return imageMapper.selectMyListByIds(ids);
    }

    @Override
    public int deleteMyImageById(Long id) {
        return imageMapper.deleteMyById(id);
    }

    @Override
    public TableDataInfo<AiImageVO> selectImageList(PageQuery pageQuery, AiImageQuery query) {
        IPage<AiImageVO> page = imageMapper.selectPage(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    @Override
    public int updateImage(AiImageUpdateQuery query) {
        AiImage image = MapstructUtils.convert(query, AiImage.class);
        return imageMapper.updateById(image);
    }

    @Override
    public int deleteImageByIds(Long[] ids) {
        return imageMapper.deleteByIds(Arrays.asList(ids));
    }

}
