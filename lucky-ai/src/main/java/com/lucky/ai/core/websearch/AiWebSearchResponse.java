package com.lucky.ai.core.websearch;

import java.util.List;

/**
 * AI 联网搜索响应
 *
 * @author lucky
 */
public class AiWebSearchResponse {

    /**
     * 总数（总共匹配的网页数）
     */
    private Long total;

    /**
     * 数据列表
     */
    private List<WebPage> lists;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<WebPage> getLists() {
        return lists;
    }

    public void setLists(List<WebPage> lists) {
        this.lists = lists;
    }

    /**
     * 网页对象
     */
    public static class WebPage {

        /**
         * 名称
         * <p>
         * 例如说：搜狐网
         */
        private String name;

        /**
         * 图标
         */
        private String icon;

        /**
         * 标题
         * <p>
         * 例如说：186页|阿里巴巴：2024年环境、社会和治理（ESG）报告
         */
        private String title;

        /**
         * URL
         * <p>
         * 例如说：https://m.sohu.com/a/815036254_121819701/?pvid=000115_3w_a
         */
        @SuppressWarnings("JavadocLinkAsPlainText")
        private String url;

        /**
         * 内容的简短描述
         */
        private String snippet;

        /**
         * 内容的文本摘要
         */
        private String summary;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSnippet() {
            return snippet;
        }

        public void setSnippet(String snippet) {
            this.snippet = snippet;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

    }

}
