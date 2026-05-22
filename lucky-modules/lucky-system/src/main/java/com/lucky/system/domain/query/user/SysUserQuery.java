package com.lucky.system.domain.query.user;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户查询参数
 *
 * @author lucky
 */
@Data
public class SysUserQuery {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 账号状态（0正常 1停用）
     */
    private String status;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 请求参数（时间查询）
     */
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

}
