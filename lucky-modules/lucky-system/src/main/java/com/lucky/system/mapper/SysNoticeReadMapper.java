package com.lucky.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.common.mybatis.core.mapper.BaseMapperX;
import com.lucky.system.domain.SysNoticeRead;
import com.lucky.system.domain.vo.notice.SysNoticeReadUserVO;
import com.lucky.system.domain.vo.notice.SysNoticeReadVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公告已读记录 数据层
 *
 * @author lucky
 */
public interface SysNoticeReadMapper extends BaseMapperX<SysNoticeRead, SysNoticeRead> {

    /**
     * 查询带已读状态的公告列表（SQL层限制条数，一次查询完成）
     *
     * @param userId 用户ID
     * @param limit  最多返回条数
     * @return 带 isRead 标记的公告列表
     */
    List<SysNoticeReadVO> selectNoticeListWithReadStatus(@Param("userId") Long userId, @Param("limit") int limit);

    /**
     * 查询已阅读某公告的用户列表
     *
     * @param page        分页参数
     * @param noticeId    公告ID
     * @param searchValue 搜索值
     * @return 已读用户列表
     */
    IPage<SysNoticeReadUserVO> selectReadUsersByNoticeId(IPage<?> page, @Param("noticeId") Long noticeId, @Param("searchValue") String searchValue);

}
