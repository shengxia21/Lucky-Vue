package com.lucky.system.utils;

import com.lucky.common.core.domain.dto.DictDataDTO;
import com.lucky.system.domain.SysDictData;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典数据转换工具类
 *
 * @author lucky
 */
public class DictDataConvertUtils {

    /**
     * 转换字典数据DTO为字典数据
     *
     * @param dictDatas 字典数据DTO列表
     * @return 字典数据列表
     */
    public static List<SysDictData> convertSys(List<DictDataDTO> dictDatas) {
        List<SysDictData> dictDataList = new ArrayList<>();
        for (DictDataDTO dto : dictDatas) {
            SysDictData dictData = new SysDictData();
            dictData.setDictCode(dto.getDictCode());
            dictData.setDictSort(dto.getDictSort());
            dictData.setDictLabel(dto.getDictLabel());
            dictData.setDictValue(dto.getDictValue());
            dictData.setDictType(dto.getDictType());
            dictData.setCssClass(dto.getCssClass());
            dictData.setListClass(dto.getListClass());
            dictData.setIsDefault(dto.getIsDefault());
            dictData.setStatus(dto.getStatus());
            dictData.setRemark(dto.getRemark());
            dictDataList.add(dictData);
        }
        return dictDataList;
    }

    /**
     * 转换字典数据为DTO
     *
     * @param dictDatas 字典数据列表
     * @return 字典数据DTO列表
     */
    public static List<DictDataDTO> convertDto(List<SysDictData> dictDatas) {
        List<DictDataDTO> dictDataList = new ArrayList<>();
        for (SysDictData dictData : dictDatas) {
            DictDataDTO dto = new DictDataDTO();
            dto.setDictCode(dictData.getDictCode());
            dto.setDictSort(dictData.getDictSort());
            dto.setDictLabel(dictData.getDictLabel());
            dto.setDictValue(dictData.getDictValue());
            dto.setDictType(dictData.getDictType());
            dto.setCssClass(dictData.getCssClass());
            dto.setListClass(dictData.getListClass());
            dto.setIsDefault(dictData.getIsDefault());
            dto.setStatus(dictData.getStatus());
            dto.setRemark(dictData.getRemark());
            dictDataList.add(dto);
        }
        return dictDataList;
    }

}
