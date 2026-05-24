package com.lucky.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.common.core.domain.dto.DictDataDTO;
import com.lucky.common.core.service.DictService;
import com.lucky.common.core.utils.MapstructUtils;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import com.lucky.system.domain.SysDictData;
import com.lucky.system.domain.query.dict.SysDictDataQuery;
import com.lucky.system.domain.query.dict.SysDictDataSaveQuery;
import com.lucky.system.domain.vo.dict.SysDictDataVO;
import com.lucky.system.mapper.SysDictDataMapper;
import com.lucky.system.service.ISysDictDataService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典 业务层处理
 *
 * @author lucky
 */
@Service
public class SysDictDataServiceImpl implements ISysDictDataService {

    @Resource
    private SysDictDataMapper dictDataMapper;

    @Resource
    private DictService dictService;

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public SysDictDataVO selectDictDataById(Long dictCode) {
        return dictDataMapper.selectVoById(dictCode);
    }

    /**
     * 查询字典数据列表
     *
     * @param pageQuery 分页参数
     * @param query     字典数据查询对象
     * @return 字典数据集合
     */
    @Override
    public TableDataInfo<SysDictDataVO> selectDictDataList(PageQuery pageQuery, SysDictDataQuery query) {
        IPage<SysDictDataVO> page = dictDataMapper.selectPage(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    /**
     * 查询字典数据列表
     *
     * @param query 字典数据查询对象
     * @return 字典数据集合
     */
    @Override
    public List<SysDictData> selectDictDataList(SysDictDataQuery query) {
        return dictDataMapper.selectList(query);
    }

    /**
     * 新增保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    @Override
    public int insertDictData(SysDictDataSaveQuery dictData) {
        SysDictData sysDictData = MapstructUtils.convert(dictData, SysDictData.class);
        int row = dictDataMapper.insert(sysDictData);
        if (row > 0) {
            List<SysDictData> dictDatas = dictDataMapper.selectListByType(dictData.getDictType());
            List<DictDataDTO> convertList = BeanUtil.copyToList(dictDatas, DictDataDTO.class);
            dictService.setDictCache(dictData.getDictType(), convertList);
        }
        return row;
    }

    /**
     * 修改保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    @Override
    public int updateDictData(SysDictDataSaveQuery dictData) {
        SysDictData sysDictData = MapstructUtils.convert(dictData, SysDictData.class);
        int row = dictDataMapper.updateById(sysDictData);
        if (row > 0) {
            List<SysDictData> dictDatas = dictDataMapper.selectListByType(dictData.getDictType());
            List<DictDataDTO> convertList = BeanUtil.copyToList(dictDatas, DictDataDTO.class);
            dictService.setDictCache(dictData.getDictType(), convertList);
        }
        return row;
    }

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    @Override
    @Transactional
    public void deleteDictDataByIds(Long[] dictCodes) {
        for (Long dictCode : dictCodes) {
            SysDictData data = dictDataMapper.selectById(dictCode);
            dictDataMapper.deleteById(dictCode);
            List<SysDictData> dictDatas = dictDataMapper.selectListByType(data.getDictType());
            List<DictDataDTO> convertList = BeanUtil.copyToList(dictDatas, DictDataDTO.class);
            dictService.setDictCache(data.getDictType(), convertList);
        }
    }

}