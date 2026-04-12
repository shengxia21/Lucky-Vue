package com.lucky.system.domain.query.operLog;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 操作日志分页查询请求
 *
 * @author lucky
 */
@Data
public class SysOperLogQuery {

    /**
     * 操作 IP 地址
     */
    private String operIp;

    /**
     * 操作模块
     */
    private String title;

    /**
     * 业务类型
     */
    private Integer businessType;

    /**
     * 业务类型数组
     */
    private Integer[] businessTypes;

    /**
     * 操作状态（0正常 1异常）
     */
    private Integer status;

    /**
     * 查询参数
     */
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public Integer[] getBusinessTypes() {
        if (businessTypes == null) {
            businessTypes = new Integer[0];
        }
        return businessTypes;
    }

}