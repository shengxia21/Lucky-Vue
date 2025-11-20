package com.lucky.ai.service.impl;

import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.mapper.AiApiKeyMapper;
import com.lucky.ai.service.IAiApiKeyService;
import com.lucky.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI API 秘钥Service业务层处理
 * 
 * @author lucky
 */
@Service
public class AiApiKeyServiceImpl implements IAiApiKeyService {

    @Autowired
    private AiApiKeyMapper aiApiKeyMapper;

    /**
     * 查询AI API 秘钥
     * 
     * @param id AI API 秘钥主键
     * @return AI API 秘钥
     */
    @Override
    public AiApiKey selectAiApiKeyById(Long id) {
        return aiApiKeyMapper.selectAiApiKeyById(id);
    }

    /**
     * 查询AI API 秘钥列表
     * 
     * @param aiApiKey AI API 秘钥
     * @return AI API 秘钥
     */
    @Override
    public List<AiApiKey> selectAiApiKeyList(AiApiKey aiApiKey) {
        return aiApiKeyMapper.selectAiApiKeyList(aiApiKey);
    }

    /**
     * 新增AI API 秘钥
     * 
     * @param aiApiKey AI API 秘钥
     * @return 结果
     */
    @Override
    public int insertAiApiKey(AiApiKey aiApiKey) {
        aiApiKey.setCreateTime(DateUtils.getNowDate());
        return aiApiKeyMapper.insertAiApiKey(aiApiKey);
    }

    /**
     * 修改AI API 秘钥
     * 
     * @param aiApiKey AI API 秘钥
     * @return 结果
     */
    @Override
    public int updateAiApiKey(AiApiKey aiApiKey) {
        aiApiKey.setUpdateTime(DateUtils.getNowDate());
        return aiApiKeyMapper.updateAiApiKey(aiApiKey);
    }

    /**
     * 批量删除AI API 秘钥
     * 
     * @param ids 需要删除的AI API 秘钥主键
     * @return 结果
     */
    @Override
    public int deleteAiApiKeyByIds(Long[] ids) {
        return aiApiKeyMapper.deleteAiApiKeyByIds(ids);
    }

    /**
     * 删除AI API 秘钥信息
     * 
     * @param id AI API 秘钥主键
     * @return 结果
     */
    @Override
    public int deleteAiApiKeyById(Long id) {
        return aiApiKeyMapper.deleteAiApiKeyById(id);
    }

}
