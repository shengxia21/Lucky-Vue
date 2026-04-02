package com.lucky.ai.domain.query.chatRole;

import com.lucky.ai.domain.AiChatRole;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * AI 聊天角色新增/修改【我的】请求VO
 *
 * @author lucky
 */
@Data
@AutoMapper(target = AiChatRole.class, reverseConvertGenerate = false)
public class AiChatRoleSaveMyQuery {

    /**
     * 角色编号
     */
    private Long id;

    /**
     * 角色名称
     */
    @NotEmpty(message = "角色名称不能为空")
    private String name;

    /**
     * 角色头像
     */
    @NotEmpty(message = "角色头像不能为空")
    private String avatar;

    /**
     * 角色描述
     */
    @NotEmpty(message = "角色描述不能为空")
    private String description;

    /**
     * 角色设定
     */
    @NotEmpty(message = "角色设定不能为空")
    private String systemMessage;

    /**
     * 引用的知识库编号列表
     */
    private List<Long> knowledgeIds;

    /**
     * 引用的工具编号列表
     */
    private List<Long> toolIds;

    /**
     * 引用的 MCP Client 名字列表
     */
    private List<String> mcpClientNames;

}