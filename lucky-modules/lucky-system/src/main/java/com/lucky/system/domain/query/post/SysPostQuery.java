package com.lucky.system.domain.query.post;

import lombok.Data;

/**
 * 岗位信息查询对象
 *
 * @author lucky
 */
@Data
public class SysPostQuery {

    /**
     * 岗位编码
     */
    private String postCode;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

}