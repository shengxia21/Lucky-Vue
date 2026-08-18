package com.lucky.system.domain.query.dept;

import lombok.Data;

/**
 * 部门查询对象
 *
 * @author lucky
 */
@Data
public class SysDeptQuery {

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 父部门ID
     */
    private Long parentId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门状态（0正常 1停用）
     */
    private String status;

}