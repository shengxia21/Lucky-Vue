package com.lucky.common.ai.image;

/**
 * 图片生成任务持久化处理器
 * <p>
 * 由业务模块实现，用于将图片生成结果持久化到数据库。
 *
 * @author lucky
 */
public interface ImagePersistenceHandler {

    /**
     * 图片生成成功回调
     *
     * @param imageId  图片ID
     * @param filePath 图片地址
     */
    void onSuccess(Long imageId, String filePath);

    /**
     * 图片生成失败回调
     *
     * @param imageId      图片ID
     * @param errorMessage 错误信息
     */
    void onFailure(Long imageId, String errorMessage);

}
