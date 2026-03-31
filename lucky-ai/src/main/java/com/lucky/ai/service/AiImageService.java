package com.lucky.ai.service;

import com.lucky.ai.controller.image.vo.AiImagePagePublicReqVO;
import com.lucky.ai.controller.image.vo.AiImagePageReqVO;
import com.lucky.ai.controller.image.vo.AiImageRespVO;
import com.lucky.ai.controller.image.vo.AiImageUpdateReqVO;
import com.lucky.ai.core.vo.image.ImageDrawRequest;
import com.lucky.ai.domain.AiImage;
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
     * @param pageReqVO 查询参数
     * @param userId    用户ID
     * @return 分页结果
     */
    TableDataInfo<AiImageRespVO> getImagePageMy(PageQuery pageQuery, AiImagePageReqVO pageReqVO, Long userId);

    /**
     * 获取公开的绘图列表
     *
     * @param pageQuery 分页查询对象
     * @param pageReqVO 查询参数
     * @return 分页结果
     */
    TableDataInfo<AiImageRespVO> getImagePagePublic(PageQuery pageQuery, AiImagePagePublicReqVO pageReqVO);

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
     * @param pageReqVO 查询参数
     * @return 分页结果
     */
    TableDataInfo<AiImageRespVO> getImagePage(PageQuery pageQuery, AiImagePageReqVO pageReqVO);

    /**
     * 更新绘画
     *
     * @param updateReqVO 更新参数
     * @return 结果
     */
    int updateImage(AiImageUpdateReqVO updateReqVO);

    /**
     * 删除绘画
     *
     * @param id 绘画主键
     * @return 结果
     */
    int deleteImageById(Long id);

}
