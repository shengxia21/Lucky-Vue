package com.lucky.system.controller.monitor;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.lucky.common.core.constant.CacheConstants;
import com.lucky.common.core.domain.AjaxResult;
import com.lucky.common.core.domain.R;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.system.domain.vo.cache.SysCacheVo;
import jakarta.annotation.Resource;
import org.springframework.data.redis.connection.DefaultedRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 缓存监控
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/cache")
public class CacheController {

    private final static List<SysCacheVo> caches = new ArrayList<>();

    @Resource(name = "stringRedisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    {
        caches.add(new SysCacheVo(CacheConstants.LOGIN_TOKEN_KEY, "用户信息"));
        caches.add(new SysCacheVo(CacheConstants.SYS_CONFIG_KEY, "配置信息"));
        caches.add(new SysCacheVo(CacheConstants.SYS_DICT_KEY, "数据字典"));
        caches.add(new SysCacheVo(CacheConstants.CAPTCHA_CODE_KEY, "验证码"));
        caches.add(new SysCacheVo(CacheConstants.REPEAT_SUBMIT_KEY, "防重提交"));
        caches.add(new SysCacheVo(CacheConstants.RATE_LIMIT_KEY, "限流处理"));
        caches.add(new SysCacheVo(CacheConstants.PWD_ERR_CNT_KEY, "密码错误次数"));
    }

    /**
     * 获取缓存监控信息
     */
    @SuppressWarnings("deprecation")
    @SaCheckPermission("monitor:cache:list")
    @GetMapping()
    public AjaxResult getInfo() {
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) DefaultedRedisConnection::info);
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) DefaultedRedisConnection::dbSize);

        Map<String, Object> result = new HashMap<>(3);
        result.put("info", info);
        result.put("dbSize", dbSize);

        List<Map<String, String>> pieList = new ArrayList<>();
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", StringUtils.removeStart(key, "cmdstat_"));
            data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
            pieList.add(data);
        });
        result.put("commandStats", pieList);
        return AjaxResult.success(result);
    }

    /**
     * 获取缓存名称
     */
    @SaCheckPermission("monitor:cache:list")
    @GetMapping("/getNames")
    public R<List<SysCacheVo>> cache() {
        return R.ok(caches);
    }

    /**
     * 获取缓存键名
     */
    @SaCheckPermission("monitor:cache:list")
    @GetMapping("/getKeys/{cacheName}")
    public R<Set<String>> getCacheKeys(@PathVariable String cacheName) {
        Set<String> cacheKeys = redisTemplate.keys(cacheName + "*");
        if (cacheName.equals(CacheConstants.LOGIN_TOKEN_KEY)) {
            cacheKeys = cacheKeys.stream().filter(key -> {
                String token = StringUtils.substringAfterLast(key, ":");
                return !(StpUtil.stpLogic.getTokenActiveTimeoutByToken(token) < -1);
            }).collect(Collectors.toSet());
        }
        return R.ok(new TreeSet<>(cacheKeys));
    }

    /**
     * 获取缓存值
     */
    @SaCheckPermission("monitor:cache:list")
    @GetMapping("/getValue/{cacheName}/{cacheKey}")
    public R<SysCacheVo> getCacheValue(@PathVariable String cacheName, @PathVariable String cacheKey) {
        String cacheValue = redisTemplate.opsForValue().get(cacheKey);
        SysCacheVo sysCacheVo = new SysCacheVo(cacheName, cacheKey, cacheValue);
        return R.ok(sysCacheVo);
    }

    /**
     * 清除缓存
     */
    @SaCheckPermission("monitor:cache:list")
    @DeleteMapping("/clearCacheName/{cacheName}")
    public R<Void> clearCacheName(@PathVariable String cacheName) {
        Collection<String> cacheKeys = redisTemplate.keys(cacheName + "*");
        redisTemplate.delete(cacheKeys);
        return R.ok();
    }

    /**
     * 清除缓存cacheKey
     */
    @SaCheckPermission("monitor:cache:list")
    @DeleteMapping("/clearCacheKey/{cacheKey}")
    public R<Void> clearCacheKey(@PathVariable String cacheKey) {
        redisTemplate.delete(cacheKey);
        return R.ok();
    }

    /**
     * 清除所有缓存
     */
    @SaCheckPermission("monitor:cache:list")
    @DeleteMapping("/clearCacheAll")
    public R<Void> clearCacheAll() {
        Collection<String> cacheKeys = redisTemplate.keys("*");
        redisTemplate.delete(cacheKeys);
        return R.ok();
    }

}
