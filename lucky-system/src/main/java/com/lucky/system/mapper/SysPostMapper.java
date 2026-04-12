package com.lucky.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lucky.common.core.mybatis.BaseMapperX;
import com.lucky.common.utils.StringUtils;
import com.lucky.system.domain.SysPost;
import com.lucky.system.domain.query.post.SysPostQuery;
import com.lucky.system.domain.vo.post.SysPostVO;

import java.util.List;

/**
 * 岗位信息 数据层
 *
 * @author lucky
 */
public interface SysPostMapper extends BaseMapperX<SysPost, SysPostVO> {

    /**
     * 根据用户ID获取岗位ID列表
     *
     * @param userId 用户ID
     * @return 岗位ID列表
     */
    List<Long> selectPostIdsByUserId(Long userId);

    /**
     * 根据用户名获取岗位列表
     *
     * @param userName 用户名
     * @return 岗位列表
     */
    List<SysPostVO> selectPostsByUserName(String userName);

    default IPage<SysPostVO> selectPage(Page<SysPost> page, SysPostQuery query) {
        return selectVoPage(page, buildWrapper(query));
    }

    default List<SysPost> selectList(SysPostQuery query) {
        return selectList(buildWrapper(query));
    }

    default Wrapper<SysPost> buildWrapper(SysPostQuery query) {
        return Wrappers.<SysPost>lambdaQuery()
                .like(StringUtils.isNotBlank(query.getPostCode()), SysPost::getPostCode, query.getPostCode())
                .like(StringUtils.isNotBlank(query.getPostName()), SysPost::getPostName, query.getPostName())
                .eq(StringUtils.isNotBlank(query.getStatus()), SysPost::getStatus, query.getStatus())
                .orderByAsc(SysPost::getPostSort);
    }

    default SysPostVO selectByPostCode(String postCode) {
        return selectVoOne(Wrappers.<SysPost>lambdaQuery().eq(SysPost::getPostCode, postCode), false);
    }

    default SysPostVO selectByPostName(String postName) {
        return selectVoOne(Wrappers.<SysPost>lambdaQuery().eq(SysPost::getPostName, postName), false);
    }

}