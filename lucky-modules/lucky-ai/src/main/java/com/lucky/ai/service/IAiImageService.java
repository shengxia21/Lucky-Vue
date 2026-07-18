package com.lucky.ai.service;

import com.lucky.ai.domain.query.image.AiImageMyQuery;
import com.lucky.ai.domain.query.image.AiImageQuery;
import com.lucky.ai.domain.query.image.AiImageUpdateQuery;
import com.lucky.ai.domain.vo.image.AiImageVO;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;

import java.util.List;

/**
 * AI 绘画Service接口
 *
 * @author lucky
 */
public interface IAiImageService {

    /**
     * 获取【我的】绘图分页
     *
     * @param pageQuery 分页查询对象
     * @param query     查询参数
     * @return 分页结果
     */
    TableDataInfo<AiImageVO> selectMyImageList(PageQuery pageQuery, AiImageMyQuery query);

    /**
     * 获取【我的】绘图详情
     *
     * @param id 绘画主键
     * @return 绘图详情
     */
    AiImageVO selectMyImageById(Long id);

    /**
     * 获取我的ID列表查询绘画列表
     *
     * @param ids 绘画主键列表
     * @return 绘画列表
     */
    List<AiImageVO> selectMyImageListByIds(List<Long> ids);

    /**
     * 删除【我的】绘图记录
     *
     * @param id 绘图记录ID
     * @return 结果
     */
    int deleteMyImageById(Long id);

    /**
     * 获得绘画列表
     *
     * @param pageQuery 分页查询对象
     * @param query     查询参数
     * @return 分页结果
     */
    TableDataInfo<AiImageVO> selectImageList(PageQuery pageQuery, AiImageQuery query);

    /**
     * 更新绘画
     *
     * @param query 更新参数
     * @return 结果
     */
    int updateImage(AiImageUpdateQuery query);

    /**
     * 删除绘画
     *
     * @param ids 绘画主键数组
     * @return 结果
     */
    int deleteImageByIds(Long[] ids);

}
