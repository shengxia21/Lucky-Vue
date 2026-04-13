package com.lucky.generator.service;

import com.lucky.generator.domain.vo.GenTableColumnVO;

import java.util.List;

/**
 * 业务字段 服务层
 *
 * @author ruoyi
 */
public interface IGenTableColumnService {

    /**
     * 查询业务字段列表
     *
     * @param tableId 业务字段编号
     * @return 业务字段集合
     */
    List<GenTableColumnVO> getGenTableColumnListByTableId(Long tableId);

}
