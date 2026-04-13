package com.lucky.system.domain.vo.notice;

import com.lucky.system.domain.SysNotice;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.util.Date;

/**
 * 通知公告VO
 *
 * @author lucky
 */
@Data
@AutoMapper(target = SysNotice.class)
public class SysNoticeVO {

    /**
     * 公告ID
     */
    private Long noticeId;

    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 公告类型（1通知 2公告）
     */
    private String noticeType;

    /**
     * 公告内容
     */
    private String noticeContent;

    /**
     * 公告状态（0正常 1关闭）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

}