package com.lucky.common.mybatis.core.controller;

import com.fhs.trans.service.impl.TransService;
import com.lucky.common.core.domain.R;
import com.lucky.common.core.utils.StringUtils;
import com.lucky.common.core.utils.spring.SpringUtils;

/**
 * web层通用数据处理
 *
 * @author lucky
 */
public class BaseController {

    public final TransService transService = SpringUtils.getBean(TransService.class);

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected R<Void> toAjax(int rows) {
        return rows > 0 ? R.ok() : R.fail();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected R<Void> toAjax(boolean result) {
        return result ? R.ok() : R.fail();
    }

    /**
     * 页面跳转
     */
    public String redirect(String url) {
        return StringUtils.format("redirect:{}", url);
    }

}
