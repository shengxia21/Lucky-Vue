package com.lucky.ai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.ai.controller.model.vo.model.AiModelPageReqVO;
import com.lucky.ai.controller.model.vo.model.AiModelRespVO;
import com.lucky.ai.controller.model.vo.model.AiModelSaveReqVO;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.enums.CommonStatusEnum;
import com.lucky.ai.enums.model.AiPlatformEnum;
import com.lucky.ai.mapper.AiModelMapper;
import com.lucky.ai.service.AiApiKeyService;
import com.lucky.ai.service.AiModelService;
import com.lucky.common.constant.AiErrorConstants;
import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.common.exception.ServiceException;
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
    public AiModel getRequiredDefaultModel(Integer type) {
        AiModel aiModel = modelMapper.selectFirstByStatus(type, CommonStatusEnum.ENABLE.getStatus());
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
        apiKeyService.validateApiKey(createReqVO.getKeyId());
        // 2. 插入
        AiModel model = BeanUtil.toBean(createReqVO, AiModel.class);
        modelMapper.insert(model);
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
        apiKeyService.validateApiKey(updateReqVO.getKeyId());
        // 2. 更新
        AiModel updateObj = BeanUtil.toBean(updateReqVO, AiModel.class);
        return modelMapper.updateById(updateObj);
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
    public AiModel getModelById(Long id) {
        return modelMapper.selectById(id);
    }

    /**
     * 获得模型分页
     *
     * @param pageReqVO 分页查询
     * @return 模型分页
     */
    @Override
    public TableDataInfo<AiModelRespVO> getModelPage(PageQuery pageQuery, AiModelPageReqVO pageReqVO) {
        IPage<AiModel> selectPage = modelMapper.selectPage(pageQuery.build(), pageReqVO);
        return TableDataInfo.build(selectPage, AiModelRespVO.class);
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
        return modelMapper.selectListByStatusAndType(status, type, platform);
    }

    private AiModel validateModelExists(Long id) {
        AiModel model = modelMapper.selectById(id);
        if (model == null) {
            throw new ServiceException(AiErrorConstants.MODEL_NOT_EXISTS);
        }
        return model;
    }

}