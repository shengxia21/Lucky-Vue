package com.lucky.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.mybatis.core.mapper.BaseMapperX;
import com.lucky.system.domain.SysLoginInfo;
import com.lucky.system.domain.query.loginInfo.SysLoginInfoQuery;
import com.lucky.system.domain.vo.loginInfo.SysLoginInfoVO;

import java.util.List;

/**
 * 系统访问日志情况信息 数据层
 *
 * @author lucky
 */
public interface SysLoginInfoMapper extends BaseMapperX<SysLoginInfo, SysLoginInfoVO> {

    default IPage<SysLoginInfoVO> selectPage(Page<SysLoginInfo> page, SysLoginInfoQuery query) {
        return selectVoPage(page, buildWrapper(query));
    }

    default List<SysLoginInfo> selectList(SysLoginInfoQuery query) {
        return selectList(buildWrapper(query));
    }

    default Wrapper<SysLoginInfo> buildWrapper(SysLoginInfoQuery query) {
        return Wrappers.<SysLoginInfo>lambdaQuery()
                .like(StringUtils.isNotBlank(query.getIpaddr()), SysLoginInfo::getIpaddr, query.getIpaddr())
                .eq(StringUtils.isNotBlank(query.getStatus()), SysLoginInfo::getStatus, query.getStatus())
                .like(StringUtils.isNotBlank(query.getUserName()), SysLoginInfo::getUserName, query.getUserName())
                .between(!query.getParams().isEmpty(), SysLoginInfo::getLoginTime, query.getParams().get("beginTime"), query.getParams().get("endTime"))
                .orderByDesc(SysLoginInfo::getLoginTime);
    }

}