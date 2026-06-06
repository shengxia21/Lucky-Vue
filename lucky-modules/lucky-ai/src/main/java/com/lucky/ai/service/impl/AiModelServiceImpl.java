package com.lucky.ai.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.domain.query.model.AiModelPageQuery;
import com.lucky.ai.domain.query.model.AiModelSaveQuery;
import com.lucky.ai.domain.vo.model.AiModelVO;
import com.lucky.ai.enums.CommonStatusEnum;
import com.lucky.ai.enums.model.AiPlatformEnum;
import com.lucky.ai.mapper.AiModelMapper;
import com.lucky.ai.service.IAiApiKeyService;
import com.lucky.ai.service.IAiModelService;
import com.lucky.common.core.constant.AiErrorConstants;
import com.lucky.common.core.exception.ServiceException;
import com.lucky.common.core.utils.MapstructUtils;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI 模型Service业务层处理
 *
 * @author lucky
 */
@Service
public class AiModelServiceImpl implements IAiModelService {

    @Resource
    private AiModelMapper modelMapper;

    @Resource
    private IAiApiKeyService apiKeyService;

    @Override
    public AiModel getDefaultModelByType(Integer type) {
        AiModel model = modelMapper.selectOneByTypeAndStatus(type, CommonStatusEnum.ENABLE.getStatus());
        if (model == null) {
            throw new ServiceException(AiErrorConstants.MODEL_DEFAULT_NOT_EXISTS);
        }
        return model;
    }

    @Override
    public AiModel validateModel(Long id) {
        AiModel model = validateModelExists(id);
        if (CommonStatusEnum.isDisable(model.getStatus())) {
            throw new ServiceException(AiErrorConstants.MODEL_DISABLE);
        }
        return model;
    }

    @Override
    public Long createModel(AiModelSaveQuery query) {
        // 1. 校验
        AiPlatformEnum.validatePlatform(query.getPlatform());
        apiKeyService.validateApiKey(query.getKeyId());
        // 2. 插入
        AiModel model = MapstructUtils.convert(query, AiModel.class);
        modelMapper.insert(model);
        return model.getId();
    }

    @Override
    public int updateModel(AiModelSaveQuery query) {
        // 1. 校验
        validateModelExists(query.getId());
        AiPlatformEnum.validatePlatform(query.getPlatform());
        apiKeyService.validateApiKey(query.getKeyId());
        // 2. 更新
        AiModel model = MapstructUtils.convert(query, AiModel.class);
        return modelMapper.updateById(model);
    }

    @Override
    public int deleteModelById(Long id) {
        // 校验存在
        validateModelExists(id);
        // 删除
        return modelMapper.deleteById(id);
    }

    @Override
    public AiModelVO getModelById(Long id) {
        return modelMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<AiModelVO> getModelPage(PageQuery pageQuery, AiModelPageQuery query) {
        IPage<AiModelVO> selectPage = modelMapper.selectPage(pageQuery.build(), query);
        return TableDataInfo.build(selectPage);
    }

    @Override
    public List<AiModelVO> getModelList(Integer status, Integer type, String platform) {
        return modelMapper.selectList(status, type, platform);
    }

    private AiModel validateModelExists(Long id) {
        AiModel model = modelMapper.selectById(id);
        if (model == null) {
            throw new ServiceException(AiErrorConstants.MODEL_NOT_EXISTS);
        }
        return model;
    }

}