package com.lucky.common.security.core.dao;

import cn.dev33.satoken.dao.auto.SaTokenDaoBySessionFollowObject;
import cn.dev33.satoken.util.SaFoxUtil;
import com.lucky.common.core.utils.spring.SpringUtils;
import com.lucky.common.redis.utils.RedisCache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Sa-Token持久层实现类（使用redis实现）
 *
 * @author lucky
 */
public class SaTokenDaoPlus implements SaTokenDaoBySessionFollowObject {

    private static final RedisCache redisCache = SpringUtils.getBean(RedisCache.class);

    /**
     * 获取Value，如无返空
     */
    @Override
    public String get(String key) {
        return redisCache.getCacheObject(key);
    }

    /**
     * 写入Value，并设定存活时间 (单位: 秒)
     */
    @Override
    public void set(String key, String value, long timeout) {
        if (timeout == 0 || timeout <= NOT_VALUE_EXPIRE) {
            return;
        }
        // 判断是否为永不过期
        if (timeout == NEVER_EXPIRE) {
            // timeout == -1 通常表示永久或不设置，这里设为永久（实际业务需根据需求调整）
            redisCache.setCacheObject(key, value);
        } else {
            // Sa-Token 传入的是秒，RedisCache 通常需要指定单位，这里直接使用秒
            redisCache.setCacheObject(key, value, timeout, TimeUnit.SECONDS);
        }
    }

    /**
     * 修修改指定key-value键值对 (过期时间不变)
     */
    @Override
    public void update(String key, String value) {
        if (redisCache.hasKey(key)) {
            // Redis 的 set 操作默认会覆盖 value 但保留原有的 TTL
            redisCache.setCacheObject(key, value);
        }
    }

    /**
     * 删除Value
     */
    @Override
    public void delete(String key) {
        redisCache.deleteObject(key);
    }

    /**
     * 获取Value的剩余存活时间 (单位: 秒)
     */
    @Override
    public long getTimeout(String key) {
        long timeout = redisCache.getExpire(key);
        // 加1的目的 解决sa-token使用秒 redis是毫秒导致1秒的精度问题 手动补偿
        return timeout < 0 ? timeout : timeout / 1000 + 1;
    }

    /**
     * 修改Value的剩余存活时间 (单位: 秒)
     */
    @Override
    public void updateTimeout(String key, long timeout) {
        redisCache.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 获取Object，如无返空
     */
    @Override
    public Object getObject(String key) {
        return redisCache.getCacheObject(key);
    }

    /**
     * 获取 Object (指定反序列化类型)，如无返空
     *
     * @param key 键名称
     * @return object
     */
    @Override
    public <T> T getObject(String key, Class<T> classType) {
        return redisCache.getCacheObject(key);
    }

    /**
     * 写入Object，并设定存活时间 (单位: 秒)
     */
    @Override
    public void setObject(String key, Object object, long timeout) {
        if (timeout == 0 || timeout <= NOT_VALUE_EXPIRE) {
            return;
        }
        // 判断是否为永不过期
        if (timeout == NEVER_EXPIRE) {
            // timeout == -1 通常表示永久或不设置，这里设为永久（实际业务需根据需求调整）
            redisCache.setCacheObject(key, object);
        } else {
            // Sa-Token 传入的是秒，RedisCache 通常需要指定单位，这里直接使用秒
            redisCache.setCacheObject(key, object, timeout, TimeUnit.SECONDS);
        }
    }

    /**
     * 更新Object (过期时间不变)
     */
    @Override
    public void updateObject(String key, Object object) {
        if (redisCache.hasKey(key)) {
            redisCache.setCacheObject(key, object);
        }
    }

    /**
     * 删除Object
     */
    @Override
    public void deleteObject(String key) {
        redisCache.deleteObject(key);
    }

    /**
     * 获取Object的剩余存活时间 (单位: 秒)
     */
    @Override
    public long getObjectTimeout(String key) {
        long timeout = redisCache.getExpire(key);
        // 加1的目的 解决sa-token使用秒 redis是毫秒导致1秒的精度问题 手动补偿
        return timeout < 0 ? timeout : timeout / 1000 + 1;
    }

    /**
     * 修改Object的剩余存活时间 (单位: 秒)
     */
    @Override
    public void updateObjectTimeout(String key, long timeout) {
        redisCache.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 搜索数据
     */
    @Override
    public List<String> searchData(String prefix, String keyword, int start, int size, boolean sortType) {
        String keyStr = prefix + "*" + keyword + "*";
        Collection<String> keys = redisCache.keys(keyStr);
        List<String> list = new ArrayList<>(keys);
        return SaFoxUtil.searchList(list, start, size, sortType);
    }

}
