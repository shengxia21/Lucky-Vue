package com.lucky.system.domain.vo.notice;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 公告已读用户VO
 *
 * @author lucky
 */
@Data
public class SysNoticeReadUserVO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 读取时间
     */
    private LocalDateTime readTime;

}
