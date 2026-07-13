package com.lucky.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 公告已读记录表 sys_notice_read
 *
 * @author ruoyi
 */
@Data
@TableName("sys_notice_read")
public class SysNoticeRead {

    /**
     * 主键
     */
    @TableId(value = "read_id")
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
    private LocalDateTime readTime;

}
