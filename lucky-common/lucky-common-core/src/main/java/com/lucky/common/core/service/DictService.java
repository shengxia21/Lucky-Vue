package com.lucky.common.core.service;

import com.lucky.common.core.domain.dto.DictDataDTO;
import com.lucky.common.core.utils.StringUtils;

import java.util.List;

/**
 * 字典服务接口
 *
 * @author lucky
 */
public interface DictService {

    /**
     * 分隔符
     */
    String SEPARATOR = ",";

    /**
     * 设置字典缓存
     *
     * @param key       缓存键
     * @param dictDatas 字典数据列表
     */
    void setDictCache(String key, List<DictDataDTO> dictDatas);

    /**
     * 获取字典缓存
     *
     * @param key 缓存键
     */
    List<DictDataDTO> getDictCache(String key);

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @return 字典标签
     */
    default String getDictLabel(String dictType, String dictValue) {
        if (StringUtils.isEmpty(dictValue)) {
            return StringUtils.EMPTY;
        }
        return getDictLabel(dictType, dictValue, SEPARATOR);
    }

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @return 字典值
     */
    default String getDictValue(String dictType, String dictLabel) {
        if (StringUtils.isEmpty(dictLabel)) {
            return StringUtils.EMPTY;
        }
        return getDictValue(dictType, dictLabel, SEPARATOR);
    }

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @param separator 分隔符
     * @return 字典标签
     */
    String getDictLabel(String dictType, String dictValue, String separator);

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @param separator 分隔符
     * @return 字典值
     */
    String getDictValue(String dictType, String dictLabel, String separator);

    /**
     * 根据字典类型获取字典所有标签
     *
     * @param dictType 字典类型
     * @return 字典值
     */
    String getDictLabels(String dictType);

    /**
     * 根据字典类型获取字典所有值
     *
     * @param dictType 字典类型
     * @return 字典值
     */
    String getDictValues(String dictType);

    /**
     * 删除指定字典缓存
     *
     * @param key 字典键
     */
    void removeDictCache(String key);

    /**
     * 清空字典缓存
     */
    void clearDictCache();

}
