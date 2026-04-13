package com.lucky.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.common.core.mybatis.BaseMapperX;
import com.lucky.common.utils.StringUtils;
import com.lucky.system.domain.SysNotice;
import com.lucky.system.domain.query.notice.SysNoticeQuery;
import com.lucky.system.domain.vo.notice.SysNoticeVO;

/**
 * 通知公告表 数据层
 *
 * @author lucky
 */
public interface SysNoticeMapper extends BaseMapperX<SysNotice, SysNoticeVO> {

    default IPage<SysNoticeVO> selectPage(Page<SysNotice> page, SysNoticeQuery query) {
        LambdaQueryWrapper<SysNotice> wrapper = Wrappers.<SysNotice>lambdaQuery()
                .like(StringUtils.isNotBlank(query.getNoticeTitle()), SysNotice::getNoticeTitle, query.getNoticeTitle())
                .eq(StringUtils.isNotBlank(query.getNoticeType()), SysNotice::getNoticeType, query.getNoticeType())
                .like(StringUtils.isNotBlank(query.getCreateBy()), SysNotice::getCreateBy, query.getCreateBy())
                .orderByDesc(SysNotice::getCreateTime);
        return selectVoPage(page, wrapper);
    }

    default Long selectUnreadCount(Long userId) {
        return selectCount(Wrappers.<SysNotice>lambdaQuery()
                .eq(SysNotice::getStatus, "0")
                .notExists("select 1 from sys_notice_read r where r.notice_id = n.notice_id and r.user_id = #{userId}", userId));
    }

}