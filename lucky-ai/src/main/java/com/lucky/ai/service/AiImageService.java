package com.lucky.ai.service;

import com.lucky.ai.core.vo.image.ImageDrawRequest;
import com.lucky.ai.domain.AiImage;
import com.lucky.ai.domain.query.image.ImagePagePublicQuery;
import com.lucky.ai.domain.query.image.ImagePageQuery;
import com.lucky.ai.domain.query.image.ImageUpdateQuery;
import com.lucky.ai.domain.vo.image.ImageVO;
import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;

import java.util.List;

/**
 * AI 绘画Service接口
 *
 * @author lucky
 */
public interface AiImageService {

    /**
     * 获取【我的】绘图分页
     *
     * @param pageQuery 分页查询对象
     * @param query 查询参数
     * @param userId    用户ID
     * @return 分页结果
     */
    TableDataInfo<ImageVO> getImagePageMy(PageQuery pageQuery, ImagePageQuery query, Long userId);

    /**
     * 获取公开的绘图列表
     *
     * @param pageQuery 分页查询对象
     * @param query 查询参数
     * @return 分页结果
     */
    TableDataInfo<ImageVO> getImagePagePublic(PageQuery pageQuery, ImagePagePublicQuery query);

    /**
     * 根据ID查询绘画详情
     *
     * @param id 绘画主键
     * @return 绘图详情
     */
    AiImage getImageById(Long id);

    /**
     * 根据ID列表查询绘画列表
     *
     * @param ids 绘画主键列表
     * @return 绘画列表
     */
    List<AiImage> getImageListByIds(List<Long> ids);

    /**
     * 生成图片
     *
     * @param userId  用户ID
     * @param request 绘图参数
     * @return 绘图记录ID
     */
    Long drawImage(Long userId, ImageDrawRequest request);

    /**
     * 删除【我的】绘图记录
     *
     * @param id     绘图记录ID
     * @param userId 用户ID
     * @return 结果
     */
    int deleteImageMy(Long id, Long userId);

    /**
     * 获得绘画列表
     *
     * @param pageQuery 分页查询对象
     * @param query 查询参数
     * @return 分页结果
     */
    TableDataInfo<ImageVO> getImagePage(PageQuery pageQuery, ImagePageQuery query);

    /**
     * 更新绘画
     *
     * @param query 更新参数
     * @return 结果
     */
    int updateImage(ImageUpdateQuery query);

    /**
     * 删除绘画
     *
     * @param id 绘画主键
     * @return 结果
     */
    int deleteImageById(Long id);

}
