package com.lucky.ai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.query.apiKey.ApiKeyPageQuery;
import com.lucky.ai.domain.query.apiKey.ApiKeySaveQuery;
import com.lucky.ai.domain.vo.apikey.ApiKeyVO;
import com.lucky.ai.enums.CommonStatusEnum;
import com.lucky.ai.mapper.AiApiKeyMapper;
import com.lucky.ai.service.AiApiKeyService;
import com.lucky.common.constant.AiErrorConstants;
import com.lucky.common.core.page.PageQuery;
import com.lucky.common.core.page.TableDataInfo;
import com.lucky.common.exception.ServiceException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI API 秘钥Service业务层处理
 *
 * @author lucky
 */
@Service
public class AiApiKeyServiceImpl implements AiApiKeyService {

    @Resource
    private AiApiKeyMapper apiKeyMapper;

    /**
     * 创建 API 密钥
     *
     * @param query 创建请求vo
     * @return 结果
     */
    @Override
    public Long createApiKey(ApiKeySaveQuery query) {
        // 插入
        AiApiKey apiKey = BeanUtil.toBean(query, AiApiKey.class);
        apiKeyMapper.insert(apiKey);
        // 返回
        return apiKey.getId();
    }

    /**
     * 更新 API 密钥
     *
     * @param query 更新请求vo
     * @return 结果
     */
    @Override
    public int updateApiKey(ApiKeySaveQuery query) {
        // 校验存在
        validateApiKeyExists(query.getId());
        // 更新
        AiApiKey updateObj = BeanUtil.toBean(query, AiApiKey.class);
        return apiKeyMapper.updateById(updateObj);
    }

    /**
     * 删除 API 密钥
     *
     * @param id API 密钥主键
     * @return 结果
     */
    @Override
    public int deleteApiKeyById(Long id) {
        // 校验存在
        validateApiKeyExists(id);
        // 删除
        return apiKeyMapper.deleteById(id);
    }

    /**
     * 查询 API 密钥
     *
     * @param id API 密钥主键
     * @return API 密钥
     */
    @Override
    public AiApiKey getApiKeyById(Long id) {
        return apiKeyMapper.selectById(id);
    }

    /**
     * 查询 API 密钥分页
     *
     * @param pageQuery 分页查询
     * @param query 分页查询参数
     * @return API 密钥分页结果
     */
    @Override
    public TableDataInfo<ApiKeyVO> getApiKeyPage(PageQuery pageQuery, ApiKeyPageQuery query) {
        IPage<AiApiKey> page = apiKeyMapper.selectPage(pageQuery.build(), query);
        return TableDataInfo.build(page, ApiKeyVO.class);
    }

    /**
     * 获得 API 密钥列表
     *
     * @return API 密钥列表
     */
    @Override
    public List<AiApiKey> getApiKeyList() {
        return apiKeyMapper.selectList(null);
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
        AiApiKey apiKey = apiKeyMapper.selectById(id);
        if (apiKey == null) {
            throw new ServiceException(AiErrorConstants.API_KEY_NOT_EXISTS);
        }
        return apiKey;
    }

}
