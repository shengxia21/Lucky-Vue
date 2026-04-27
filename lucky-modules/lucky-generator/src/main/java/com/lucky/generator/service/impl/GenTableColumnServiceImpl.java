package com.lucky.generator.service.impl;

import com.lucky.generator.domain.vo.GenTableColumnVO;
import com.lucky.generator.mapper.GenTableColumnMapper;
import com.lucky.generator.service.IGenTableColumnService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务字段 服务层实现
 *
 * @author ruoyi
 */
@Service
public class GenTableColumnServiceImpl implements IGenTableColumnService {

    @Resource
    private GenTableColumnMapper genTableColumnMapper;

    /**
     * 查询业务字段列表
     *
     * @param tableId 业务字段编号
     * @return 业务字段集合
     */
    @Override
    public List<GenTableColumnVO> getGenTableColumnListByTableId(Long tableId) {
        return genTableColumnMapper.selectListByTableId(tableId);
    }

}
