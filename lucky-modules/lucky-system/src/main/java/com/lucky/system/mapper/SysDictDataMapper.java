package com.lucky.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.mybatis.core.mapper.BaseMapperX;
import com.lucky.system.domain.SysDictData;
import com.lucky.system.domain.query.dict.SysDictDataQuery;
import com.lucky.system.domain.vo.dict.SysDictDataVO;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author lucky
 */
public interface SysDictDataMapper extends BaseMapperX<SysDictData, SysDictDataVO> {

    default IPage<SysDictDataVO> selectPage(Page<SysDictData> page, SysDictDataQuery query) {
        return selectVoPage(page, buildWrapper(query));
    }

    default List<SysDictData> selectList(SysDictDataQuery query) {
        return selectList(buildWrapper(query));
    }

    default Wrapper<SysDictData> buildWrapper(SysDictDataQuery query) {
        return Wrappers.<SysDictData>lambdaQuery()
                .like(StringUtils.isNotBlank(query.getDictLabel()), SysDictData::getDictLabel, query.getDictLabel())
                .eq(StringUtils.isNotBlank(query.getDictType()), SysDictData::getDictType, query.getDictType())
                .eq(StringUtils.isNotBlank(query.getStatus()), SysDictData::getStatus, query.getStatus())
                .orderByAsc(SysDictData::getDictSort);
    }

    default List<SysDictData> selectListByType(String dictType) {
        Wrapper<SysDictData> wrapper = Wrappers.<SysDictData>lambdaQuery()
                .eq(SysDictData::getStatus, "0")
                .eq(SysDictData::getDictType, dictType)
                .orderByAsc(SysDictData::getDictSort);
        return selectList(wrapper);
    }

    default int updateByDictType(String oldDictType, String newDictType) {
        return update(Wrappers.<SysDictData>lambdaUpdate()
                .set(SysDictData::getDictType, newDictType)
                .eq(SysDictData::getDictType, oldDictType));
    }

    default Long countByType(String dictType) {
        return selectCount(Wrappers.<SysDictData>lambdaQuery()
                .eq(SysDictData::getDictType, dictType));
    }

}