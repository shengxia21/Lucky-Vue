package com.lucky.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.common.core.constant.Constants;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.core.utils.ip.AddressUtils;
import com.lucky.common.core.utils.ip.IpUtils;
import com.lucky.common.log.event.LoginInfoEvent;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import com.lucky.common.web.utils.UserAgentUtils;
import com.lucky.system.domain.SysLoginInfo;
import com.lucky.system.domain.query.loginInfo.SysLoginInfoQuery;
import com.lucky.system.domain.vo.loginInfo.SysLoginInfoVO;
import com.lucky.system.mapper.SysLoginInfoMapper;
import com.lucky.system.service.ISysLoginInfoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 系统访问日志情况信息 服务层处理
 *
 * @author lucky
 */
@Slf4j
@Service
public class SysLoginInfoServiceImpl implements ISysLoginInfoService {

    @Resource
    private SysLoginInfoMapper loginInfoMapper;

    @Async
    @EventListener
    public void recordLoginInfo(LoginInfoEvent loginInfoEvent) {
        HttpServletRequest request = loginInfoEvent.getRequest();
        final String userAgent = request.getHeader("User-Agent");
        final String ip = IpUtils.getIpAddr(request);
        String address = AddressUtils.getRealAddressByIP(ip);
        // 打印信息到日志
        log.info("[{}]{}[{}][{}][{}]", ip, address, loginInfoEvent.getUserName(), loginInfoEvent.getStatus(), loginInfoEvent.getMessage());
        // 获取客户端操作系统
        String os = UserAgentUtils.getOperatingSystem(userAgent);
        // 获取客户端浏览器
        String browser = UserAgentUtils.getBrowser(userAgent);
        // 封装对象
        SysLoginInfo loginInfo = new SysLoginInfo();
        loginInfo.setUserName(loginInfoEvent.getUserName());
        loginInfo.setIpaddr(ip);
        loginInfo.setLoginLocation(address);
        loginInfo.setBrowser(browser);
        loginInfo.setOs(os);
        loginInfo.setMsg(loginInfoEvent.getMessage());
        loginInfo.setLoginTime(LocalDateTime.now());
        // 日志状态
        if (StringUtils.equalsAny(loginInfoEvent.getStatus(), Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
            loginInfo.setStatus(Constants.SUCCESS);
        } else if (Constants.LOGIN_FAIL.equals(loginInfoEvent.getStatus())) {
            loginInfo.setStatus(Constants.FAIL);
        }
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