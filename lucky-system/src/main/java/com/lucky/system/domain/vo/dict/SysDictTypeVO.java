package com.lucky.system.domain.vo.dict;

import com.lucky.common.core.domain.entity.SysDictType;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.util.Date;

/**
 * 字典类型VO
 *
 * @author lucky
 */
@Data
@AutoMapper(target = SysDictType.class)
public class SysDictTypeVO {

    /**
     * 字典主键
     */
    private Long dictId;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

}