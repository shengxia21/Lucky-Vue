package com.lucky.system.domain.query.notice;

import lombok.Data;

/**
 * 通知公告查询对象
 *
 * @author lucky
 */
@Data
public class SysNoticeQuery {

    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 公告类型（1通知 2公告）
     */
    private String noticeType;

    /**
     * 创建人
     */
    private String createBy;

}