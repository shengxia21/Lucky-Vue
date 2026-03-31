package com.lucky.ai.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lucky.ai.controller.model.vo.apikey.AiApiKeyPageReqVO;
import com.lucky.ai.domain.AiApiKey;
import com.lucky.common.utils.StringUtils;

/**
 * AI API 秘钥Mapper接口
 *
 * @author lucky
 */
public interface AiApiKeyMapper extends BaseMapper<AiApiKey> {

    default IPage<AiApiKey> selectPage(IPage<AiApiKey> page, AiApiKeyPageReqVO pageReqVO) {
        LambdaQueryWrapper<AiApiKey> wrapper = Wrappers.<AiApiKey>lambdaQuery()
                .like(StringUtils.isNotEmpty(pageReqVO.getName()), AiApiKey::getName, pageReqVO.getName())
                .eq(StringUtils.isNotEmpty(pageReqVO.getPlatform()), AiApiKey::getPlatform, pageReqVO.getPlatform())
                .eq(StringUtils.isNotNull(pageReqVO.getStatus()), AiApiKey::getStatus, pageReqVO.getStatus())
                .orderByDesc(AiApiKey::getCreateTime);
        return selectPage(page, wrapper);
    }

}