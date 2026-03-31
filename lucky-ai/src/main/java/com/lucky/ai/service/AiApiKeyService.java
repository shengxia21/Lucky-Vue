package com.lucky.ai.service;

import com.lucky.ai.controller.model.vo.apikey.AiApiKeyPageReqVO;
import com.lucky.ai.controller.model.vo.apikey.AiApiKeyRespVO;
import com.lucky.ai.controller.model.vo.apikey.AiApiKeySaveReqVO;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;

import java.util.List;

/**
 * AI API 秘钥Service接口
 *
 * @author lucky
 */
public interface AiApiKeyService {

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
    int deleteApiKeyById(Long id);

    /**
     * 查询 API 密钥
     *
     * @param id API 密钥主键
     * @return API 密钥
     */
    AiApiKey getApiKeyById(Long id);

    /**
     * 查询 API 密钥分页
     *
     * @param pageQuery 分页查询对象
     * @param pageReqVO 查询参数
     * @return API 密钥分页结果
     */
    TableDataInfo<AiApiKeyRespVO> getApiKeyPage(PageQuery pageQuery, AiApiKeyPageReqVO pageReqVO);

    /**
     * 获得 API 密钥列表
     *
     * @return API 密钥列表
     */
    List<AiApiKey> getApiKeyList();

    /**
     * 校验 API 密钥是否有效
     *
     * @param id API 密钥主键
     * @return API 密钥
     */
    AiApiKey validateApiKey(Long id);

}
