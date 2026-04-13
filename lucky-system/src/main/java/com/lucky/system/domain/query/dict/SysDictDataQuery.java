package com.lucky.system.domain.query.dict;

import lombok.Data;

/**
 * 字典数据查询对象
 *
 * @author lucky
 */
@Data
public class SysDictDataQuery {

    /**
     * 字典标签
     */
    private String dictLabel;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

}