package com.lucky.ai.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.ai.domain.query.apiKey.AiApiKeyQuery;
import com.lucky.ai.domain.vo.apikey.AiApiKeyVO;
import com.lucky.common.core.enums.DataStatus;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.mybatis.core.mapper.BaseMapperX;

import java.util.List;

/**
 * AI API 秘钥Mapper接口
 *
 * @author lucky
 */
public interface AiApiKeyMapper extends BaseMapperX<AiApiKey, AiApiKeyVO> {

    default IPage<AiApiKeyVO> selectPage(IPage<AiApiKey> page, AiApiKeyQuery query) {
        LambdaQueryWrapper<AiApiKey> wrapper = Wrappers.<AiApiKey>lambdaQuery()
                .like(StringUtils.isNotEmpty(query.getName()), AiApiKey::getName, query.getName())
                .eq(StringUtils.isNotEmpty(query.getPlatform()), AiApiKey::getPlatform, query.getPlatform())
                .eq(StringUtils.isNotNull(query.getStatus()), AiApiKey::getStatus, query.getStatus())
                .orderByDesc(AiApiKey::getCreateTime);
        return selectVoPage(page, wrapper);
    }

    default List<AiApiKeyVO> selectOptionList() {
        LambdaQueryWrapper<AiApiKey> wrapper = Wrappers.<AiApiKey>lambdaQuery()
                .select(AiApiKey::getId, AiApiKey::getName)
                .eq(AiApiKey::getStatus, DataStatus.OK.getCode());
        return selectVoList(wrapper);
    }

}