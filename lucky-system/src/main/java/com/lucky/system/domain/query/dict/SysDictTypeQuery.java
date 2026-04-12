package com.lucky.system.domain.query.dict;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 字典类型查询对象
 *
 * @author lucky
 */
@Data
public class SysDictTypeQuery {

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

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