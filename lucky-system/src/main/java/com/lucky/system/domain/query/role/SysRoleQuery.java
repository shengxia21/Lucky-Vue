package com.lucky.system.domain.query.role;

import com.lucky.common.core.domain.DataScopeQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色查询参数
 *
 * @author lucky
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleQuery extends DataScopeQuery {

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

}
