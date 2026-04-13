package com.lucky.generator.domain.query;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务查询参数
 *
 * @author lucky
 */
@Data
public class GenTableQuery {

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 表描述
     */
    private String tableComment;

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
