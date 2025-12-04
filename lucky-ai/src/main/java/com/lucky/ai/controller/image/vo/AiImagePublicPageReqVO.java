package com.lucky.ai.controller.image.vo;

/**
 * 公开的绘图分页查询VO
 *
 * @author lucky
 */
public class AiImagePublicPageReqVO {

    /**
     * 提示词
     */
    private String prompt;

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

}
