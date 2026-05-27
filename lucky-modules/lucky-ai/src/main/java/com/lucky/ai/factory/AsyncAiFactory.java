package com.lucky.ai.factory;

import com.lucky.ai.core.context.ImageContext;
import com.lucky.ai.core.facade.ImageServiceFacade;
import com.lucky.ai.domain.AiChatMessage;
import com.lucky.ai.mapper.AiChatMessageMapper;
import com.lucky.common.core.utils.spring.SpringUtils;

import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 *
 * @author lucky
 */
public class AsyncAiFactory {

    /**
     * 执行绘制图片任务
     *
     * @param imageContext 图片上下文
     * @return 任务
     */
    public static TimerTask executeDrawImage(ImageContext imageContext) {
        return new TimerTask() {
            @Override
            public void run() {
                // 获取图片服务实例
                ImageServiceFacade imageService = SpringUtils.getBean(ImageServiceFacade.class);
                // 生成图片
                imageService.generateImage(imageContext);
            }
        };
    }

    /**
     * 执行更新assistant消息任务
     *
     * @param assistantId      assistant消息id
     * @param userName         用户名
     * @param content          消息内容
     * @param reasoningContent 推理内容
     * @return 任务
     */
    public static TimerTask updateAssistantMessage(Long assistantId, String userName, String content, String reasoningContent) {
        return new TimerTask() {
            @Override
            public void run() {
                AiChatMessage message = new AiChatMessage();
                message.setId(assistantId);
                message.setContent(content);
                message.setReasoningContent(reasoningContent);
                message.setUpdateBy(userName);
                SpringUtils.getBean(AiChatMessageMapper.class).updateById(message);
            }
        };
    }

    /**
     * 执行删除assistant消息任务
     *
     * @param assistantId assistant消息id
     * @return 任务
     */
    public static TimerTask deleteAssistantMessage(Long assistantId) {
        return new TimerTask() {
            @Override
            public void run() {
                SpringUtils.getBean(AiChatMessageMapper.class).deleteById(assistantId);
            }
        };
    }

}
