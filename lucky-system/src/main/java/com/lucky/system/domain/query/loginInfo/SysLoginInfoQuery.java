package com.lucky.system.domain.query.loginInfo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统登录日志查询对象
 *
 * @author lucky
 */
@Data
public class SysLoginInfoQuery {

    /**
     * IP地址
     */
    private String ipaddr;

    /**
     * 登录状态
     */
    private String status;

    /**
     * 用户名
     */
    private String userName;

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