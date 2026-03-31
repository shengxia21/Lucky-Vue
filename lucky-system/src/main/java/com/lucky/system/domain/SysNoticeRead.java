package com.lucky.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
    private Date readTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableField(exist = false)
    private String delFlag;

}
