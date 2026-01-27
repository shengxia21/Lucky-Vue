package com.lucky.ai.util;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.lucky.ai.enums.model.AiPlatformEnum;
import com.lucky.common.utils.SecurityUtils;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.deepseek.DeepSeekAssistantMessage;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring AI 工具类
 *
 * @author lucky
 */
public class AiUtils {

    public static final String TOOL_CONTEXT_LOGIN_USER = "LOGIN_USER";

    public static ChatOptions buildChatOptions(AiPlatformEnum platform, String model, Double temperature, Integer maxTokens) {
        return buildChatOptions(platform, model, temperature, maxTokens, null, null);
    }

    public static ChatOptions buildChatOptions(AiPlatformEnum platform, String model, Double temperature, Integer maxTokens,
                                               List<ToolCallback> toolCallbacks, Map<String, Object> toolContext) {
        toolCallbacks = ObjUtil.defaultIfNull(toolCallbacks, Collections.emptyList());
        toolContext = ObjUtil.defaultIfNull(toolContext, Collections.emptyMap());
        return switch (platform) {
            case TONG_YI ->
                    DashScopeChatOptions.builder().model(model).temperature(temperature).maxToken(maxTokens)
                            .enableThinking(true)
                            .toolCallbacks(toolCallbacks).toolContext(toolContext).build();
            // 复用 DeepSeek 客户端
            case DEEP_SEEK, DOU_BAO, HUN_YUAN, SILICON_FLOW, XING_HUO ->
                    DeepSeekChatOptions.builder().model(model).temperature(temperature).maxTokens(maxTokens)
                            .toolCallbacks(toolCallbacks).toolContext(toolContext).build();
            case ZHI_PU -> ZhiPuAiChatOptions.builder().model(model).temperature(temperature).maxTokens(maxTokens)
                    .toolCallbacks(toolCallbacks).toolContext(toolContext).build();
            default -> throw new IllegalArgumentException(StrUtil.format("未知平台({})", platform));
        };
    }

    public static Message buildMessage(String type, String content) {
        if (MessageType.USER.getValue().equals(type)) {
            return new UserMessage(content);
        }
        if (MessageType.ASSISTANT.getValue().equals(type)) {
            return new AssistantMessage(content);
        }
        if (MessageType.SYSTEM.getValue().equals(type)) {
            return new SystemMessage(content);
        }
        if (MessageType.TOOL.getValue().equals(type)) {
            throw new UnsupportedOperationException("暂不支持 tool 消息：" + content);
        }
        throw new IllegalArgumentException(StrUtil.format("未知消息类型({})", type));
    }

    public static Map<String, Object> buildCommonToolContext() {
        Map<String, Object> context = new HashMap<>();
        context.put(TOOL_CONTEXT_LOGIN_USER, SecurityUtils.getLoginUser());
        return context;
    }

    public static String getChatResponseContent(ChatResponse response) {
        if (response == null) {
            return null;
        }
        return response.getResult().getOutput().getText();
    }

    public static String getChatResponseReasoningContent(ChatResponse response) {
        if (response == null) {
            return null;
        }
        if (response.getResult().getOutput() instanceof DeepSeekAssistantMessage) {
            return ((DeepSeekAssistantMessage) (response.getResult().getOutput())).getReasoningContent();
        } else {
            return (String) response.getResult().getOutput().getMetadata().getOrDefault("reasoningContent", "");
        }
    }

}
