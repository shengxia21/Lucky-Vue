package com.lucky.system.domain.vo.loginInfo;

import com.lucky.system.domain.SysLoginInfo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统登录日志VO
 *
 * @author lucky
 */
@Data
@AutoMapper(target = SysLoginInfo.class)
public class SysLoginInfoVO {

    /**
     * ID
     */
    private Long infoId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 登录状态
     */
    private String status;

    /**
     * IP地址
     */
    private String ipaddr;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 消息
     */
    private String msg;

    /**
     * 访问时间
     */
    private LocalDateTime loginTime;

}