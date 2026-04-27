package com.lucky.system.service;

import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import com.lucky.system.domain.SysDictData;
import com.lucky.system.domain.query.dict.SysDictDataQuery;
import com.lucky.system.domain.query.dict.SysDictDataSaveQuery;
import com.lucky.system.domain.vo.dict.SysDictDataVO;

import java.util.List;

/**
 * 字典 业务层
 *
 * @author ruoyi
 */
public interface ISysDictDataService {

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    SysDictDataVO selectDictDataById(Long dictCode);

    /**
     * 查询字典数据列表
     *
     * @param pageQuery 分页参数
     * @param query     字典数据查询对象
     * @return 字典数据集合
     */
    TableDataInfo<SysDictDataVO> selectDictDataList(PageQuery pageQuery, SysDictDataQuery query);

    /**
     * 查询字典数据列表
     *
     * @param query 字典数据查询对象
     * @return 字典数据集合
     */
    List<SysDictData> selectDictDataList(SysDictDataQuery query);

    /**
     * 新增保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    int insertDictData(SysDictDataSaveQuery dictData);

    /**
     * 修改保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    int updateDictData(SysDictDataSaveQuery dictData);

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    void deleteDictDataByIds(Long[] dictCodes);

}