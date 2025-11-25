package com.lucky.ai.factory;

import com.lucky.ai.enums.model.AiPlatformEnum;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.image.ImageModel;

/**
 * AI Model 模型工厂的接口类
 *
 * @author lucky
 */
public interface AiModelFactory {

    /**
     * 基于指定配置，获得 ChatModel 对象
     * <p>
     * 如果不存在，则进行创建
     *
     * @param platform 平台
     * @param apiKey   API KEY
     * @param url      API URL
     * @return ChatModel 对象
     */
    ChatModel getOrCreateChatModel(AiPlatformEnum platform, String apiKey, String url);

    /**
     * 基于默认配置，获得 ChatModel 对象
     * <p>
     * 默认配置，指的是在 application.yaml 配置文件中的 spring.ai 相关的配置
     *
     * @param platform 平台
     * @return ChatModel 对象
     */
    ChatModel getDefaultChatModel(AiPlatformEnum platform);

    /**
     * 基于默认配置，获得 ImageModel 对象
     * <p>
     * 默认配置，指的是在 application.yaml 配置文件中的 spring.ai 相关的配置
     *
     * @param platform 平台
     * @return ImageModel 对象
     */
    ImageModel getDefaultImageModel(AiPlatformEnum platform);

    /**
     * 基于指定配置，获得 ImageModel 对象
     * <p>
     * 如果不存在，则进行创建
     *
     * @param platform 平台
     * @param apiKey   API KEY
     * @param url      API URL
     * @return ImageModel 对象
     */
    ImageModel getOrCreateImageModel(AiPlatformEnum platform, String apiKey, String url);

}
