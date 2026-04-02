package com.lucky.ai.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.domain.query.model.AiModelPageQuery;
import com.lucky.ai.domain.query.model.AiModelSaveQuery;
import com.lucky.ai.domain.vo.model.AiModelVO;
import com.lucky.ai.enums.CommonStatusEnum;
import com.lucky.ai.enums.model.AiPlatformEnum;
import com.lucky.ai.mapper.AiModelMapper;
import com.lucky.ai.service.AiApiKeyService;
import com.lucky.ai.service.AiModelService;
import com.lucky.common.constant.AiErrorConstants;
import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.common.exception.ServiceException;
import com.lucky.common.utils.MapstructUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI 模型Service业务层处理
 *
 * @author lucky
 */
@Service
public class AiModelServiceImpl implements AiModelService {

    @Resource
    private AiModelMapper modelMapper;

    @Resource
    private AiApiKeyService apiKeyService;

    /**
     * 获得默认的模型
     * <p>
     * 如果获取不到，则抛出 {@link com.lucky.common.exception.ServiceException}
     *
     * @param type 模型类型
     * @return 模型
     */
    @Override
    public AiModel getDefaultModelByType(Integer type) {
        AiModel model = modelMapper.selectOneByTypeAndStatus(type, CommonStatusEnum.ENABLE.getStatus());
        if (model == null) {
            throw new ServiceException(AiErrorConstants.MODEL_DEFAULT_NOT_EXISTS);
        }
        return model;
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
     * @param query 创建信息
     * @return 编号
     */
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

    /**
     * 更新模型
     *
     * @param query 更新信息
     * @return 影响行数
     */
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

    /**
     * 删除模型
     *
     * @param id 编号
     * @return 影响行数
     */
    @Override
    public int deleteModelById(Long id) {
        // 校验存在
        validateModelExists(id);
        // 删除
        return modelMapper.deleteById(id);
    }

    /**
     * 获得模型
     *
     * @param id 编号
     * @return 模型
     */
    @Override
    public AiModelVO getModelById(Long id) {
        return modelMapper.selectVoById(id);
    }

    /**
     * 获得模型分页
     *
     * @param query 分页查询
     * @return 模型分页
     */
    @Override
    public TableDataInfo<AiModelVO> getModelPage(PageQuery pageQuery, AiModelPageQuery query) {
        IPage<AiModelVO> selectPage = modelMapper.selectPage(pageQuery.build(), query);
        return TableDataInfo.build(selectPage);
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