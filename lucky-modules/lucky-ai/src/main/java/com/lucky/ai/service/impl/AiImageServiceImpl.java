package com.lucky.ai.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.ai.core.context.ImageContext;
import com.lucky.ai.core.vo.image.ImageDrawRequest;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.AiImage;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.domain.query.image.AiImagePagePublicQuery;
import com.lucky.ai.domain.query.image.AiImagePageQuery;
import com.lucky.ai.domain.query.image.AiImageUpdateQuery;
import com.lucky.ai.domain.vo.image.AiImageVO;
import com.lucky.ai.enums.image.AiImageStatusEnum;
import com.lucky.ai.factory.AsyncAiFactory;
import com.lucky.ai.mapper.AiImageMapper;
import com.lucky.ai.service.IAiApiKeyService;
import com.lucky.ai.service.IAiImageService;
import com.lucky.ai.service.IAiModelService;
import com.lucky.common.core.constant.AiErrorConstants;
import com.lucky.common.core.exception.ServiceException;
import com.lucky.common.core.utils.MapstructUtils;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import com.lucky.common.web.manager.AsyncManager;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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

    @Override
    public TableDataInfo<AiImageVO> getImagePageMy(PageQuery pageQuery, AiImagePageQuery query, Long userId) {
        IPage<AiImageVO> page = imageMapper.selectPageMy(pageQuery.build(), query, userId);
        return TableDataInfo.build(page);
    }

    @Override
    public TableDataInfo<AiImageVO> getImagePagePublic(PageQuery pageQuery, AiImagePagePublicQuery query) {
        IPage<AiImageVO> page = imageMapper.selectPagePublic(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    @Override
    public AiImageVO getImageById(Long id) {
        return imageMapper.selectVoById(id);
    }

    @Override
    public List<AiImageVO> getImageListByIdsAndUserId(List<Long> ids, Long userId) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return imageMapper.selectListByIdsAndUserId(ids, userId);
    }

    @Override
    public Long drawImage(Long userId, ImageDrawRequest request) {
        // 校验模型是否存在
        AiModel model = modelService.validateModel(request.getModelId());
        // 校验apiKey是否存在
        AiApiKey apiKey = apiKeyService.validateApiKey(model.getKeyId());

        // 保存数据库
        AiImage image = MapstructUtils.convert(request, AiImage.class);
        image.setUserId(userId);
        image.setPlatform(model.getPlatform());
        image.setModelId(model.getId());
        image.setModel(model.getModel());
        image.setPublicStatus(false);
        image.setStatus(AiImageStatusEnum.IN_PROGRESS.getStatus());
        imageMapper.insert(image);

        // 构建图片上下文
        ImageContext imageContext = new ImageContext();
        imageContext.setImage(image);
        imageContext.setRequest(request);
        imageContext.setModel(model);
        imageContext.setApiKey(apiKey);

        // 异步绘制，后续前端通过返回的 id 进行轮询结果
        AsyncManager.me().execute(AsyncAiFactory.executeDrawImage(imageContext));
        return image.getId();
    }

    @Override
    public int deleteImageMyById(Long id, Long userId) {
        // 1. 校验是否存在
        AiImage image = validateImageExists(id);
        if (ObjUtil.notEqual(image.getUserId(), userId)) {
            throw new ServiceException(AiErrorConstants.IMAGE_NOT_EXISTS);
        }
        // 2. 删除记录
        return imageMapper.deleteById(id);
    }

    @Override
    public TableDataInfo<AiImageVO> getImagePage(PageQuery pageQuery, AiImagePageQuery query) {
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
    public int deleteImageById(Long id) {
        // 1. 校验存在
        validateImageExists(id);
        // 2. 删除
        return imageMapper.deleteById(id);
    }

    private AiImage validateImageExists(Long id) {
        AiImage image = imageMapper.selectById(id);
        if (image == null) {
            throw new ServiceException(AiErrorConstants.IMAGE_NOT_EXISTS);
        }
        return image;
    }

}
