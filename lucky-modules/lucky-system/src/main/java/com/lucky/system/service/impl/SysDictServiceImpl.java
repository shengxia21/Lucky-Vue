package com.lucky.system.service.impl;

import com.lucky.common.core.constant.CacheConstants;
import com.lucky.common.core.domain.dto.DictDataDTO;
import com.lucky.common.core.service.DictService;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.redis.utils.RedisCache;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典服务类
 *
 * @author lucky
 */
@Service
public class SysDictServiceImpl implements DictService {

    @Resource
    private RedisCache redisCache;

    /**
     * 设置字典缓存
     *
     * @param key       参数键
     * @param dictDatas 字典数据列表
     */
    @Override
    public void setDictCache(String key, List<DictDataDTO> dictDatas) {
        redisCache.setCacheObject(getCacheKey(key), dictDatas);
    }

    /**
     * 获取字典缓存
     *
     * @param key 参数键
     * @return dictDatas 字典数据列表
     */
    @Override
    public List<DictDataDTO> getDictCache(String key) {
        List<DictDataDTO> dictDataList = redisCache.getCacheObject(getCacheKey(key));
        if (StringUtils.isNotNull(dictDataList)) {
            return dictDataList;
        }
        return null;
    }

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @param separator 分隔符
     * @return 字典标签
     */
    @Override
    public String getDictLabel(String dictType, String dictValue, String separator) {
        List<DictDataDTO> datas = getDictCache(dictType);
        if (StringUtils.isNull(datas) || StringUtils.isEmpty(dictValue)) {
            return StringUtils.EMPTY;
        }
        Map<String, String> dictMap = datas.stream().collect(HashMap::new, (map, dict) -> map.put(dict.getDictValue(), dict.getDictLabel()), Map::putAll);
        if (!StringUtils.contains(dictValue, separator)) {
            return dictMap.getOrDefault(dictValue, StringUtils.EMPTY);
        }
        StringBuilder labelBuilder = new StringBuilder();
        for (String seperatedValue : dictValue.split(separator)) {
            if (dictMap.containsKey(seperatedValue)) {
                labelBuilder.append(dictMap.get(seperatedValue)).append(separator);
            }
        }
        return StringUtils.removeEnd(labelBuilder.toString(), separator);
    }

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @param separator 分隔符
     * @return 字典值
     */
    @Override
    public String getDictValue(String dictType, String dictLabel, String separator) {
        List<DictDataDTO> datas = getDictCache(dictType);
        if (StringUtils.isNull(datas) || StringUtils.isEmpty(dictLabel)) {
            return StringUtils.EMPTY;
        }
        Map<String, String> dictMap = datas.stream().collect(HashMap::new, (map, dict) -> map.put(dict.getDictLabel(), dict.getDictValue()), Map::putAll);
        if (!StringUtils.contains(dictLabel, separator)) {
            return dictMap.getOrDefault(dictLabel, StringUtils.EMPTY);
        }
        StringBuilder valueBuilder = new StringBuilder();
        for (String seperatedValue : dictLabel.split(separator)) {
            if (dictMap.containsKey(seperatedValue)) {
                valueBuilder.append(dictMap.get(seperatedValue)).append(separator);
            }
        }
        return StringUtils.removeEnd(valueBuilder.toString(), separator);
    }

    /**
     * 根据字典类型获取字典所有标签
     *
     * @param dictType 字典类型
     * @return 字典值
     */
    @Override
    public String getDictLabels(String dictType) {
        StringBuilder propertyString = new StringBuilder();
        List<DictDataDTO> datas = getDictCache(dictType);
        if (StringUtils.isNull(datas)) {
            return StringUtils.EMPTY;
        }
        for (DictDataDTO dict : datas) {
            propertyString.append(dict.getDictLabel()).append(SEPARATOR);
        }
        return StringUtils.stripEnd(propertyString.toString(), SEPARATOR);
    }

    /**
     * 根据字典类型获取字典所有值
     *
     * @param dictType 字典类型
     * @return 字典值
     */
    @Override
    public String getDictValues(String dictType) {
        StringBuilder propertyString = new StringBuilder();
        List<DictDataDTO> datas = getDictCache(dictType);
        if (StringUtils.isNull(datas)) {
            return StringUtils.EMPTY;
        }
        for (DictDataDTO dict : datas) {
            propertyString.append(dict.getDictValue()).append(SEPARATOR);
        }
        return StringUtils.stripEnd(propertyString.toString(), SEPARATOR);
    }

    /**
     * 删除指定字典缓存
     *
     * @param key 字典键
     */
    @Override
    public void removeDictCache(String key) {
        redisCache.deleteObject(getCacheKey(key));
    }

    /**
     * 清空字典缓存
     */
    @Override
    public void clearDictCache() {
        Collection<String> keys = redisCache.keys(CacheConstants.SYS_DICT_KEY + "*");
        redisCache.deleteObject(keys);
    }

    /**
     * 设置cache key
     *
     * @param cacheKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String cacheKey) {
        return CacheConstants.SYS_DICT_KEY + cacheKey;
    }

}
