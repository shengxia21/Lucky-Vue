package com.lucky.system.domain.query.user;

import com.lucky.common.mybatis.core.domain.DataScopeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户查询参数
 *
 * @author lucky
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserQuery extends DataScopeEntity {

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

}
