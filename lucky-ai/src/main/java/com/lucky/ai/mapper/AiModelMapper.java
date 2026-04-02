package com.lucky.ai.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.ai.domain.AiModel;
import com.lucky.ai.domain.query.model.AiModelPageQuery;
import com.lucky.ai.domain.vo.model.AiModelVO;
import com.lucky.common.core.mybatis.BaseMapperX;
import com.lucky.common.utils.StringUtils;

import java.util.List;

/**
 * AI 模型Mapper接口
 *
 * @author lucky
 */
public interface AiModelMapper extends BaseMapperX<AiModel, AiModelVO> {

    default IPage<AiModelVO> selectPage(IPage<AiModel> page, AiModelPageQuery query) {
        LambdaQueryWrapper<AiModel> wrapper = Wrappers.<AiModel>lambdaQuery()
                .like(StringUtils.isNotEmpty(query.getName()), AiModel::getName, query.getName())
                .eq(StringUtils.isNotEmpty(query.getModel()), AiModel::getModel, query.getModel())
                .eq(StringUtils.isNotEmpty(query.getPlatform()), AiModel::getPlatform, query.getPlatform())
                .orderByAsc(AiModel::getSort);
        return selectVoPage(page, wrapper);
    }

    default AiModel selectOneByTypeAndStatus(Integer type, Integer status) {
        LambdaQueryWrapper<AiModel> wrapper = Wrappers.<AiModel>lambdaQuery()
                .eq(AiModel::getType, type)
                .eq(AiModel::getStatus, status)
                .orderByAsc(AiModel::getSort);
        return selectOne(wrapper, false);
    }

    default List<AiModelVO> selectList(Integer status, Integer type, String platform) {
        LambdaQueryWrapper<AiModel> wrapper = Wrappers.<AiModel>lambdaQuery()
                .select(AiModel::getId, AiModel::getName, AiModel::getModel, AiModel::getPlatform)
                .eq(StringUtils.isNotNull(status), AiModel::getStatus, status)
                .eq(StringUtils.isNotNull(type), AiModel::getType, type)
                .eq(StringUtils.isNotEmpty(platform), AiModel::getPlatform, platform)
                .orderByAsc(AiModel::getSort);
        return selectVoList(wrapper);
    }

}