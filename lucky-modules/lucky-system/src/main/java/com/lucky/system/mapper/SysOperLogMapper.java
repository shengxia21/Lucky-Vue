package com.lucky.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.mybatis.core.mapper.BaseMapperX;
import com.lucky.system.domain.SysOperLog;
import com.lucky.system.domain.query.operLog.SysOperLogQuery;
import com.lucky.system.domain.vo.operLog.SysOperLogVO;

import java.util.Arrays;
import java.util.List;

/**
 * 操作日志 数据层
 *
 * @author lucky
 */
public interface SysOperLogMapper extends BaseMapperX<SysOperLog, SysOperLogVO> {

    default IPage<SysOperLogVO> selectPage(IPage<SysOperLog> page, SysOperLogQuery query) {
        return selectVoPage(page, buildWrapper(query));
    }

    default List<SysOperLog> selectList(SysOperLogQuery query) {
        return selectList(buildWrapper(query));
    }

    default Wrapper<SysOperLog> buildWrapper(SysOperLogQuery query) {
        return Wrappers.<SysOperLog>lambdaQuery()
                .like(StringUtils.isNotBlank(query.getOperIp()), SysOperLog::getOperIp, query.getOperIp())
                .like(StringUtils.isNotBlank(query.getTitle()), SysOperLog::getTitle, query.getTitle())
                .eq(StringUtils.isNotNull(query.getBusinessType()), SysOperLog::getBusinessType, query.getBusinessType())
                .in(StringUtils.isNotNull(query.getBusinessTypes()) && query.getBusinessTypes().length > 0,
                        SysOperLog::getBusinessType, Arrays.asList(query.getBusinessTypes()))
                .eq(StringUtils.isNotNull(query.getStatus()), SysOperLog::getStatus, query.getStatus())
                .between(!query.getParams().isEmpty(), SysOperLog::getOperTime, query.getParams().get("beginTime"), query.getParams().get("endTime"))
                .orderByDesc(SysOperLog::getOperTime);
    }

}