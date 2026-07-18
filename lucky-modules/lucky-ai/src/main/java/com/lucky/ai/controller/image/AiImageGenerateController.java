package com.lucky.ai.controller.image;

import com.lucky.ai.domain.query.image.ImageQuery;
import com.lucky.ai.service.IAiImageGenerateService;
import com.lucky.common.core.domain.R;
import com.lucky.common.mybatis.core.controller.BaseController;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AI 图片生成Controller
 *
 * @author lucky
 */
@RestController
@RequestMapping("/ai/image")
public class AiImageGenerateController extends BaseController {

    @Resource
    private IAiImageGenerateService imageGenerateService;

    /**
     * 生成图片
     */
    @PostMapping("/generate")
    public R<Void> generateImage(@Validated @RequestBody ImageQuery query) {
        return toAjax(imageGenerateService.generateImage(query));
    }

}
