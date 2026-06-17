package com.lucky.ai.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.AiImage;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.domain.query.image.AiImagePagePublicQuery;
import com.lucky.ai.domain.query.image.AiImagePageQuery;
import com.lucky.ai.domain.query.image.AiImageUpdateQuery;
import com.lucky.ai.domain.query.image.ImageQuery;
import com.lucky.ai.domain.vo.image.AiImageVO;
import com.lucky.ai.mapper.AiImageMapper;
import com.lucky.ai.service.IAiApiKeyService;
import com.lucky.ai.service.IAiImageService;
import com.lucky.ai.service.IAiModelService;
import com.lucky.common.ai.domain.request.ImageRequest;
import com.lucky.common.ai.enums.AiImageStatusEnum;
import com.lucky.common.core.constant.AiErrorConstants;
import com.lucky.common.core.exception.ServiceException;
import com.lucky.common.core.utils.MapstructUtils;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import com.lucky.common.security.utils.SecurityUtils;
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

    @Resource
    private IAiModelService modelService;
    @Resource
    private IAiApiKeyService apiKeyService;

    @Resource
    private ImageServiceFacade imageServiceFacade;

    @Override
    public TableDataInfo<AiImageVO> selectMyImageList(PageQuery pageQuery, AiImagePageQuery query) {
        IPage<AiImageVO> page = imageMapper.selectMyPage(pageQuery.build(), query, SecurityUtils.getUserId());
        return TableDataInfo.build(page);
    }

    @Override
    public TableDataInfo<AiImageVO> selectPublicImageList(PageQuery pageQuery, AiImagePagePublicQuery query) {
        IPage<AiImageVO> page = imageMapper.selectPublicPage(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    @Override
    public AiImageVO selectImageById(Long id) {
        return imageMapper.selectVoById(id);
    }

    @Override
    public List<AiImageVO> selectMyImageListByIds(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return imageMapper.selectListByIdsAndUserId(ids, SecurityUtils.getUserId());
    }

    @Override
    public Long drawImage(ImageQuery request) {
        // 校验模型是否存在
        AiModel model = modelService.validateModel(request.getModelId());
        // 校验apiKey是否存在
        AiApiKey apiKey = apiKeyService.validateApiKey(model.getKeyId());

        // 保存数据库
        AiImage image = MapstructUtils.convert(request, AiImage.class);
        image.setUserId(SecurityUtils.getUserId());
        image.setPlatform(model.getPlatform());
        image.setModelId(model.getId());
        image.setModel(model.getModel());
        image.setPublicStatus(false);
        image.setStatus(AiImageStatusEnum.IN_PROGRESS.getStatus());
        imageMapper.insert(image);

        // 构建图片请求
        ImageRequest imageRequest = new ImageRequest();
        imageRequest.setPrompt(request.getPrompt());
        imageRequest.setWidth(request.getWidth());
        imageRequest.setHeight(request.getHeight());
        imageRequest.setOptions(request.getOptions());
        imageRequest.setImageId(image.getId());
        imageRequest.setModel(model.getModel());
        imageRequest.setPlatform(model.getPlatform());
        imageRequest.setApiKey(apiKey.getApiKey());
        imageRequest.setUrl(apiKey.getUrl());

        // 异步绘制，后续前端通过返回的 id 进行轮询结果
        imageServiceFacade.generateImage(imageRequest);
        return image.getId();
    }

    @Override
    public int deleteMyImageById(Long id) {
        // 1. 校验是否存在
        AiImage image = validateImageExists(id);
        if (ObjUtil.notEqual(image.getUserId(), SecurityUtils.getUserId())) {
            throw new ServiceException(AiErrorConstants.IMAGE_NOT_EXISTS);
        }
        // 2. 删除记录
        return imageMapper.deleteById(id);
    }

    @Override
    public TableDataInfo<AiImageVO> selectImageList(PageQuery pageQuery, AiImagePageQuery query) {
        IPage<AiImageVO> page = imageMapper.selectPage(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    @Override
    public int updateImage(AiImageUpdateQuery query) {
        // 1. 校验存在
        validateImageExists(query.getId());
        // 2. 更新发布状态
        AiImage image = MapstructUtils.convert(query, AiImage.class);
        return imageMapper.updateById(image);
    }

    @Override
    public int deleteImageByIds(Long[] ids) {
        return imageMapper.deleteByIds(Arrays.asList(ids));
    }

    private AiImage validateImageExists(Long id) {
        AiImage image = imageMapper.selectById(id);
        if (image == null) {
            throw new ServiceException(AiErrorConstants.IMAGE_NOT_EXISTS);
        }
        return image;
    }

}
