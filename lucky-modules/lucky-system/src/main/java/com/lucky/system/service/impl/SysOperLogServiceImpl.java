package com.lucky.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.common.core.utils.bean.BeanUtils;
import com.lucky.common.log.event.OperLogEvent;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import com.lucky.system.domain.SysOperLog;
import com.lucky.system.domain.query.operLog.SysOperLogQuery;
import com.lucky.system.domain.vo.operLog.SysOperLogVO;
import com.lucky.system.mapper.SysOperLogMapper;
import com.lucky.system.service.ISysOperLogService;
import jakarta.annotation.Resource;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 操作日志 服务层处理
 *
 * @author lucky
 */
@Service
public class SysOperLogServiceImpl implements ISysOperLogService {

    @Resource
    private SysOperLogMapper operLogMapper;

    @Async
    @EventListener
    public void recordOper(OperLogEvent operLogEvent) {
        SysOperLog operLog = new SysOperLog();
        BeanUtils.copyProperties(operLogEvent, operLog);
        operLogMapper.insert(operLog);
    }

    @Override
    public TableDataInfo<SysOperLogVO> selectOperLogList(PageQuery pageQuery, SysOperLogQuery query) {
        IPage<SysOperLogVO> result = operLogMapper.selectPage(pageQuery.build(), query);
        return TableDataInfo.build(result);
    }

    @Override
    public List<SysOperLog> selectOperLogList(SysOperLogQuery query) {
        return operLogMapper.selectList(query);
    }

    @Override
    public int deleteOperLogByIds(Long[] operIds) {
        return operLogMapper.deleteByIds(Arrays.asList(operIds));
    }

    @Override
    public int cleanOperLog() {
        return operLogMapper.delete(new QueryWrapper<>());
    }

}