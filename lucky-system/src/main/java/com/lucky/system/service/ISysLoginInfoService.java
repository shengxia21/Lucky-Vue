package com.lucky.system.service;

import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.system.domain.SysLoginInfo;
import com.lucky.system.domain.query.loginInfo.SysLoginInfoQuery;
import com.lucky.system.domain.vo.loginInfo.SysLoginInfoVO;

import java.util.List;

/**
 * 系统访问日志情况信息 服务层
 *
 * @author ruoyi
 */
public interface ISysLoginInfoService {

    /**
     * 新增系统登录日志
     *
     * @param loginInfo 访问日志对象
     */
    void insertLoginInfo(SysLoginInfo loginInfo);

    /**
     * 查询系统登录日志集合
     *
     * @param pageQuery 分页查询对象
     * @param query 访问日志查询对象
     * @return 登录记录集合
     */
    TableDataInfo<SysLoginInfoVO> selectLoginInfoList(PageQuery pageQuery, SysLoginInfoQuery query);

    /**
     * 查询系统登录日志集合
     *
     * @param query 访问日志查询对象
     * @return 登录记录集合
     */
    List<SysLoginInfo> selectLoginInfoList(SysLoginInfoQuery query);

    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    int deleteLoginInfoByIds(Long[] infoIds);

    /**
     * 清空系统登录日志
     */
    int cleanLoginInfo();

}