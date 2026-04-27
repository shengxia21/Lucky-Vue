package com.lucky.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.common.core.constant.UserConstants;
import com.lucky.common.core.exception.ServiceException;
import com.lucky.common.core.utils.MapstructUtils;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import com.lucky.system.domain.SysPost;
import com.lucky.system.domain.query.post.SysPostQuery;
import com.lucky.system.domain.query.post.SysPostSaveQuery;
import com.lucky.system.domain.vo.post.SysPostVO;
import com.lucky.system.mapper.SysPostMapper;
import com.lucky.system.mapper.SysUserPostMapper;
import com.lucky.system.service.ISysPostService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 岗位信息 服务层处理
 *
 * @author lucky
 */
@Service
public class SysPostServiceImpl implements ISysPostService {

    @Resource
    private SysPostMapper postMapper;

    @Resource
    private SysUserPostMapper userPostMapper;

    @Override
    public TableDataInfo<SysPostVO> selectPostList(PageQuery pageQuery, SysPostQuery query) {
        IPage<SysPostVO> page = postMapper.selectPage(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    @Override
    public List<SysPost> selectPostList(SysPostQuery query) {
        return postMapper.selectList(query);
    }

    @Override
    public List<SysPostVO> selectPostAll() {
        return postMapper.selectVoList();
    }

    @Override
    public SysPostVO selectPostById(Long postId) {
        return postMapper.selectVoById(postId);
    }

    @Override
    public List<Long> selectPostIdsByUserId(Long userId) {
        return postMapper.selectPostIdsByUserId(userId);
    }

    @Override
    public boolean checkPostNameUnique(Long postId, String postName) {
        long newPostId = StringUtils.isNull(postId) ? -1L : postId;
        SysPostVO info = postMapper.selectByPostName(postName);
        if (StringUtils.isNotNull(info) && info.getPostId() != newPostId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean checkPostCodeUnique(Long postId, String postCode) {
        long newPostId = StringUtils.isNull(postId) ? -1L : postId;
        SysPostVO info = postMapper.selectByPostCode(postCode);
        if (StringUtils.isNotNull(info) && info.getPostId() != newPostId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public Long countUserPostById(Long postId) {
        return userPostMapper.countPostByPostId(postId);
    }

    @Override
    public int deletePostByIds(Long[] postIds) {
        for (Long postId : postIds) {
            SysPost post = postMapper.selectById(postId);
            if (countUserPostById(postId) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", post.getPostName()));
            }
        }
        return postMapper.deleteByIds(Arrays.asList(postIds));
    }

    @Override
    public int insertPost(SysPostSaveQuery post) {
        SysPost sysPost = MapstructUtils.convert(post, SysPost.class);
        return postMapper.insert(sysPost);
    }

    @Override
    public int updatePost(SysPostSaveQuery post) {
        SysPost sysPost = MapstructUtils.convert(post, SysPost.class);
        return postMapper.updateById(sysPost);
    }

}