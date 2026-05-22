package com.lucky.system.domain.vo.cache;

import com.lucky.common.core.utils.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 缓存信息VO
 *
 * @author lucky
 */
@Data
@NoArgsConstructor
public class SysCacheVo {

    /**
     * 缓存名称
     */
    private String cacheName = "";

    /**
     * 缓存键名
     */
    private String cacheKey = "";

    /**
     * 缓存内容
     */
    private String cacheValue = "";

    /**
     * 备注
     */
    private String remark = "";

    public SysCacheVo(String cacheName, String remark) {
        this.cacheName = cacheName;
        this.remark = remark;
    }

    public SysCacheVo(String cacheName, String cacheKey, String cacheValue) {
        this.cacheName = StringUtils.replace(cacheName, ":", "");
        this.cacheKey = StringUtils.replace(cacheKey, cacheName, "");
        this.cacheValue = cacheValue;
    }

}
