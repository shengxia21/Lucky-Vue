package com.lucky.ai.factory;

import com.lucky.ai.core.context.ImageContext;
import com.lucky.ai.core.facade.ImageServiceFacade;
import com.lucky.common.utils.spring.SpringUtils;

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
    public static Runnable executeDrawImage(ImageContext imageContext) {
        return () -> {
            // 获取图片服务实例
            ImageServiceFacade imageService = SpringUtils.getBean(ImageServiceFacade.class);
            // 生成图片
            imageService.generateImage(imageContext);
        };
    }

}
