package com.lucky.ai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.ai.controller.model.vo.model.AiModelPageReqVO;
import com.lucky.ai.controller.model.vo.model.AiModelSaveReqVO;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.enums.CommonStatusEnum;
import com.lucky.ai.enums.model.AiPlatformEnum;
import com.lucky.ai.factory.AiModelFactory;
import com.lucky.ai.mapper.AiModelMapper;
import com.lucky.ai.service.IAiApiKeyService;
import com.lucky.ai.service.IAiModelService;
import com.lucky.common.constant.AiErrorConstants;
import com.lucky.common.exception.ServiceException;
import com.lucky.common.utils.DateUtils;
import com.lucky.common.utils.SecurityUtils;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.image.ImageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI 模型Service业务层处理
 *
 * @author lucky
 */
@Service
public class AiModelServiceImpl implements IAiModelService {

    @Autowired
    private AiModelMapper aiModelMapper;

    @Autowired
    private IAiApiKeyService aiApiKeyService;

    @Autowired
    private AiModelFactory modelFactory;

    /**
     * 获得默认的模型
     * <p>
     * 如果获取不到，则抛出 {@link com.lucky.common.exception.ServiceException}
     *
     * @param type 模型类型
     * @return 模型
     */
    @Override
    public AiModel getRequiredDefaultModel(Integer type) {
        AiModel aiModel = aiModelMapper.selectFirstByStatus(type, CommonStatusEnum.ENABLE.getStatus());
        if (aiModel == null) {
            throw new ServiceException(AiErrorConstants.MODEL_DEFAULT_NOT_EXISTS);
        }
        return aiModel;
    }

    /**
     * 校验模型是否可使用
     *
     * @param id 编号
     * @return 模型
     */
    @Override
    public AiModel validateModel(Long id) {
        AiModel model = validateModelExists(id);
        if (CommonStatusEnum.isDisable(model.getStatus())) {
            throw new ServiceException(AiErrorConstants.MODEL_DISABLE);
        }
        return model;
    }

    /**
     * 创建模型
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    @Override
    public Long createModel(AiModelSaveReqVO createReqVO) {
        // 1. 校验
        AiPlatformEnum.validatePlatform(createReqVO.getPlatform());
        aiApiKeyService.validateApiKey(createReqVO.getKeyId());

        // 2. 插入
        AiModel model = BeanUtil.toBean(createReqVO, AiModel.class);
        model.setCreateBy(SecurityUtils.getUsername());
        model.setCreateTime(DateUtils.getNowDate());
        aiModelMapper.insertAiModel(model);
        return model.getId();
    }

    /**
     * 更新模型
     *
     * @param updateReqVO 更新信息
     * @return 影响行数
     */
    @Override
    public int updateModel(AiModelSaveReqVO updateReqVO) {
        // 1. 校验
        validateModelExists(updateReqVO.getId());
        AiPlatformEnum.validatePlatform(updateReqVO.getPlatform());
        aiApiKeyService.validateApiKey(updateReqVO.getKeyId());

        // 2. 更新
        AiModel updateObj = BeanUtil.toBean(updateReqVO, AiModel.class);
        updateObj.setUpdateBy(SecurityUtils.getUsername());
        updateObj.setUpdateTime(DateUtils.getNowDate());
        return aiModelMapper.updateAiModel(updateObj);
    }

    /**
     * 删除模型
     *
     * @param id 编号
     * @return 影响行数
     */
    @Override
    public int deleteModel(Long id) {
        // 校验存在
        validateModelExists(id);
        // 删除
        return aiModelMapper.deleteAiModelById(id);
    }

    /**
     * 获得模型
     *
     * @param id 编号
     * @return 模型
     */
    @Override
    public AiModel getModel(Long id) {
        return aiModelMapper.selectAiModelById(id);
    }

    /**
     * 获得模型分页
     *
     * @param pageReqVO 分页查询
     * @return 模型分页
     */
    @Override
    public List<AiModel> getModelPage(AiModelPageReqVO pageReqVO) {
        return aiModelMapper.selectPage(pageReqVO);
    }

    /**
     * 获得模型列表
     *
     * @param status   状态
     * @param type     类型
     * @param platform 平台
     * @return 模型列表
     */
    @Override
    public List<AiModel> getModelListByStatusAndType(Integer status, Integer type, String platform) {
        return aiModelMapper.selectListByStatusAndType(status, type, platform);
    }

    /**
     * 获得 ChatModel 对象
     *
     * @param id 编号
     * @return ChatModel 对象
     */
    @Override
    public ChatModel getChatModel(Long id) {
        AiModel model = validateModel(id);
        AiApiKey apiKey = aiApiKeyService.validateApiKey(model.getKeyId());
        AiPlatformEnum platform = AiPlatformEnum.validatePlatform(apiKey.getPlatform());
        return modelFactory.getOrCreateChatModel(platform, apiKey.getApiKey(), apiKey.getUrl());
    }

    /**
     * 获得 ImageModel 对象
     *
     * @param id 编号
     * @return ImageModel 对象
     */
    @Override
    public ImageModel getImageModel(Long id) {
        AiModel model = validateModel(id);
        AiApiKey apiKey = aiApiKeyService.validateApiKey(model.getKeyId());
        AiPlatformEnum platform = AiPlatformEnum.validatePlatform(apiKey.getPlatform());
        return modelFactory.getOrCreateImageModel(platform, apiKey.getApiKey(), apiKey.getUrl());
    }

    private AiModel validateModelExists(Long id) {
        AiModel model = aiModelMapper.selectAiModelById(id);
        if (model == null) {
            throw new ServiceException(AiErrorConstants.MODEL_NOT_EXISTS);
        }
        return model;
    }

}
