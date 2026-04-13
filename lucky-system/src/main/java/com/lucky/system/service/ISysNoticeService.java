package com.lucky.system.service;

import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.system.domain.query.notice.SysNoticeQuery;
import com.lucky.system.domain.query.notice.SysNoticeSaveQuery;
import com.lucky.system.domain.vo.notice.SysNoticeVO;

/**
 * 公告 服务层
 *
 * @author ruoyi
 */
public interface ISysNoticeService {

    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    SysNoticeVO selectNoticeById(Long noticeId);

    /**
     * 查询公告列表
     *
     * @param pageQuery 分页参数
     * @param query     公告查询对象
     * @return 公告集合
     */
    TableDataInfo<SysNoticeVO> selectNoticeList(PageQuery pageQuery, SysNoticeQuery query);

    /**
     * 新增公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    int insertNotice(SysNoticeSaveQuery notice);

    /**
     * 修改公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    int updateNotice(SysNoticeSaveQuery notice);

    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    int deleteNoticeByIds(Long[] noticeIds);

    /**
     * 查询未读数量
     *
     * @param userId 用户ID
     * @return 未读数量
     */
    Long selectUnreadCount(Long userId);

}