package com.lucky.ai.core.websearch;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * AI 联网搜索请求
 *
 * @author lucky
 */
public class AiWebSearchRequest {

    /**
     * 用户的搜索词
     */
    @NotEmpty(message = "搜索词不能为空")
    private String query;

    /**
     * 是否显示文本摘要
     * <p>
     * true - 显示
     * false - 不显示（默认）
     */
    private Boolean summary;

    /**
     * 返回结果的条数
     */
    @NotNull(message = "返回结果条数不能为空")
    @Min(message = "返回结果条数最小为 1", value = 1)
    @Max(message = "返回结果条数最大为 50", value = 50)
    private Integer count;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Boolean getSummary() {
        return summary;
    }

    public void setSummary(Boolean summary) {
        this.summary = summary;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
