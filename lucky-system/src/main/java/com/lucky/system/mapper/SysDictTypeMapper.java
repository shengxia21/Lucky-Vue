package com.lucky.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.common.core.domain.entity.SysDictType;
import com.lucky.common.core.mybatis.BaseMapperX;
import com.lucky.common.utils.StringUtils;
import com.lucky.system.domain.query.dict.SysDictTypeQuery;
import com.lucky.system.domain.vo.dict.SysDictTypeVO;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author lucky
 */
public interface SysDictTypeMapper extends BaseMapperX<SysDictType, SysDictTypeVO> {

    default IPage<SysDictTypeVO> selectPage(Page<SysDictType> page, SysDictTypeQuery query) {
        return selectVoPage(page, buildWrapper(query));
    }

    default List<SysDictType> selectList(SysDictTypeQuery query) {
        return selectList(buildWrapper(query));
    }

    default Wrapper<SysDictType> buildWrapper(SysDictTypeQuery query) {
        return Wrappers.<SysDictType>lambdaQuery()
                .like(StringUtils.isNotBlank(query.getDictName()), SysDictType::getDictName, query.getDictName())
                .like(StringUtils.isNotBlank(query.getDictType()), SysDictType::getDictType, query.getDictType())
                .eq(StringUtils.isNotBlank(query.getStatus()), SysDictType::getStatus, query.getStatus())
                .between(!query.getParams().isEmpty(), SysDictType::getCreateTime, query.getParams().get("beginTime"), query.getParams().get("endTime"))
                .orderByDesc(SysDictType::getCreateTime);
    }

    default SysDictTypeVO checkDictTypeUnique(String dictType) {
        return selectVoOne(Wrappers.<SysDictType>lambdaQuery().eq(SysDictType::getDictType, dictType));
    }

}