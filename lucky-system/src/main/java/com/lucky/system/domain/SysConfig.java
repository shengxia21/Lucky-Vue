package com.lucky.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lucky.common.annotation.Excel;
import com.lucky.common.annotation.Excel.ColumnType;
import com.lucky.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 参数配置表 sys_config
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_config")
public class SysConfig extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 参数主键
     */
    @Excel(name = "参数主键", cellType = ColumnType.NUMERIC)
    @TableId(value = "config_id")
    private Long configId;

    /**
     * 参数名称
     */
    @Excel(name = "参数名称")
    private String configName;

    /**
     * 参数键名
     */
    @Excel(name = "参数键名")
    private String configKey;

    /**
     * 参数键值
     */
    @Excel(name = "参数键值")
    private String configValue;

    /**
     * 系统内置（Y是 N否）
     */
    @Excel(name = "系统内置", readConverterExp = "Y=是,N=否")
    private String configType;

    /**
     * 备注
     */
    private String remark;

}
