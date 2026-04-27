package com.lucky.system.service;

import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import com.lucky.system.domain.SysDictType;
import com.lucky.system.domain.query.dict.SysDictTypeQuery;
import com.lucky.system.domain.query.dict.SysDictTypeSaveQuery;
import com.lucky.system.domain.vo.dict.SysDictDataVO;
import com.lucky.system.domain.vo.dict.SysDictTypeVO;

import java.util.List;

/**
 * 字典 业务层
 *
 * @author ruoyi
 */
public interface ISysDictTypeService {

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    SysDictTypeVO selectDictTypeById(Long dictId);

    /**
     * 查询字典类型列表
     *
     * @param pageQuery 分页参数
     * @param query     字典类型查询对象
     * @return 字典类型集合
     */
    TableDataInfo<SysDictTypeVO> selectDictTypeList(PageQuery pageQuery, SysDictTypeQuery query);

    /**
     * 查询字典类型列表
     *
     * @param query 字典类型查询对象
     * @return 字典类型集合
     */
    List<SysDictType> selectDictTypeList(SysDictTypeQuery query);

    /**
     * 查询字典类型所有数据
     *
     * @return 字典类型集合信息
     */
    List<SysDictTypeVO> selectDictTypeAll();

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    List<SysDictDataVO> selectDictDataByType(String dictType);

    /**
     * 新增保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    int insertDictType(SysDictTypeSaveQuery dictType);

    /**
     * 修改保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    int updateDictType(SysDictTypeSaveQuery dictType);

    /**
     * 批量删除字典类型信息
     *
     * @param dictIds 需要删除的字典ID
     */
    void deleteDictTypeByIds(Long[] dictIds);

    /**
     * 加载字典缓存数据
     */
    void loadingDictCache();

    /**
     * 清空字典缓存数据
     */
    void clearDictCache();

    /**
     * 重置字典缓存数据
     */
    void resetDictCache();

    /**
     * 校验字典类型称是否唯一
     *
     * @param dictType 字典类型
     * @return 结果
     */
    boolean checkDictTypeUnique(Long dictId, String dictType);

}