package com.lucky.ai.service;

import com.lucky.ai.domain.AiApiKey;

import java.util.List;

/**
 * AI API 秘钥Service接口
 * 
 * @author lucky
 */
public interface IAiApiKeyService {

    /**
     * 查询AI API 秘钥
     * 
     * @param id AI API 秘钥主键
     * @return AI API 秘钥
     */
    AiApiKey selectAiApiKeyById(Long id);

    /**
     * 查询AI API 秘钥列表
     * 
     * @param aiApiKey AI API 秘钥
     * @return AI API 秘钥集合
     */
    List<AiApiKey> selectAiApiKeyList(AiApiKey aiApiKey);

    /**
     * 新增AI API 秘钥
     * 
     * @param aiApiKey AI API 秘钥
     * @return 结果
     */
    int insertAiApiKey(AiApiKey aiApiKey);

    /**
     * 修改AI API 秘钥
     * 
     * @param aiApiKey AI API 秘钥
     * @return 结果
     */
    int updateAiApiKey(AiApiKey aiApiKey);

    /**
     * 批量删除AI API 秘钥
     * 
     * @param ids 需要删除的AI API 秘钥主键集合
     * @return 结果
     */
    int deleteAiApiKeyByIds(Long[] ids);

    /**
     * 删除AI API 秘钥信息
     * 
     * @param id AI API 秘钥主键
     * @return 结果
     */
    int deleteAiApiKeyById(Long id);

}
