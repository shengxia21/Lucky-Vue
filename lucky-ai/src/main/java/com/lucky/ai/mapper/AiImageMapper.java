package com.lucky.ai.mapper;

import com.lucky.ai.controller.image.vo.AiImagePageReqVO;
import com.lucky.ai.controller.image.vo.AiImagePublicPageReqVO;
import com.lucky.ai.domain.AiImage;

import java.util.List;

/**
 * AI 绘画Mapper接口
 *
 * @author lucky
 */
public interface AiImageMapper {

    /**
     * 查询【我的】AI 绘画列表
     *
     * @param pageReqVO 分页查询参数
     * @return 绘画集合
     */
    List<AiImage> selectPageMy(AiImagePageReqVO pageReqVO);

    /**
     * 查询公开的绘图
     *
     * @param pageReqVO 分页查询参数
     * @return 绘画集合
     */
    List<AiImage> selectPage(AiImagePublicPageReqVO pageReqVO);

    /**
     * 查询AI 绘画
     *
     * @param id AI 绘画主键
     * @return AI 绘画
     */
    AiImage selectAiImageById(Long id);

    /**
     * 查询AI 绘画列表
     *
     * @param ids 绘画主键集合
     * @return 绘画列表
     */
    List<AiImage> selectByIds(List<Long> ids);

    /**
     * 查询AI 绘画列表
     *
     * @param pageReqVO 分页查询参数
     * @return 绘画集合
     */
    List<AiImage> selectAiImagePage(AiImagePageReqVO pageReqVO);

    /**
     * 新增AI 绘画
     *
     * @param aiImage AI 绘画
     * @return 结果
     */
    int insertAiImage(AiImage aiImage);

    /**
     * 修改AI 绘画
     *
     * @param aiImage AI 绘画
     * @return 结果
     */
    int updateAiImage(AiImage aiImage);

    /**
     * 删除AI 绘画
     *
     * @param id AI 绘画主键
     * @return 结果
     */
    int deleteAiImageById(Long id);

}
