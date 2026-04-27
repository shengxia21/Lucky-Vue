package com.lucky.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.mybatis.core.mapper.BaseMapperX;
import com.lucky.system.domain.SysConfig;
import com.lucky.system.domain.query.config.SysConfigQuery;
import com.lucky.system.domain.vo.config.SysConfigVO;

import java.util.List;

/**
 * 参数配置 数据层
 *
 * @author lucky
 */
public interface SysConfigMapper extends BaseMapperX<SysConfig, SysConfigVO> {

    default IPage<SysConfigVO> selectPage(Page<SysConfig> page, SysConfigQuery query) {
        return selectVoPage(page, buildWrapper(query));
    }

    default List<SysConfig> selectList(SysConfigQuery query) {
        return selectList(buildWrapper(query));
    }

    default Wrapper<SysConfig> buildWrapper(SysConfigQuery query) {
        return Wrappers.<SysConfig>lambdaQuery()
                .like(StringUtils.isNotBlank(query.getConfigName()), SysConfig::getConfigName, query.getConfigName())
                .like(StringUtils.isNotBlank(query.getConfigKey()), SysConfig::getConfigKey, query.getConfigKey())
                .eq(StringUtils.isNotBlank(query.getConfigType()), SysConfig::getConfigType, query.getConfigType())
                .between(!query.getParams().isEmpty(), SysConfig::getCreateTime, query.getParams().get("beginTime"), query.getParams().get("endTime"))
                .orderByDesc(SysConfig::getCreateTime);
    }

    default SysConfigVO selectOneByConfigKey(String configKey) {
        return selectVoOne(Wrappers.<SysConfig>lambdaQuery().eq(SysConfig::getConfigKey, configKey), false);
    }

}