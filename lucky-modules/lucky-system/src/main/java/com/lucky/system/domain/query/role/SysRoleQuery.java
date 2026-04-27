package com.lucky.system.domain.query.role;

import com.lucky.common.mybatis.core.domain.DataScopeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色查询参数
 *
 * @author lucky
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleQuery extends DataScopeEntity {

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
