package com.lucky.ai.service;

import com.lucky.ai.domain.AiModel;
import com.lucky.ai.domain.query.model.AiModelPageQuery;
import com.lucky.ai.domain.query.model.AiModelSaveQuery;
import com.lucky.ai.domain.vo.model.AiModelVO;
import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;

import java.util.List;

/**
 * AI 模型Service接口
 *
 * @author lucky
 */
public interface AiModelService {

    /**
     * 获得默认的模型
     * <p>
     * 如果获取不到，则抛出 {@link com.lucky.common.exception.ServiceException}
     *
     * @param type 模型类型
     * @return 模型
     */
    AiModel getDefaultModelByType(Integer type);

    /**
     * 校验模型是否可使用
     *
     * @param id 编号
     * @return 模型
     */
    AiModel validateModel(Long id);

    /**
     * 创建模型
     *
     * @param query 创建信息
     * @return 编号
     */
    Long createModel(AiModelSaveQuery query);

    /**
     * 更新模型
     *
     * @param query 更新信息
     * @return 影响行数
     */
    int updateModel(AiModelSaveQuery query);

    /**
     * 删除模型
     *
     * @param id 编号
     * @return 影响行数
     */
    int deleteModelById(Long id);

    /**
     * 获得模型
     *
     * @param id 编号
     * @return 模型
     */
    AiModelVO getModelById(Long id);

    /**
     * 获得模型分页
     *
     * @param pageQuery 分页查询
     * @param query 请求参数
     * @return 模型分页
     */
    TableDataInfo<AiModelVO> getModelPage(PageQuery pageQuery, AiModelPageQuery query);

    /**
     * 获得模型列表
     *
     * @param status   状态
     * @param type     类型
     * @param platform 平台
     * @return 模型列表
     */
    List<AiModelVO> getModelList(Integer status, Integer type, String platform);

}
