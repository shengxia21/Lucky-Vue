package com.lucky.system.domain.query.config;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 参数配置查询对象
 *
 * @author lucky
 */
@Data
public class SysConfigQuery {

    /**
     * 参数名称
     */
    private String configName;

    /**
     * 参数键名
     */
    private String configKey;

    /**
     * 系统内置（Y是 N否）
     */
    private String configType;

    /**
     * 查询参数（时间查询）
     */
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

}