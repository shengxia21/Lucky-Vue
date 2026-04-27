package com.lucky.system.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.common.mybatis.core.mapper.BaseMapperX;
import com.lucky.system.domain.SysUserPost;

import java.util.Arrays;

/**
 * 用户与岗位关联表 数据层
 *
 * @author lucky
 */
public interface SysUserPostMapper extends BaseMapperX<SysUserPost, SysUserPost> {

    default int deleteByUserId(Long userId) {
        return delete(Wrappers.<SysUserPost>lambdaQuery()
                .eq(SysUserPost::getUserId, userId));
    }

    default Long countPostByPostId(Long postId) {
        return selectCount(Wrappers.<SysUserPost>lambdaQuery()
                .eq(SysUserPost::getPostId, postId));
    }

    default int deleteByUserIds(Long[] userIds) {
        return delete(Wrappers.<SysUserPost>lambdaQuery()
                .in(SysUserPost::getUserId, Arrays.asList(userIds)));
    }

}
