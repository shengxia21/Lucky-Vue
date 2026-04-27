package com.lucky.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.common.core.utils.DateUtils;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import com.lucky.system.domain.SysNoticeRead;
import com.lucky.system.domain.vo.notice.SysNoticeReadUserVO;
import com.lucky.system.domain.vo.notice.SysNoticeReadVO;
import com.lucky.system.mapper.SysNoticeReadMapper;
import com.lucky.system.service.ISysNoticeReadService;
import jakarta.annotation.Resource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 公告已读记录 服务层实现
 *
 * @author lucky
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
        record.setReadTime(DateUtils.getNowDate());
        try {
            noticeReadMapper.insert(record);
        } catch (DuplicateKeyException e) {
            // 忽略重复插入异常
        }
    }

    /**
     * 查询公告列表并标记当前用户已读状态
     */
    @Override
    public List<SysNoticeReadVO> selectNoticeListWithReadStatus(Long userId, int limit) {
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
        List<SysNoticeRead> records = new ArrayList<>(noticeIds.length);
        Date nowDate = DateUtils.getNowDate();
        for (Long noticeId : noticeIds) {
            SysNoticeRead record = new SysNoticeRead();
            record.setNoticeId(noticeId);
            record.setUserId(userId);
            record.setReadTime(nowDate);
            records.add(record);
        }
        try {
            noticeReadMapper.insertBatch(records);
        } catch (DuplicateKeyException e) {
            // 忽略重复插入异常
        }
    }

    @Override
    public TableDataInfo<SysNoticeReadUserVO> selectReadUsersByNoticeId(PageQuery pageQuery, Long noticeId, String searchValue) {
        IPage<SysNoticeReadUserVO> page = noticeReadMapper.selectReadUsersByNoticeId(pageQuery.build(), noticeId, searchValue);
        return TableDataInfo.build(page);
    }

    /**
     * 删除公告时清理对应已读记录
     */
    @Override
    public int deleteByNoticeIds(Long[] noticeIds) {
        return noticeReadMapper.deleteByIds(Arrays.asList(noticeIds));
    }

}
