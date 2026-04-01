package com.lucky.ai.service;

import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.query.apiKey.ApiKeyPageQuery;
import com.lucky.ai.domain.query.apiKey.ApiKeySaveQuery;
import com.lucky.ai.domain.vo.apikey.ApiKeyVO;
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
     * @param query 创建请求vo
     * @return 结果
     */
    Long createApiKey(ApiKeySaveQuery query);

    /**
     * 更新 API 密钥
     *
     * @param query 更新请求vo
     * @return 结果
     */
    int updateApiKey(ApiKeySaveQuery query);

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
     * @param query 查询参数
     * @return API 密钥分页结果
     */
    TableDataInfo<ApiKeyVO> getApiKeyPage(PageQuery pageQuery, ApiKeyPageQuery query);

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
