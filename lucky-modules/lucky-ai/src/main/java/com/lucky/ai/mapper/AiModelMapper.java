package com.lucky.ai.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.domain.query.model.AiModelQuery;
import com.lucky.ai.domain.vo.model.AiModelVO;
import com.lucky.common.core.enums.DataStatus;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.mybatis.core.mapper.BaseMapperX;

import java.util.List;

/**
 * AI 模型Mapper接口
 *
 * @author lucky
 */
public interface AiModelMapper extends BaseMapperX<AiModel, AiModelVO> {

    default IPage<AiModelVO> selectPage(IPage<AiModel> page, AiModelQuery query) {
        LambdaQueryWrapper<AiModel> wrapper = Wrappers.<AiModel>lambdaQuery()
                .like(StringUtils.isNotEmpty(query.getName()), AiModel::getName, query.getName())
                .eq(StringUtils.isNotEmpty(query.getProvider()), AiModel::getProvider, query.getProvider())
                .eq(StringUtils.isNotNull(query.getStatus()), AiModel::getStatus, query.getStatus())
                .orderByAsc(AiModel::getSort);
        return selectVoPage(page, wrapper);
    }

    default List<AiModelVO> selectOptionList(Integer type) {
        LambdaQueryWrapper<AiModel> wrapper = Wrappers.<AiModel>lambdaQuery()
                .select(AiModel::getId, AiModel::getName, AiModel::getProvider, AiModel::getModel)
                .eq(AiModel::getStatus, DataStatus.OK.getCode())
                .eq(AiModel::getType, type)
                .orderByAsc(AiModel::getSort);
        return selectVoList(wrapper);
    }

}