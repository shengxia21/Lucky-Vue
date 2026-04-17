package com.lucky.system.service;

import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.system.domain.vo.notice.SysNoticeReadUserVO;
import com.lucky.system.domain.vo.notice.SysNoticeReadVO;

import java.util.List;

/**
 * 公告已读记录 服务层
 *
 * @author ruoyi
 */
public interface ISysNoticeReadService {

    /**
     * 标记已读（幂等，重复调用不报错）
     *
     * @param noticeId 公告ID
     * @param userId   用户ID
     */
    void markRead(Long noticeId, Long userId);

    /**
     * 查询公告列表并标记当前用户已读状态（用于首页展示）
     *
     * @param userId 用户ID
     * @param limit  最多返回条数
     * @return 带 isRead 标记的公告列表
     */
    List<SysNoticeReadVO> selectNoticeListWithReadStatus(Long userId, int limit);

    /**
     * 批量标记已读
     *
     * @param userId    用户ID
     * @param noticeIds 公告ID数组
     */
    void markReadBatch(Long userId, Long[] noticeIds);

    /**
     * 查询已阅读某公告的用户列表
     *
     * @param pageQuery   分页查询
     * @param noticeId    公告ID
     * @param searchValue 搜索值
     * @return 已读用户列表
     */
    TableDataInfo<SysNoticeReadUserVO> selectReadUsersByNoticeId(PageQuery pageQuery, Long noticeId, String searchValue);

    /**
     * 删除公告时清理对应已读记录
     *
     * @param noticeIds 公告ID数组
     */
    int deleteByNoticeIds(Long[] noticeIds);

}
