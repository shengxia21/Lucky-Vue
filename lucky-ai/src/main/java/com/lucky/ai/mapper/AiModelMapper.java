package com.lucky.ai.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.domain.query.model.ModelPageQuery;
import com.lucky.common.utils.StringUtils;

import java.util.List;

/**
 * AI 模型Mapper接口
 *
 * @author lucky
 */
public interface AiModelMapper extends BaseMapper<AiModel> {

    default IPage<AiModel> selectPage(IPage<AiModel> page, ModelPageQuery query) {
        LambdaQueryWrapper<AiModel> wrapper = Wrappers.<AiModel>lambdaQuery()
                .like(StringUtils.isNotEmpty(query.getName()), AiModel::getName, query.getName())
                .eq(StringUtils.isNotEmpty(query.getModel()), AiModel::getModel, query.getModel())
                .eq(StringUtils.isNotEmpty(query.getPlatform()), AiModel::getPlatform, query.getPlatform())
                .orderByAsc(AiModel::getSort);
        return selectPage(page, wrapper);
    }

    default AiModel selectFirstByStatus(Integer type, Integer status) {
        LambdaQueryWrapper<AiModel> wrapper = Wrappers.<AiModel>lambdaQuery()
                .eq(AiModel::getType, type)
                .eq(AiModel::getStatus, status)
                .orderByAsc(AiModel::getSort);
        return selectOne(wrapper, false);
    }

    default List<AiModel> selectListByStatusAndType(Integer status, Integer type, String platform) {
        LambdaQueryWrapper<AiModel> wrapper = Wrappers.<AiModel>lambdaQuery()
                .eq(StringUtils.isNotNull(status), AiModel::getStatus, status)
                .eq(StringUtils.isNotNull(type), AiModel::getType, type)
                .eq(StringUtils.isNotEmpty(platform), AiModel::getPlatform, platform)
                .orderByAsc(AiModel::getSort);
        return selectList(wrapper);
    }

}