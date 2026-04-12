package com.lucky.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.system.domain.SysLoginInfo;
import com.lucky.system.domain.query.loginInfo.SysLoginInfoQuery;
import com.lucky.system.domain.vo.loginInfo.SysLoginInfoVO;
import com.lucky.system.mapper.SysLoginInfoMapper;
import com.lucky.system.service.ISysLoginInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 系统访问日志情况信息 服务层处理
 *
 * @author lucky
 */
@Service
public class SysLoginInfoServiceImpl implements ISysLoginInfoService {

    @Resource
    private SysLoginInfoMapper loginInfoMapper;

    @Override
    public void insertLoginInfo(SysLoginInfo loginInfo) {
        loginInfoMapper.insert(loginInfo);
    }

    @Override
    public TableDataInfo<SysLoginInfoVO> selectLoginInfoList(PageQuery pageQuery, SysLoginInfoQuery query) {
        IPage<SysLoginInfoVO> page = loginInfoMapper.selectPage(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    @Override
    public List<SysLoginInfo> selectLoginInfoList(SysLoginInfoQuery query) {
        return loginInfoMapper.selectList(query);
    }

    @Override
    public int deleteLoginInfoByIds(Long[] infoIds) {
        return loginInfoMapper.deleteByIds(Arrays.asList(infoIds));
    }

    @Override
    public int cleanLoginInfo() {
        return loginInfoMapper.delete(new QueryWrapper<>());
    }

}