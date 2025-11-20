package com.lucky.ai.service;

import com.lucky.ai.domain.AiImage;

import java.util.List;

/**
 * AI 绘画Service接口
 * 
 * @author lucky
 */
public interface IAiImageService {

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
     * @param aiImage AI 绘画
     * @return AI 绘画集合
     */
    List<AiImage> selectAiImageList(AiImage aiImage);

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
     * 批量删除AI 绘画
     * 
     * @param ids 需要删除的AI 绘画主键集合
     * @return 结果
     */
    int deleteAiImageByIds(Long[] ids);

    /**
     * 删除AI 绘画信息
     * 
     * @param id AI 绘画主键
     * @return 结果
     */
    int deleteAiImageById(Long id);

}
