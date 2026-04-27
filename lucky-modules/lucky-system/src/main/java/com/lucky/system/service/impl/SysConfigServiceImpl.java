package com.lucky.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.common.core.constant.CacheConstants;
import com.lucky.common.core.constant.UserConstants;
import com.lucky.common.core.exception.ServiceException;
import com.lucky.common.core.utils.MapstructUtils;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.core.utils.text.Convert;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import com.lucky.common.redis.utils.RedisCache;
import com.lucky.system.domain.SysConfig;
import com.lucky.system.domain.query.config.SysConfigQuery;
import com.lucky.system.domain.query.config.SysConfigSaveQuery;
import com.lucky.system.domain.vo.config.SysConfigVO;
import com.lucky.system.mapper.SysConfigMapper;
import com.lucky.system.service.ISysConfigService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 参数配置 服务层实现
 *
 * @author lucky
 */
@Service
public class SysConfigServiceImpl implements ISysConfigService {

    @Resource
    private SysConfigMapper configMapper;

    @Resource
    private RedisCache redisCache;

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init() {
        loadingConfigCache();
    }

    @Override
    public SysConfigVO selectConfigById(Long configId) {
        return configMapper.selectVoById(configId);
    }

    @Override
    public String selectConfigByKey(String configKey) {
        String configValue = Convert.toStr(redisCache.getCacheObject(getCacheKey(configKey)));
        if (StringUtils.isNotEmpty(configValue)) {
            return configValue;
        }
        SysConfigVO retConfig = configMapper.selectOneByConfigKey(configKey);
        if (StringUtils.isNotNull(retConfig)) {
            redisCache.setCacheObject(getCacheKey(configKey), retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    @Override
    public boolean selectCaptchaEnabled() {
        String captchaEnabled = selectConfigByKey("sys.account.captchaEnabled");
        if (StringUtils.isEmpty(captchaEnabled)) {
            return true;
        }
        return Convert.toBool(captchaEnabled);
    }

    @Override
    public boolean selectRegisterEnabled() {
        String registerEnabled = selectConfigByKey("sys.account.registerUser");
        if (StringUtils.isEmpty(registerEnabled)) {
            return false;
        }
        return Convert.toBool(registerEnabled);
    }

    @Override
    public TableDataInfo<SysConfigVO> selectConfigList(PageQuery pageQuery, SysConfigQuery query) {
        IPage<SysConfigVO> page = configMapper.selectPage(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    @Override
    public List<SysConfig> selectConfigList(SysConfigQuery query) {
        return configMapper.selectList(query);
    }

    @Override
    public int insertConfig(SysConfigSaveQuery config) {
        SysConfig sysConfig = MapstructUtils.convert(config, SysConfig.class);
        int row = configMapper.insert(sysConfig);
        if (row > 0) {
            redisCache.setCacheObject(getCacheKey(sysConfig.getConfigKey()), sysConfig.getConfigValue());
        }
        return row;
    }

    @Override
    public int updateConfig(SysConfigSaveQuery config) {
        SysConfig temp = configMapper.selectById(config.getConfigId());
        if (!StringUtils.equals(temp.getConfigKey(), config.getConfigKey())) {
            redisCache.deleteObject(getCacheKey(temp.getConfigKey()));
        }

        SysConfig sysConfig = MapstructUtils.convert(config, SysConfig.class);
        int row = configMapper.updateById(sysConfig);
        if (row > 0) {
            redisCache.setCacheObject(getCacheKey(sysConfig.getConfigKey()), sysConfig.getConfigValue());
        }
        return row;
    }

    @Override
    public void deleteConfigByIds(Long[] configIds) {
        for (Long configId : configIds) {
            SysConfig config = configMapper.selectById(configId);
            if (StringUtils.equals(UserConstants.YES, config.getConfigType())) {
                throw new ServiceException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
            configMapper.deleteById(configId);
            redisCache.deleteObject(getCacheKey(config.getConfigKey()));
        }
    }

    @Override
    public void loadingConfigCache() {
        List<SysConfig> configsList = configMapper.selectList();
        for (SysConfig config : configsList) {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
    }

    @Override
    public void clearConfigCache() {
        Collection<String> keys = redisCache.keys(CacheConstants.SYS_CONFIG_KEY + "*");
        redisCache.deleteObject(keys);
    }

    @Override
    public void resetConfigCache() {
        clearConfigCache();
        loadingConfigCache();
    }

    @Override
    public boolean checkConfigKeyUnique(Long configId, String configKey) {
        long newConfigId = StringUtils.isNull(configId) ? -1L : configId;
        SysConfigVO info = configMapper.selectOneByConfigKey(configKey);
        if (StringUtils.isNotNull(info) && info.getConfigId() != newConfigId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }

}