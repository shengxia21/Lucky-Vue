package com.lucky.generator.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.common.mybatis.core.mapper.BaseMapperX;
import com.lucky.generator.domain.GenTableColumn;
import com.lucky.generator.domain.vo.GenTableColumnVO;

import java.util.List;

/**
 * 业务字段 数据层
 *
 * @author lucky
 */
public interface GenTableColumnMapper extends BaseMapperX<GenTableColumn, GenTableColumnVO> {

    /**
     * 根据表名称查询列信息
     *
     * @param tableName 表名称
     * @return 列信息
     */
    List<GenTableColumn> selectDbByName(String tableName);

    default List<GenTableColumnVO> selectListByTableId(Long tableId) {
        return selectVoList(Wrappers.<GenTableColumn>lambdaQuery().eq(GenTableColumn::getTableId, tableId));
    }

    default int deleteByTableIds(List<Long> ids) {
        return delete(Wrappers.<GenTableColumn>lambdaQuery().in(GenTableColumn::getTableId, ids));
    }

}
