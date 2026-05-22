package com.lucky.system.domain.query.role;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 角色查询参数
 *
 * @author lucky
 */
@Data
public class SysRoleQuery {

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权限
     */
    private String roleKey;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

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
