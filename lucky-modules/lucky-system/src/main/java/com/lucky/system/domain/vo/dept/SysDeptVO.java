package com.lucky.system.domain.vo.dept;

import com.lucky.system.domain.SysDept;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 部门VO
 *
 * @author lucky
 */
@Data
@AutoMapper(target = SysDept.class)
public class SysDeptVO {

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 父部门ID
     */
    private Long parentId;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 父部门名称
     */
    private String parentName;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 部门状态:0正常,1停用
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}