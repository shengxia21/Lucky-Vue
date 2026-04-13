package com.lucky.generator.domain.vo;

import com.lucky.generator.domain.GenTable;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.util.Date;

/**
 * 生成表VO
 *
 * @author lucky
 */
@Data
@AutoMapper(target = GenTable.class)
public class GenTableVO {

    /**
     * 编号
     */
    private Long tableId;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 表描述
     */
    private String tableComment;

    /**
     * 实体类名称(首字母大写)
     */
    private String className;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
