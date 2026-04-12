package com.lucky.system.service;

import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.system.domain.SysPost;
import com.lucky.system.domain.query.post.SysPostQuery;
import com.lucky.system.domain.query.post.SysPostSaveQuery;
import com.lucky.system.domain.vo.post.SysPostVO;

import java.util.List;

/**
 * 岗位信息 服务层
 *
 * @author ruoyi
 */
public interface ISysPostService {

    /**
     * 查询岗位信息集合
     *
     * @param pageQuery 分页参数
     * @param query     岗位查询对象
     * @return 岗位列表
     */
    TableDataInfo<SysPostVO> selectPostList(PageQuery pageQuery, SysPostQuery query);

    /**
     * 查询岗位信息集合
     *
     * @param query 岗位查询对象
     * @return 岗位列表
     */
    List<SysPost> selectPostList(SysPostQuery query);

    /**
     * 查询所有岗位
     *
     * @return 岗位列表
     */
    List<SysPostVO> selectPostAll();

    /**
     * 通过岗位ID查询岗位信息
     *
     * @param postId 岗位ID
     * @return 角色对象信息
     */
    SysPostVO selectPostById(Long postId);

    /**
     * 根据用户ID获取岗位选择框列表
     *
     * @param userId 用户ID
     * @return 选中岗位ID列表
     */
    List<Long> selectPostIdsByUserId(Long userId);

    /**
     * 校验岗位名称
     *
     * @param postId   岗位ID
     * @param postName 岗位名称
     * @return 结果
     */
    boolean checkPostNameUnique(Long postId, String postName);

    /**
     * 校验岗位编码
     *
     * @param postId   岗位ID
     * @param postCode 岗位编码
     * @return 结果
     */
    boolean checkPostCodeUnique(Long postId, String postCode);

    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    Long countUserPostById(Long postId);

    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位ID
     * @return 结果
     */
    int deletePostByIds(Long[] postIds);

    /**
     * 新增保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    int insertPost(SysPostSaveQuery post);

    /**
     * 修改保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    int updatePost(SysPostSaveQuery post);

}