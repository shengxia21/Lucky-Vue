package com.lucky.ai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.lucky.ai.controller.image.vo.AiImageDrawReqVO;
import com.lucky.ai.controller.image.vo.AiImagePageReqVO;
import com.lucky.ai.controller.image.vo.AiImagePublicPageReqVO;
import com.lucky.ai.controller.image.vo.AiImageUpdateReqVO;
import com.lucky.ai.core.context.ImageContext;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.AiImage;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.enums.image.AiImageStatusEnum;
import com.lucky.ai.factory.AsyncAiFactory;
import com.lucky.ai.mapper.AiImageMapper;
import com.lucky.ai.service.IAiApiKeyService;
import com.lucky.ai.service.IAiImageService;
import com.lucky.ai.service.IAiModelService;
import com.lucky.common.constant.AiErrorConstants;
import com.lucky.common.exception.ServiceException;
import com.lucky.common.manager.AsyncManager;
import com.lucky.common.utils.DateUtils;
import com.lucky.common.utils.SecurityUtils;
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
    private AiImageMapper aiImageMapper;

    @Resource
    private IAiModelService aiModelService;
    @Resource
    private IAiApiKeyService aiApiKeyService;

    /**
     * 获取【我的】绘图分页
     *
     * @param pageReqVO 分页查询参数
     * @return 分页结果
     */
    @Override
    public List<AiImage> getImagePageMy(AiImagePageReqVO pageReqVO) {
        pageReqVO.setUserId(SecurityUtils.getUserId());
        return aiImageMapper.selectPageMy(pageReqVO);
    }

    /**
     * 获取公开的绘图
     *
     * @param pageReqVO 分页查询参数
     * @return 分页结果
     */
    @Override
    public List<AiImage> getImagePagePublic(AiImagePublicPageReqVO pageReqVO) {
        return aiImageMapper.selectPage(pageReqVO);
    }

    /**
     * 根据ID查询绘画详情
     *
     * @param id 绘画主键
     * @return 绘图详情
     */
    @Override
    public AiImage getImage(Long id) {
        return aiImageMapper.selectAiImageById(id);
    }

    /**
     * 根据ID列表查询绘画列表
     *
     * @param ids 绘画主键列表
     * @return 绘画列表
     */
    @Override
    public List<AiImage> getImageList(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return aiImageMapper.selectByIds(ids);
    }

    /**
     * 生成图片
     *
     * @param userId    用户ID
     * @param drawReqVO 绘图参数
     * @return 结果
     */
    @Override
    public Long drawImage(Long userId, AiImageDrawReqVO drawReqVO) {
        // 校验模型是否存在
        AiModel model = aiModelService.validateModel(drawReqVO.getModelId());
        // 校验apiKey是否存在
        AiApiKey apiKey = aiApiKeyService.validateApiKey(model.getKeyId());

        // 保存数据库
        AiImage image = BeanUtil.toBean(drawReqVO, AiImage.class);
        image.setUserId(userId);
        image.setPlatform(model.getPlatform());
        image.setModelId(model.getId());
        image.setModel(model.getModel());
        image.setPublicStatus(false);
        image.setStatus(AiImageStatusEnum.IN_PROGRESS.getStatus());
        image.setCreateBy(SecurityUtils.getUsername());
        image.setCreateTime(DateUtils.getNowDate());
        aiImageMapper.insertAiImage(image);

        // 构建图片上下文
        ImageContext imageContext = new ImageContext();
        imageContext.setImage(image);
        imageContext.setDrawReqVO(drawReqVO);
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
    public int deleteImageMy(Long id, Long userId) {
        // 1. 校验是否存在
        AiImage image = validateImageExists(id);
        if (ObjUtil.notEqual(image.getUserId(), userId)) {
            throw new ServiceException(AiErrorConstants.IMAGE_NOT_EXISTS);
        }
        // 2. 删除记录
        return aiImageMapper.deleteAiImageById(id);
    }

    /**
     * 获得绘画列表
     *
     * @param pageReqVO 分页查询参数
     * @return 分页结果
     */
    @Override
    public List<AiImage> getImagePage(AiImagePageReqVO pageReqVO) {
        return aiImageMapper.selectAiImagePage(pageReqVO);
    }

    /**
     * 更新绘画
     *
     * @param updateReqVO 更新参数
     * @return 结果
     */
    @Override
    public int updateImage(AiImageUpdateReqVO updateReqVO) {
        // 1. 校验存在
        validateImageExists(updateReqVO.getId());
        // 2. 更新发布状态
        AiImage image = BeanUtil.toBean(updateReqVO, AiImage.class);
        image.setUpdateBy(SecurityUtils.getUsername());
        image.setUpdateTime(DateUtils.getNowDate());
        return aiImageMapper.updateAiImage(image);
    }

    /**
     * 删除绘画
     *
     * @param id 绘画主键
     * @return 结果
     */
    @Override
    public int deleteImage(Long id) {
        // 1. 校验存在
        validateImageExists(id);
        // 2. 删除
        return aiImageMapper.deleteAiImageById(id);
    }

    private AiImage validateImageExists(Long id) {
        AiImage image = aiImageMapper.selectAiImageById(id);
        if (image == null) {
            throw new ServiceException(AiErrorConstants.IMAGE_NOT_EXISTS);
        }
        return image;
    }

}
