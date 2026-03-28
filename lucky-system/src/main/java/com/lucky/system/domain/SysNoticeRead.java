package com.lucky.system.domain;

import lombok.Data;

import java.util.Date;

/**
 * 公告已读记录表 sys_notice_read
 *
 * @author ruoyi
 */
@Data
public class SysNoticeRead {

    /**
     * 主键
     */
    private Long readId;

    /**
     * 公告ID
     */
    private Long noticeId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 阅读时间
     */
    private Date readTime;

}
