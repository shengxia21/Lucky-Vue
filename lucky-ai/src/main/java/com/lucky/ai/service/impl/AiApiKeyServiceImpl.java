package com.lucky.ai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.ai.controller.model.vo.apikey.AiApiKeyPageReqVO;
import com.lucky.ai.controller.model.vo.apikey.AiApiKeySaveReqVO;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.enums.CommonStatusEnum;
import com.lucky.ai.mapper.AiApiKeyMapper;
import com.lucky.ai.service.IAiApiKeyService;
import com.lucky.common.constant.AiErrorConstants;
import com.lucky.common.exception.ServiceException;
import com.lucky.common.utils.DateUtils;
import com.lucky.common.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI API 秘钥Service业务层处理
 *
 * @author lucky
 */
@Service
public class AiApiKeyServiceImpl implements IAiApiKeyService {

    @Resource
    private AiApiKeyMapper aiApiKeyMapper;

    /**
     * 创建 API 密钥
     *
     * @param createReqVO 创建请求vo
     * @return 结果
     */
    @Override
    public Long createApiKey(AiApiKeySaveReqVO createReqVO) {
        // 插入
        AiApiKey apiKey = BeanUtil.toBean(createReqVO, AiApiKey.class);
        apiKey.setCreateBy(SecurityUtils.getUsername());
        apiKey.setCreateTime(DateUtils.getNowDate());
        aiApiKeyMapper.insertAiApiKey(apiKey);
        // 返回
        return apiKey.getId();
    }

    /**
     * 更新 API 密钥
     *
     * @param updateReqVO 更新请求vo
     * @return 结果
     */
    @Override
    public int updateApiKey(AiApiKeySaveReqVO updateReqVO) {
        // 校验存在
        validateApiKeyExists(updateReqVO.getId());
        // 更新
        AiApiKey updateObj = BeanUtil.toBean(updateReqVO, AiApiKey.class);
        updateObj.setUpdateBy(SecurityUtils.getUsername());
        updateObj.setUpdateTime(DateUtils.getNowDate());
        return aiApiKeyMapper.updateAiApiKey(updateObj);
    }

    /**
     * 删除 API 密钥
     *
     * @param id API 密钥主键
     * @return 结果
     */
    @Override
    public int deleteApiKey(Long id) {
        // 校验存在
        validateApiKeyExists(id);
        // 删除
        return aiApiKeyMapper.deleteAiApiKeyById(id);
    }

    /**
     * 查询 API 密钥
     *
     * @param id API 密钥主键
     * @return API 密钥
     */
    @Override
    public AiApiKey getApiKey(Long id) {
        return aiApiKeyMapper.selectAiApiKeyById(id);
    }

    /**
     * 查询 API 密钥分页
     *
     * @param pageReqVO 分页查询请求vo
     * @return API 密钥分页结果
     */
    @Override
    public List<AiApiKey> getApiKeyPage(AiApiKeyPageReqVO pageReqVO) {
        return aiApiKeyMapper.selectAiApiKeyList(pageReqVO);
    }

    /**
     * 获得 API 密钥分页列表
     *
     * @return API 密钥分页结果
     */
    @Override
    public List<AiApiKey> getApiKeyList() {
        return aiApiKeyMapper.selectList();
    }

    /**
     * 获得默认 API 密钥
     *
     * @param platform 平台
     * @param status   状态
     * @return 默认 API 密钥
     */
    @Override
    public AiApiKey getRequiredDefaultApiKey(String platform, Integer status) {
        AiApiKey apiKey = aiApiKeyMapper.selectFirstByPlatformAndStatus(platform, status);
        if (apiKey == null) {
            throw new ServiceException(AiErrorConstants.API_KEY_NOT_EXISTS);
        }
        return apiKey;
    }

    /**
     * 校验 API 密钥是否有效
     *
     * @param id API 密钥主键
     * @return API 密钥
     */
    @Override
    public AiApiKey validateApiKey(Long id) {
        AiApiKey apiKey = validateApiKeyExists(id);
        if (CommonStatusEnum.isDisable(apiKey.getStatus())) {
            throw new ServiceException(AiErrorConstants.API_KEY_DISABLE);
        }
        return apiKey;
    }

    private AiApiKey validateApiKeyExists(Long id) {
        AiApiKey apiKey = aiApiKeyMapper.selectAiApiKeyById(id);
        if (apiKey == null) {
            throw new ServiceException(AiErrorConstants.API_KEY_NOT_EXISTS);
        }
        return apiKey;
    }

}
