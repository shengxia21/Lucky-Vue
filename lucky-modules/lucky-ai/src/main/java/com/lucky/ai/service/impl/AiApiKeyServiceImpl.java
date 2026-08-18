package com.lucky.ai.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.query.apiKey.AiApiKeyQuery;
import com.lucky.ai.domain.query.apiKey.AiApiKeySaveQuery;
import com.lucky.ai.domain.vo.apikey.AiApiKeyVO;
import com.lucky.ai.mapper.AiApiKeyMapper;
import com.lucky.ai.service.IAiApiKeyService;
import com.lucky.common.ai.enums.AiStatus;
import com.lucky.common.core.constant.AiConstants;
import com.lucky.common.core.exception.ServiceException;
import com.lucky.common.core.utils.MapstructUtils;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * AI API 秘钥Service业务层处理
 *
 * @author lucky
 */
@Service
public class AiApiKeyServiceImpl implements IAiApiKeyService {

    @Resource
    private AiApiKeyMapper apiKeyMapper;

    @Override
    public TableDataInfo<AiApiKeyVO> selectApiKeyList(PageQuery pageQuery, AiApiKeyQuery query) {
        IPage<AiApiKeyVO> page = apiKeyMapper.selectPage(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    @Override
    public AiApiKeyVO selectApiKeyById(Long id) {
        return apiKeyMapper.selectVoById(id);
    }

    @Override
    public int insertApiKey(AiApiKeySaveQuery query) {
        AiApiKey apiKey = MapstructUtils.convert(query, AiApiKey.class);
        return apiKeyMapper.insert(apiKey);
    }

    @Override
    public int updateApiKey(AiApiKeySaveQuery query) {
        AiApiKey apiKey = MapstructUtils.convert(query, AiApiKey.class);
        return apiKeyMapper.updateById(apiKey);
    }

    @Override
    public int deleteApiKeyByIds(Long[] ids) {
        return apiKeyMapper.deleteByIds(Arrays.asList(ids));
    }

    @Override
    public List<AiApiKeyVO> selectApiKeyAll() {
        return apiKeyMapper.selectOptionList();
    }

    @Override
    public AiApiKey validateApiKey(Long id) {
        AiApiKey apiKey = validateApiKeyExists(id);
        if (AiStatus.DISABLE.getCode().equals(apiKey.getStatus())) {
            throw new ServiceException(StringUtils.format(AiConstants.API_KEY_DISABLE, apiKey.getName()));
        }
        return apiKey;
    }

    private AiApiKey validateApiKeyExists(Long id) {
        AiApiKey apiKey = apiKeyMapper.selectById(id);
        if (apiKey == null) {
            throw new ServiceException(AiConstants.API_KEY_NOT_EXISTS);
        }
        return apiKey;
    }

}
