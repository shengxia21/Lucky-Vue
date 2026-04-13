package com.lucky.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.common.utils.MapstructUtils;
import com.lucky.system.domain.SysNotice;
import com.lucky.system.domain.query.notice.SysNoticeQuery;
import com.lucky.system.domain.query.notice.SysNoticeSaveQuery;
import com.lucky.system.domain.vo.notice.SysNoticeVO;
import com.lucky.system.mapper.SysNoticeMapper;
import com.lucky.system.service.ISysNoticeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * 公告 服务层实现
 *
 * @author lucky
 */
@Service
public class SysNoticeServiceImpl implements ISysNoticeService {

    @Resource
    private SysNoticeMapper noticeMapper;

    @Override
    public SysNoticeVO selectNoticeById(Long noticeId) {
        return noticeMapper.selectVoById(noticeId);
    }

    @Override
    public TableDataInfo<SysNoticeVO> selectNoticeList(PageQuery pageQuery, SysNoticeQuery query) {
        IPage<SysNoticeVO> page = noticeMapper.selectPage(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    @Override
    public int insertNotice(SysNoticeSaveQuery notice) {
        SysNotice sysNotice = MapstructUtils.convert(notice, SysNotice.class);
        return noticeMapper.insert(sysNotice);
    }

    @Override
    public int updateNotice(SysNoticeSaveQuery notice) {
        SysNotice sysNotice = MapstructUtils.convert(notice, SysNotice.class);
        return noticeMapper.updateById(sysNotice);
    }

    @Override
    public int deleteNoticeByIds(Long[] noticeIds) {
        return noticeMapper.deleteByIds(Arrays.asList(noticeIds));
    }

    @Override
    public Long selectUnreadCount(Long userId) {
        return noticeMapper.selectUnreadCount(userId);
    }

}