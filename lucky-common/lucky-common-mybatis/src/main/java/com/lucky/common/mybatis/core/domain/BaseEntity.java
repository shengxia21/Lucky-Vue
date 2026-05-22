package com.lucky.common.mybatis.core.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fhs.core.trans.vo.TransPojo;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Entity基类
 * <p>
 * 为什么实现 {@link TransPojo} 接口？
 * 因为使用 Easy-Trans TransType.SIMPLE 模式，集成 MyBatis Plus 查询
 *
 * @author lucky
 */
@Data
@JsonIgnoreProperties(value = "transMap") // 由于 Easy-Trans 会添加 transMap 属性，避免 Jackson 在 Spring Cache 反序列化报错
public class BaseEntity implements Serializable, TransPojo {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 创建部门
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createDept;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

}
