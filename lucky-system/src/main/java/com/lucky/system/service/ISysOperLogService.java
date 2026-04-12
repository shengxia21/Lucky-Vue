package com.lucky.system.service;

import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.system.domain.SysOperLog;
import com.lucky.system.domain.query.operLog.SysOperLogQuery;
import com.lucky.system.domain.vo.operLog.SysOperLogVO;

import java.util.List;

/**
 * 操作日志 服务层
 *
 * @author ruoyi
 */
public interface ISysOperLogService {

    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    void insertOperLog(SysOperLog operLog);

    /**
     * 分页查询系统操作日志集合
     *
     * @param pageQuery 分页查询对象
     * @param query     查询条件
     * @return 操作日志分页集合
     */
    TableDataInfo<SysOperLogVO> selectOperLogList(PageQuery pageQuery, SysOperLogQuery query);

    /**
     * 查询系统操作日志集合
     *
     * @param query 查询条件
     * @return 操作日志集合
     */
    List<SysOperLog> selectOperLogList(SysOperLogQuery query);

    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    int deleteOperLogByIds(Long[] operIds);

    /**
     * 清空操作日志
     *
     * @return 结果
     */
    int cleanOperLog();

}