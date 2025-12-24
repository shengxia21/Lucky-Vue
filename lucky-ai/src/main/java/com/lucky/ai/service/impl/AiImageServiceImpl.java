package com.lucky.ai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import com.lucky.ai.controller.image.vo.AiImageDrawReqVO;
import com.lucky.ai.controller.image.vo.AiImagePageReqVO;
import com.lucky.ai.controller.image.vo.AiImagePublicPageReqVO;
import com.lucky.ai.controller.image.vo.AiImageUpdateReqVO;
import com.lucky.ai.domain.AiImage;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.enums.image.AiImageStatusEnum;
import com.lucky.ai.enums.model.AiPlatformEnum;
import com.lucky.ai.factory.AsyncAiFactory;
import com.lucky.ai.mapper.AiImageMapper;
import com.lucky.ai.service.IAiImageService;
import com.lucky.ai.service.IAiModelService;
import com.lucky.common.constant.AiErrorConstants;
import com.lucky.common.exception.ServiceException;
import com.lucky.common.manager.AsyncManager;
import com.lucky.common.utils.DateUtils;
import com.lucky.common.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.zhipuai.ZhiPuAiImageOptions;
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
        // 1. 校验模型
        AiModel model = aiModelService.validateModel(drawReqVO.getModelId());

        // 2. 保存数据库
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

        // 3. 异步绘制，后续前端通过返回的 id 进行轮询结果
        AsyncManager.me().execute(AsyncAiFactory.executeDrawImage(image, drawReqVO, model));
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

    public static ImageOptions buildImageOptions(AiImageDrawReqVO draw, AiModel model) {
        if (ObjUtil.equal(model.getPlatform(), AiPlatformEnum.TONG_YI.getPlatform())) {
            return DashScopeImageOptions.builder()
                    .model(model.getModel()).n(1)
                    .height(draw.getHeight()).width(draw.getWidth())
                    .build();
        } else if (ObjUtil.equal(model.getPlatform(), AiPlatformEnum.ZHI_PU.getPlatform())) {
            return ZhiPuAiImageOptions.builder()
                    .model(model.getModel())
                    .build();
        }
        throw new IllegalArgumentException("不支持的 AI 平台：" + model.getPlatform());
    }

    private AiImage validateImageExists(Long id) {
        AiImage image = aiImageMapper.selectAiImageById(id);
        if (image == null) {
            throw new ServiceException(AiErrorConstants.IMAGE_NOT_EXISTS);
        }
        return image;
    }

}
