package com.lucky.ai.service.impl;

import com.lucky.ai.domain.AiModel;
import com.lucky.ai.mapper.AiModelMapper;
import com.lucky.ai.service.IAiModelService;
import com.lucky.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI 模型Service业务层处理
 * 
 * @author lucky
 */
@Service
public class AiModelServiceImpl implements IAiModelService {

    @Autowired
    private AiModelMapper aiModelMapper;

    /**
     * 查询AI 模型
     * 
     * @param id AI 模型主键
     * @return AI 模型
     */
    @Override
    public AiModel selectAiModelById(Long id) {
        return aiModelMapper.selectAiModelById(id);
    }

    /**
     * 查询AI 模型列表
     * 
     * @param aiModel AI 模型
     * @return AI 模型
     */
    @Override
    public List<AiModel> selectAiModelList(AiModel aiModel) {
        return aiModelMapper.selectAiModelList(aiModel);
    }

    /**
     * 新增AI 模型
     * 
     * @param aiModel AI 模型
     * @return 结果
     */
    @Override
    public int insertAiModel(AiModel aiModel) {
        aiModel.setCreateTime(DateUtils.getNowDate());
        return aiModelMapper.insertAiModel(aiModel);
    }

    /**
     * 修改AI 模型
     * 
     * @param aiModel AI 模型
     * @return 结果
     */
    @Override
    public int updateAiModel(AiModel aiModel) {
        aiModel.setUpdateTime(DateUtils.getNowDate());
        return aiModelMapper.updateAiModel(aiModel);
    }

    /**
     * 批量删除AI 模型
     * 
     * @param ids 需要删除的AI 模型主键
     * @return 结果
     */
    @Override
    public int deleteAiModelByIds(Long[] ids) {
        return aiModelMapper.deleteAiModelByIds(ids);
    }

    /**
     * 删除AI 模型信息
     * 
     * @param id AI 模型主键
     * @return 结果
     */
    @Override
    public int deleteAiModelById(Long id) {
        return aiModelMapper.deleteAiModelById(id);
    }

}
