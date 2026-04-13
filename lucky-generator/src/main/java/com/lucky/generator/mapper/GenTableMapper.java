package com.lucky.generator.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.common.core.mybatis.BaseMapperX;
import com.lucky.common.utils.StringUtils;
import com.lucky.generator.domain.GenTable;
import com.lucky.generator.domain.query.GenTableQuery;
import com.lucky.generator.domain.vo.GenTableVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务 数据层
 *
 * @author lucky
 */
public interface GenTableMapper extends BaseMapperX<GenTable, GenTableVO> {

    /**
     * 查询据库列表
     *
     * @param page  分页参数
     * @param query 查询参数
     * @return 数据库表集合
     */
    IPage<GenTableVO> selectDbTableList(IPage<GenTable> page, @Param("query") GenTableQuery query);

    /**
     * 查询据库列表
     *
     * @param tableNames 表名称组
     * @return 数据库表集合
     */
    List<GenTable> selectDbTableListByNames(String[] tableNames);

    /**
     * 查询所有表信息
     *
     * @return 表信息集合
     */
    List<GenTable> selectGenTableAll();

    /**
     * 查询表ID业务信息
     *
     * @param id 业务ID
     * @return 业务信息
     */
    GenTable selectGenTableById(Long id);

    /**
     * 查询表名称业务信息
     *
     * @param tableName 表名称
     * @return 业务信息
     */
    GenTable selectGenTableByName(String tableName);

    default IPage<GenTableVO> selectList(IPage<GenTable> page, GenTableQuery query) {
        LambdaQueryWrapper<GenTable> wrapper = Wrappers.<GenTable>lambdaQuery()
                .like(StringUtils.isNotEmpty(query.getTableName()), GenTable::getTableName, query.getTableName())
                .like(StringUtils.isNotEmpty(query.getTableComment()), GenTable::getTableComment, query.getTableComment())
                .between(!query.getParams().isEmpty(), GenTable::getCreateTime, query.getParams().get("beginTime"), query.getParams().get("endTime"));
        return selectVoPage(page, wrapper);
    }

    default int deleteByTableIds(List<Long> ids) {
        return delete(Wrappers.<GenTable>lambdaQuery().in(GenTable::getTableId, ids));
    }

}
