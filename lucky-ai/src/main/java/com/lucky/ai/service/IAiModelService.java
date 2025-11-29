package com.lucky.ai.service;

import com.lucky.ai.controller.model.vo.model.AiModelPageReqVO;
import com.lucky.ai.controller.model.vo.model.AiModelSaveReqVO;
import com.lucky.ai.domain.AiModel;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.image.ImageModel;

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
     * 校验模型是否可使用
     *
     * @param id 编号
     * @return 模型
     */
    AiModel validateModel(Long id);

    /**
     * 创建模型
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createModel(AiModelSaveReqVO createReqVO);

    /**
     * 更新模型
     *
     * @param updateReqVO 更新信息
     * @return 影响行数
     */
    int updateModel(AiModelSaveReqVO updateReqVO);

    /**
     * 删除模型
     *
     * @param id 编号
     * @return 影响行数
     */
    int deleteModel(Long id);

    /**
     * 获得模型
     *
     * @param id 编号
     * @return 模型
     */
    AiModel getModel(Long id);

    /**
     * 获得模型分页
     *
     * @param pageReqVO 分页查询
     * @return 模型分页
     */
    List<AiModel> getModelPage(AiModelPageReqVO pageReqVO);

    /**
     * 获得模型列表
     *
     * @param status   状态
     * @param type     类型
     * @param platform 平台
     * @return 模型列表
     */
    List<AiModel> getModelListByStatusAndType(Integer status, Integer type, String platform);

    // ========== 与 Spring AI 集成 ==========

    /**
     * 获得 ChatModel 对象
     *
     * @param id 编号
     * @return ChatModel 对象
     */
    ChatModel getChatModel(Long id);

    /**
     * 获得 ImageModel 对象
     *
     * @param id 编号
     * @return ImageModel 对象
     */
    ImageModel getImageModel(Long id);

}
