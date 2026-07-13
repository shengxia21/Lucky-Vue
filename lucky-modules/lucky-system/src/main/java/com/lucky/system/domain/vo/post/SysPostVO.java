package com.lucky.system.domain.vo.post;

import com.lucky.system.domain.SysPost;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 岗位信息VO
 *
 * @author lucky
 */
@Data
@AutoMapper(target = SysPost.class)
public class SysPostVO {

    /**
     * 岗位序号
     */
    private Long postId;

    /**
     * 岗位编码
     */
    private String postCode;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 岗位排序
     */
    private Integer postSort;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}