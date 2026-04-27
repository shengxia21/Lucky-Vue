package com.lucky.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.query.apiKey.AiApiKeyPageQuery;
import com.lucky.ai.domain.query.apiKey.AiApiKeySaveQuery;
import com.lucky.ai.domain.vo.apikey.AiApiKeyVO;
import com.lucky.ai.enums.CommonStatusEnum;
import com.lucky.ai.mapper.AiApiKeyMapper;
import com.lucky.ai.service.AiApiKeyService;
import com.lucky.common.core.constant.AiErrorConstants;
import com.lucky.common.core.exception.ServiceException;
import com.lucky.common.core.utils.MapstructUtils;
import com.lucky.common.mybatis.core.page.PageQuery;
import com.lucky.common.mybatis.core.page.TableDataInfo;
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

    @Override
    public Long createApiKey(AiApiKeySaveQuery query) {
        // 插入
        AiApiKey apiKey = MapstructUtils.convert(query, AiApiKey.class);
        apiKeyMapper.insert(apiKey);
        // 返回
        return apiKey.getId();
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
    public int deleteApiKeyById(Long id) {
        // 校验存在
        validateApiKeyExists(id);
        // 删除
        return apiKeyMapper.deleteById(id);
    }

    @Override
    public AiApiKeyVO getApiKeyById(Long id) {
        return apiKeyMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<AiApiKeyVO> getApiKeyPage(PageQuery pageQuery, AiApiKeyPageQuery query) {
        IPage<AiApiKeyVO> page = apiKeyMapper.selectPage(pageQuery.build(), query);
        return TableDataInfo.build(page);
    }

    @Override
    public List<AiApiKeyVO> getApiKeyList() {
        LambdaQueryWrapper<AiApiKey> wrapper = Wrappers.<AiApiKey>lambdaQuery()
                .select(AiApiKey::getId, AiApiKey::getName);
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
