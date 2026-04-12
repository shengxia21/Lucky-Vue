package com.lucky.common.core.domain;

import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据权限查询基类
 *
 * @author lucky
 */
@Setter
public class DataScopeQuery {

    /**
     * 请求参数
     */
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

}
