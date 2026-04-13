package com.lucky.system.domain.vo.notice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * 通知公告已读VO
 *
 * @author lucky
 */
@Data
public class SysNoticeReadVO {

    /**
     * 通知公告ID
     */
    private Long noticeId;

    /**
     * 通知公告标题
     */
    private String noticeTitle;

    /**
     * 通知公告类型
     */
    private String noticeType;

    /**
     * 通知公告状态
     */
    private String status;

    /**
     * 是否已读
     */
    @JsonProperty("isRead")
    private boolean isRead;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

}