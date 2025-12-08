package com.lucky.ai.service;

import com.lucky.ai.controller.model.vo.apikey.AiApiKeyPageReqVO;
import com.lucky.ai.controller.model.vo.apikey.AiApiKeySaveReqVO;
import com.lucky.ai.domain.AiApiKey;

import java.util.List;

/**
 * AI API 秘钥Service接口
 *
 * @author lucky
 */
public interface IAiApiKeyService {

    /**
     * 创建 API 密钥
     *
     * @param createReqVO 创建请求vo
     * @return 结果
     */
    Long createApiKey(AiApiKeySaveReqVO createReqVO);

    /**
     * 更新 API 密钥
     *
     * @param updateReqVO 更新请求vo
     * @return 结果
     */
    int updateApiKey(AiApiKeySaveReqVO updateReqVO);

     /**
     * 删除 API 密钥
     *
     * @param id API 密钥主键
     * @return 结果
     */
    int deleteApiKey(Long id);

    /**
     * 查询 API 密钥
     *
     * @param id API 密钥主键
     * @return API 密钥
     */
    AiApiKey getApiKey(Long id);

    /**
     * 查询 API 密钥分页
     *
     * @param pageReqVO 分页查询请求vo
     * @return API 密钥分页结果
     */
    List<AiApiKey> getApiKeyPage(AiApiKeyPageReqVO pageReqVO);

    /**
     * 获得 API 密钥分页列表
     *
     * @return API 密钥分页结果
     */
    List<AiApiKey> getApiKeyList();

    /**
     * 获得默认 API 密钥
     *
     * @param platform 平台
     * @param status   状态
     * @return 默认 API 密钥
     */
    AiApiKey getRequiredDefaultApiKey(String platform, Integer status);

    /**
     * 校验 API 密钥是否有效
     *
     * @param id API 密钥主键
     * @return API 密钥
     */
    AiApiKey validateApiKey(Long id);

}
