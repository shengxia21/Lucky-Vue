package com.lucky.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.common.core.constant.UserConstants;
import com.lucky.common.core.domain.dto.DictDataDTO;
import com.lucky.common.core.exception.ServiceException;
import com.lucky.common.core.utils.MapstructUtils;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import com.lucky.common.redis.utils.DictUtils;
import com.lucky.system.domain.SysDictData;
import com.lucky.system.domain.SysDictType;
import com.lucky.system.domain.query.dict.SysDictDataQuery;
import com.lucky.system.domain.query.dict.SysDictTypeQuery;
import com.lucky.system.domain.query.dict.SysDictTypeSaveQuery;
import com.lucky.system.domain.vo.dict.SysDictDataVO;
import com.lucky.system.domain.vo.dict.SysDictTypeVO;
import com.lucky.system.mapper.SysDictDataMapper;
import com.lucky.system.mapper.SysDictTypeMapper;
import com.lucky.system.service.ISysDictTypeService;
import com.lucky.system.utils.DictDataConvertUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典 业务层处理
 *
 * @author lucky
 */
@Service
public class SysDictTypeServiceImpl implements ISysDictTypeService {

    @Resource
    private SysDictTypeMapper dictTypeMapper;

    @Resource
    private SysDictDataMapper dictDataMapper;

    /**
     * 项目启动时，初始化字典到缓存
     */
    @PostConstruct
    public void init() {
        loadingDictCache();
    }

    @Override
    public SysDictTypeVO selectDictTypeById(Long dictId) {
        return dictTypeMapper.selectVoById(dictId);
    }

    @Override
    public TableDataInfo<SysDictTypeVO> selectDictTypeList(PageQuery pageQuery, SysDictTypeQuery query) {
        IPage<SysDictTypeVO> page = dictTypeMapper.selectPage(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    @Override
    public List<SysDictType> selectDictTypeList(SysDictTypeQuery query) {
        return dictTypeMapper.selectList(query);
    }

    @Override
    public List<SysDictTypeVO> selectDictTypeAll() {
        return dictTypeMapper.selectVoList();
    }

    @Override
    public List<SysDictDataVO> selectDictDataByType(String dictType) {
        List<DictDataDTO> dictDatas = DictUtils.getDictCache(dictType);
        if (StringUtils.isNotEmpty(dictDatas)) {
            List<SysDictData> convertList = DictDataConvertUtils.convertSys(dictDatas);
            return MapstructUtils.convert(convertList, SysDictDataVO.class);
        }
        List<SysDictData> sysDictData = dictDataMapper.selectListByType(dictType);
        if (StringUtils.isNotEmpty(sysDictData)) {
            List<DictDataDTO> convertList = DictDataConvertUtils.convertDto(sysDictData);
            DictUtils.setDictCache(dictType, convertList);
            return MapstructUtils.convert(sysDictData, SysDictDataVO.class);
        }
        return null;
    }

    @Override
    public void deleteDictTypeByIds(Long[] dictIds) {
        for (Long dictId : dictIds) {
            SysDictType dictType = dictTypeMapper.selectById(dictId);
            if (dictDataMapper.countByType(dictType.getDictType()) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
            }
            dictTypeMapper.deleteById(dictId);
            DictUtils.removeDictCache(dictType.getDictType());
        }
    }

    @Override
    public void loadingDictCache() {
        SysDictDataQuery query = new SysDictDataQuery();
        query.setStatus("0");
        Map<String, List<SysDictData>> dictDataMap = dictDataMapper.selectList(query).stream().collect(Collectors.groupingBy(SysDictData::getDictType));
        for (Map.Entry<String, List<SysDictData>> entry : dictDataMap.entrySet()) {
            List<DictDataDTO> dtoList = DictDataConvertUtils.convertDto(entry.getValue());
            DictUtils.setDictCache(entry.getKey(), dtoList.stream().sorted(Comparator.comparing(DictDataDTO::getDictSort)).toList());
        }
    }

    @Override
    public void clearDictCache() {
        DictUtils.clearDictCache();
    }

    @Override
    public void resetDictCache() {
        clearDictCache();
        loadingDictCache();
    }

    @Override
    public int insertDictType(SysDictTypeSaveQuery dictType) {
        SysDictType sysDictType = MapstructUtils.convert(dictType, SysDictType.class);
        int row = dictTypeMapper.insert(sysDictType);
        if (row > 0) {
            DictUtils.setDictCache(dictType.getDictType(), null);
        }
        return row;
    }

    @Override
    @Transactional
    public int updateDictType(SysDictTypeSaveQuery dictType) {
        SysDictType oldDict = dictTypeMapper.selectById(dictType.getDictId());
        dictDataMapper.updateByDictType(oldDict.getDictType(), dictType.getDictType());
        SysDictType sysDictType = MapstructUtils.convert(dictType, SysDictType.class);
        int row = dictTypeMapper.updateById(sysDictType);
        if (row > 0) {
            List<SysDictData> dictDatas = dictDataMapper.selectListByType(dictType.getDictType());
            List<DictDataDTO> convertList = DictDataConvertUtils.convertDto(dictDatas);
            DictUtils.setDictCache(dictType.getDictType(), convertList);
        }
        return row;
    }

    @Override
    public boolean checkDictTypeUnique(Long dictId, String dictType) {
        long newDictId = StringUtils.isNull(dictId) ? -1L : dictId;
        SysDictTypeVO sysDictType = dictTypeMapper.checkDictTypeUnique(dictType);
        if (StringUtils.isNotNull(sysDictType) && sysDictType.getDictId() != newDictId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

}