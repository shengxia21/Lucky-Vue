package com.lucky.ai.service;

import com.lucky.ai.domain.AiModel;

import java.util.List;

/**
 * AI 模型Service接口
 *
 * @author lucky
 */
public interface IAiModelService {

    /**
     * 获得默认的模型
     * <p>
     * 如果获取不到，则抛出 {@link com.lucky.common.exception.ServiceException}
     *
     * @param type 模型类型
     * @return 模型
     */
    AiModel getRequiredDefaultModel(Integer type);

    /**
     * 查询AI 模型
     *
     * @param id AI 模型主键
     * @return AI 模型
     */
    AiModel selectAiModelById(Long id);

    /**
     * 查询AI 模型列表
     *
     * @param aiModel AI 模型
     * @return AI 模型集合
     */
    List<AiModel> selectAiModelList(AiModel aiModel);

    /**
     * 新增AI 模型
     *
     * @param aiModel AI 模型
     * @return 结果
     */
    int insertAiModel(AiModel aiModel);

    /**
     * 修改AI 模型
     *
     * @param aiModel AI 模型
     * @return 结果
     */
    int updateAiModel(AiModel aiModel);

    /**
     * 批量删除AI 模型
     *
     * @param ids 需要删除的AI 模型主键集合
     * @return 结果
     */
    int deleteAiModelByIds(Long[] ids);

    /**
     * 删除AI 模型信息
     *
     * @param id AI 模型主键
     * @return 结果
     */
    int deleteAiModelById(Long id);

}
