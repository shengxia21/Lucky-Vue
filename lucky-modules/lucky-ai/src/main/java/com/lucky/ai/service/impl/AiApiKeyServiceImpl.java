package com.lucky.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.query.apiKey.AiApiKeyPageQuery;
import com.lucky.ai.domain.query.apiKey.AiApiKeySaveQuery;
import com.lucky.ai.domain.vo.apikey.AiApiKeyVO;
import com.lucky.ai.mapper.AiApiKeyMapper;
import com.lucky.ai.service.IAiApiKeyService;
import com.lucky.common.ai.enums.CommonStatusEnum;
import com.lucky.common.core.constant.AiErrorConstants;
import com.lucky.common.core.exception.ServiceException;
import com.lucky.common.core.utils.MapstructUtils;
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
    public int insertApiKey(AiApiKeySaveQuery query) {
        AiApiKey apiKey = MapstructUtils.convert(query, AiApiKey.class);
        return apiKeyMapper.insert(apiKey);
    }

    @Override
    public int updateApiKey(AiApiKeySaveQuery query) {
        // 校验存在
        validateApiKeyExists(query.getId());
        // 更新
        AiApiKey apiKey = MapstructUtils.convert(query, AiApiKey.class);
        return apiKeyMapper.updateById(apiKey);
    }

    @Override
    public int deleteApiKeyByIds(Long[] ids) {
        return apiKeyMapper.deleteByIds(Arrays.asList(ids));
    }

    @Override
    public AiApiKeyVO selectApiKeyById(Long id) {
        return apiKeyMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<AiApiKeyVO> selectApiKeyList(PageQuery pageQuery, AiApiKeyPageQuery query) {
        IPage<AiApiKeyVO> page = apiKeyMapper.selectPage(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    @Override
    public List<AiApiKeyVO> selectApiKeyAll() {
        LambdaQueryWrapper<AiApiKey> wrapper = Wrappers.<AiApiKey>lambdaQuery()
                .select(AiApiKey::getId, AiApiKey::getName)
                .eq(AiApiKey::getStatus, CommonStatusEnum.ENABLE.getStatus());
        return apiKeyMapper.selectVoList(wrapper);
    }

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
