package com.lucky.ai.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.ai.core.context.ImageContext;
import com.lucky.ai.core.vo.image.ImageDrawRequest;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.AiImage;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.domain.query.image.ImagePagePublicQuery;
import com.lucky.ai.domain.query.image.ImagePageQuery;
import com.lucky.ai.domain.query.image.ImageUpdateQuery;
import com.lucky.ai.domain.vo.image.ImageVO;
import com.lucky.ai.enums.image.AiImageStatusEnum;
import com.lucky.ai.factory.AsyncAiFactory;
import com.lucky.ai.mapper.AiImageMapper;
import com.lucky.ai.service.AiApiKeyService;
import com.lucky.ai.service.AiImageService;
import com.lucky.ai.service.AiModelService;
import com.lucky.common.constant.AiErrorConstants;
import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.common.exception.ServiceException;
import com.lucky.common.manager.AsyncManager;
import com.lucky.common.utils.MapstructUtils;
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
public class AiImageServiceImpl implements AiImageService {

    @Resource
    private AiImageMapper imageMapper;

    @Resource
    private AiModelService modelService;
    @Resource
    private AiApiKeyService apiKeyService;

    /**
     * 获取【我的】绘图分页
     *
     * @param query 分页查询参数
     * @return 分页结果
     */
    @Override
    public TableDataInfo<ImageVO> getImagePageMy(PageQuery pageQuery, ImagePageQuery query, Long userId) {
        IPage<ImageVO> page = imageMapper.selectPageMy(pageQuery.build(), query, userId);
        return TableDataInfo.build(page);
    }

    /**
     * 获取公开的绘图
     *
     * @param query 分页查询参数
     * @return 分页结果
     */
    @Override
    public TableDataInfo<ImageVO> getImagePagePublic(PageQuery pageQuery, ImagePagePublicQuery query) {
        IPage<ImageVO> page = imageMapper.selectPagePublic(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    /**
     * 根据ID查询绘画详情
     *
     * @param id 绘画主键
     * @return 绘图详情
     */
    @Override
    public ImageVO getImageById(Long id) {
        return imageMapper.selectVoById(id);
    }

    /**
     * 根据ID列表查询绘画列表
     *
     * @param ids    绘画主键列表
     * @param userId 用户ID
     * @return 绘画列表
     */
    @Override
    public List<ImageVO> getImageListByIdsAndUserId(List<Long> ids, Long userId) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return imageMapper.selectListByIdsAndUserId(ids, userId);
    }

    /**
     * 生成图片
     *
     * @param userId  用户ID
     * @param request 绘图参数
     * @return 结果
     */
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

    /**
     * 删除【我的】绘图记录
     *
     * @param id     绘图记录ID
     * @param userId 用户ID
     * @return 结果
     */
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

    /**
     * 获得绘画列表
     *
     * @param query 分页查询参数
     * @return 分页结果
     */
    @Override
    public TableDataInfo<ImageVO> getImagePage(PageQuery pageQuery, ImagePageQuery query) {
        IPage<ImageVO> page = imageMapper.selectPage(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    /**
     * 更新绘画
     *
     * @param query 更新参数
     * @return 结果
     */
    @Override
    public int updateImage(ImageUpdateQuery query) {
        // 1. 校验存在
        validateImageExists(query.getId());
        // 2. 更新发布状态
        AiImage image = MapstructUtils.convert(query, AiImage.class);
        return imageMapper.updateById(image);
    }

    /**
     * 删除绘画
     *
     * @param id 绘画主键
     * @return 结果
     */
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
