package com.lucky.ai.factory;

import com.lucky.ai.core.context.ImageContext;
import com.lucky.ai.core.facade.ImageServiceFacade;
import com.lucky.common.utils.spring.SpringUtils;

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

}
