package com.lucky.ai.mapper;

import com.lucky.ai.controller.model.vo.apikey.AiApiKeyPageReqVO;
import com.lucky.ai.domain.AiApiKey;

import java.util.List;

/**
 * AI API 秘钥Mapper接口
 *
 * @author lucky
 */
public interface AiApiKeyMapper {

    /**
     * 查询AI API 秘钥
     *
     * @param id AI API 秘钥主键
     * @return AI API 秘钥
     */
    AiApiKey selectAiApiKeyById(Long id);

    /**
     * 查询 API 密钥分页
     *
     * @param pageReqVO 分页查询请求vo
     * @return API 密钥分页结果
     */
    List<AiApiKey> selectAiApiKeyList(AiApiKeyPageReqVO pageReqVO);

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
     * 删除 AI API 秘钥
     *
     * @param id AI API 秘钥主键
     * @return 结果
     */
    int deleteAiApiKeyById(Long id);

    /**
     * 获得 API 密钥分页列表
     *
     * @return API 密钥分页结果
     */
    List<AiApiKey> selectList();

    /**
     * 获得默认 API 密钥
     *
     * @param platform 平台
     * @param status   状态
     * @return 默认 API 密钥
     */
    AiApiKey selectFirstByPlatformAndStatus(String platform, Integer status);

}
