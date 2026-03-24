package com.lucky.system.service.impl;

import com.lucky.system.domain.SysNotice;
import com.lucky.system.domain.SysNoticeRead;
import com.lucky.system.mapper.SysNoticeReadMapper;
import com.lucky.system.service.ISysNoticeReadService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 公告已读记录 服务层实现
 *
 * @author ruoyi
 */
@Service
public class SysNoticeReadServiceImpl implements ISysNoticeReadService {

    @Resource
    private SysNoticeReadMapper noticeReadMapper;

    /**
     * 标记已读
     */
    @Override
    public void markRead(Long noticeId, Long userId) {
        SysNoticeRead record = new SysNoticeRead();
        record.setNoticeId(noticeId);
        record.setUserId(userId);
        noticeReadMapper.insertNoticeRead(record);
    }

    /**
     * 查询某用户未读公告数量
     */
    @Override
    public int selectUnreadCount(Long userId) {
        return noticeReadMapper.selectUnreadCount(userId);
    }

    /**
     * 查询公告列表并标记当前用户已读状态
     */
    @Override
    public List<SysNotice> selectNoticeListWithReadStatus(Long userId, int limit) {
        return noticeReadMapper.selectNoticeListWithReadStatus(userId, limit);
    }

    /**
     * 批量标记已读
     */
    @Override
    public void markReadBatch(Long userId, Long[] noticeIds) {
        if (noticeIds == null || noticeIds.length == 0) {
            return;
        }
        noticeReadMapper.insertNoticeReadBatch(userId, noticeIds);
    }

    /**
     * 删除公告时清理对应已读记录
     */
    @Override
    public void deleteByNoticeIds(Long[] noticeIds) {
        noticeReadMapper.deleteByNoticeIds(noticeIds);
    }

}
