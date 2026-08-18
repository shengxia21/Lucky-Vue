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
     * 操作人员
     */
    private String operName;

    /**
     * 业务类型
     */
    private Integer businessType;

    /**
     * 操作状态（0正常 1异常）
     */
    private String status;

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

}