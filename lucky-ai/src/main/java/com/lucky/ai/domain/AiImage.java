package com.lucky.ai.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lucky.common.annotation.Excel;
import com.lucky.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * AI 绘画对象 ai_image
 * 
 * @author lucky
 */
public class AiImage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 用户编号 */
    @Excel(name = "用户编号")
    private Long userId;

    /** 提示词 */
    @Excel(name = "提示词")
    private String prompt;

    /** 平台 */
    @Excel(name = "平台")
    private String platform;

    /** 模型编号 */
    @Excel(name = "模型编号")
    private Long modelId;

    /** 模型标识 */
    @Excel(name = "模型标识")
    private String model;

    /** 图片宽度 */
    @Excel(name = "图片宽度")
    private Long width;

    /** 图片高度 */
    @Excel(name = "图片高度")
    private Long height;

    /** 生成状态（10进行中 20已完成 30已失败） */
    @Excel(name = "生成状态", readConverterExp = "1=0进行中,2=0已完成,3=0已失败")
    private Long status;

    /** 完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date finishTime;

    /** 绘画错误信息 */
    @Excel(name = "绘画错误信息")
    private String errorMessage;

    /** 图片地址 */
    @Excel(name = "图片地址")
    private String picUrl;

    /** 是否公开（0否 1是） */
    @Excel(name = "是否公开", readConverterExp = "0=否,1=是")
    private Long publicStatus;

    /** 绘制参数 */
    @Excel(name = "绘制参数")
    private String options;

    /** mj buttons 按钮 */
    @Excel(name = "mj buttons 按钮")
    private String buttons;

    /** 任务编号 */
    @Excel(name = "任务编号")
    private String taskId;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatform() {
        return platform;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public void setWidth(Long width) {
        this.width = width;
    }

    public Long getWidth() {
        return width;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public Long getHeight() {
        return height;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getStatus() {
        return status;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPublicStatus(Long publicStatus) {
        this.publicStatus = publicStatus;
    }

    public Long getPublicStatus() {
        return publicStatus;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getOptions() {
        return options;
    }

    public void setButtons(String buttons) {
        this.buttons = buttons;
    }

    public String getButtons() {
        return buttons;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("prompt", getPrompt())
            .append("platform", getPlatform())
            .append("modelId", getModelId())
            .append("model", getModel())
            .append("width", getWidth())
            .append("height", getHeight())
            .append("status", getStatus())
            .append("finishTime", getFinishTime())
            .append("errorMessage", getErrorMessage())
            .append("picUrl", getPicUrl())
            .append("publicStatus", getPublicStatus())
            .append("options", getOptions())
            .append("buttons", getButtons())
            .append("taskId", getTaskId())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }

}
