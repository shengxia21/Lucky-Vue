package com.lucky.ai.mapper;

import com.lucky.ai.controller.model.vo.model.AiModelPageReqVO;
import com.lucky.ai.domain.AiModel;

import java.util.List;

/**
 * AI 模型Mapper接口
 *
 * @author lucky
 */
public interface AiModelMapper {

    /**
     * 查询默认AI 模型
     *
     * @param type   模型类型
     * @param status 状态
     * @return 模型
     */
    AiModel selectFirstByStatus(Integer type, Integer status);

    /**
     * 查询AI 模型
     *
     * @param id AI 模型主键
     * @return AI 模型
     */
    AiModel selectAiModelById(Long id);

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
     * 删除AI 模型
     *
     * @param id AI 模型主键
     * @return 结果
     */
    int deleteAiModelById(Long id);

    /**
     * 查询AI 模型分页
     *
     * @param pageReqVO 分页查询
     * @return 模型分页
     */
    List<AiModel> selectPage(AiModelPageReqVO pageReqVO);

    /**
     * 查询AI 模型列表
     *
     * @param status   状态
     * @param type     类型
     * @param platform 平台
     * @return 模型列表
     */
    List<AiModel> selectListByStatusAndType(Integer status, Integer type, String platform);

}
