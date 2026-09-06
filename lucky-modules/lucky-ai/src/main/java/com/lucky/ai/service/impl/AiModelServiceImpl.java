package com.lucky.ai.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.domain.query.model.AiModelQuery;
import com.lucky.ai.domain.query.model.AiModelSaveQuery;
import com.lucky.ai.domain.vo.model.AiModelVO;
import com.lucky.ai.enums.ModelType;
import com.lucky.ai.mapper.AiModelMapper;
import com.lucky.ai.service.IAiApiKeyService;
import com.lucky.ai.service.IAiModelService;
import com.lucky.common.core.constant.AiConstants;
import com.lucky.common.core.enums.DataStatus;
import com.lucky.common.core.exception.ServiceException;
import com.lucky.common.core.utils.MapstructUtils;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
    public TableDataInfo<AiModelVO> selectModelList(PageQuery pageQuery, AiModelQuery query) {
        IPage<AiModelVO> selectPage = modelMapper.selectPage(pageQuery.build(), query);
        return TableDataInfo.build(selectPage);
    }

    @Override
    public AiModelVO selectModelById(Long id) {
        return modelMapper.selectVoById(id);
    }

    @Override
    public int insertModel(AiModelSaveQuery query) {
        // 1. 校验 api-key 是否有效
        apiKeyService.validateApiKey(query.getKeyId());
        // 2. 插入
        AiModel model = MapstructUtils.convert(query, AiModel.class);
        // 3. 规整对话能力标志
        normalizeChatAbility(model);
        return modelMapper.insert(model);
    }

    @Override
    public int updateModel(AiModelSaveQuery query) {
        // 1. 校验 api-key 是否有效
        apiKeyService.validateApiKey(query.getKeyId());
        // 2. 更新
        AiModel model = MapstructUtils.convert(query, AiModel.class);
        // 3. 规整对话能力标志
        normalizeChatAbility(model);
        return modelMapper.updateById(model);
    }

    @Override
    public int deleteModelByIds(Long[] ids) {
        return modelMapper.deleteByIds(Arrays.asList(ids));
    }

    @Override
    public List<AiModelVO> selectModelAll(String type) {
        return modelMapper.selectOptionList(type);
    }

    @Override
    public AiModel validateModel(Long id) {
        AiModel model = validateModelExists(id);
        if (DataStatus.DISABLE.getCode().equals(model.getStatus())) {
            throw new ServiceException(StringUtils.format(AiConstants.MODEL_DISABLE, model.getName()));
        }
        return model;
    }

    /**
     * 规整对话能力标志：仅对话模型可配置联网搜索与多模态，其它类型强制置空（落库为 NULL）
     */
    private void normalizeChatAbility(AiModel model) {
        if (!ModelType.CHAT.getCode().equals(model.getType())) {
            model.setEnableSearch(null);
            model.setEnableMultimodal(null);
        }
    }

    private AiModel validateModelExists(Long id) {
        AiModel model = modelMapper.selectById(id);
        if (model == null) {
            throw new ServiceException(AiConstants.MODEL_NOT_EXISTS);
        }
        return model;
    }

}