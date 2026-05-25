package com.lucky.common.redis.utils;

import jakarta.annotation.Resource;
import org.redisson.api.*;
import org.redisson.api.options.KeysScanOptions;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * spring redis 工具类
 *
 * @author lucky
 **/
@SuppressWarnings(value = {"unchecked", "rawtypes"})
@Component
public class RedisCache {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param duration 时间
     */
    public <T> void setCacheObject(final String key, final T value, final Duration duration) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        bucket.set(value, duration);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout) {
        return expire(key, Duration.ofSeconds(timeout));
    }

    /**
     * 设置有效时间
     *
     * @param key      Redis键
     * @param duration 时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final Duration duration) {
        RBucket<Object> rBucket = redissonClient.getBucket(key);
        return rBucket.expire(duration);
    }

    /**
     * 获取有效时间
     *
     * @param key Redis键
     * @return 有效时间
     */
    public long getExpire(final String key) {
        RBucket<Object> rBucket = redissonClient.getBucket(key);
        return rBucket.getExpireTime();
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key) {
        RBucket<T> rBucket = redissonClient.getBucket(key);
        return rBucket.get();
    }

    /**
     * 删除单个对象
     *
     * @param key 缓存键值
     * @return true=删除成功；false=删除失败
     */
    public boolean deleteObject(final String key) {
        return redissonClient.getBucket(key).delete();
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     */
    public void deleteObject(final Collection collection) {
        RBatch batch = redissonClient.createBatch();
        collection.forEach(t -> {
            batch.getBucket(t.toString()).deleteAsync();
        });
        batch.execute();
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> boolean setCacheList(final String key, final List<T> dataList) {
        RList<T> rList = redissonClient.getList(key);
        return rList.addAll(dataList);
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key) {
        RList<T> rList = redissonClient.getList(key);
        return rList.readAll();
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> boolean setCacheSet(final String key, final Set<T> dataSet) {
        RSet<T> rSet = redissonClient.getSet(key);
        return rSet.addAll(dataSet);
    }

    /**
     * 获得缓存的set
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> Set<T> getCacheSet(final String key) {
        RSet<T> rSet = redissonClient.getSet(key);
        return rSet.readAll();
    }

    /**
     * 缓存Map
     *
     * @param key     缓存键值
     * @param dataMap 缓存的数据Map
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            RMap<String, T> rMap = redissonClient.getMap(key);
            rMap.putAll(dataMap);
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> Map<String, T> getCacheMap(final String key) {
        RMap<String, T> rMap = redissonClient.getMap(key);
        return rMap.getAll(rMap.keySet());
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        RMap<String, T> rMap = redissonClient.getMap(key);
        rMap.put(hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(final String key, final String hKey) {
        RMap<String, T> rMap = redissonClient.getMap(key);
        return rMap.get(hKey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <K, V> Map<K, V> getMultiCacheMapValue(final String key, final Set<K> hKeys) {
        RMap<K, V> rMap = redissonClient.getMap(key);
        return rMap.getAll(hKeys);
    }

    /**
     * 删除Hash中的某条数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     */
    public <T> void deleteCacheMapValue(final String key, final Set<String> hKeys) {
        RBatch batch = redissonClient.createBatch();
        RMapAsync<String, T> rMap = batch.getMap(key);
        for (String hKey : hKeys) {
            rMap.removeAsync(hKey);
        }
        batch.execute();
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key) {
        RKeys rKeys = redissonClient.getKeys();
        return rKeys.countExists(key) > 0;
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern) {
        return keys(KeysScanOptions.defaults().pattern(pattern).chunkSize(1000));
    }

    /**
     * 通过扫描参数获取缓存的基本对象列表
     * <p>
     * limit-设置扫描的限制数量(默认为0,查询全部)
     * pattern-设置键的匹配模式(默认为null)
     * chunkSize-设置每次扫描的块大小(默认为0)
     * type-设置键的类型(默认为null,查询全部类型)
     *
     * @param keysScanOptions 扫描参数
     */
    public Collection<String> keys(final KeysScanOptions keysScanOptions) {
        Stream<String> keysStream = redissonClient.getKeys().getKeysStream(keysScanOptions);
        return keysStream.collect(Collectors.toList());
    }

}
