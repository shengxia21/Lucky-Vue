package com.lucky.ai.service.impl;

import com.lucky.ai.domain.AiImage;
import com.lucky.ai.mapper.AiImageMapper;
import com.lucky.ai.service.IAiImageService;
import com.lucky.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI 绘画Service业务层处理
 * 
 * @author lucky
 */
@Service
public class AiImageServiceImpl implements IAiImageService {

    @Autowired
    private AiImageMapper aiImageMapper;

    /**
     * 查询AI 绘画
     * 
     * @param id AI 绘画主键
     * @return AI 绘画
     */
    @Override
    public AiImage selectAiImageById(Long id) {
        return aiImageMapper.selectAiImageById(id);
    }

    /**
     * 查询AI 绘画列表
     * 
     * @param aiImage AI 绘画
     * @return AI 绘画
     */
    @Override
    public List<AiImage> selectAiImageList(AiImage aiImage) {
        return aiImageMapper.selectAiImageList(aiImage);
    }

    /**
     * 新增AI 绘画
     * 
     * @param aiImage AI 绘画
     * @return 结果
     */
    @Override
    public int insertAiImage(AiImage aiImage) {
        aiImage.setCreateTime(DateUtils.getNowDate());
        return aiImageMapper.insertAiImage(aiImage);
    }

    /**
     * 修改AI 绘画
     * 
     * @param aiImage AI 绘画
     * @return 结果
     */
    @Override
    public int updateAiImage(AiImage aiImage) {
        aiImage.setUpdateTime(DateUtils.getNowDate());
        return aiImageMapper.updateAiImage(aiImage);
    }

    /**
     * 批量删除AI 绘画
     * 
     * @param ids 需要删除的AI 绘画主键
     * @return 结果
     */
    @Override
    public int deleteAiImageByIds(Long[] ids) {
        return aiImageMapper.deleteAiImageByIds(ids);
    }

    /**
     * 删除AI 绘画信息
     * 
     * @param id AI 绘画主键
     * @return 结果
     */
    @Override
    public int deleteAiImageById(Long id) {
        return aiImageMapper.deleteAiImageById(id);
    }

}
