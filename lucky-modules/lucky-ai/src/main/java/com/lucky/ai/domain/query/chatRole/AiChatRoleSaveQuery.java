package com.lucky.ai.domain.query.chatRole;

import com.lucky.ai.domain.AiChatRole;
import com.lucky.common.ai.enums.AiStatusEnum;
import com.lucky.common.core.enumeration.InEnum;
import com.lucky.common.core.validate.Update;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * AI 聊天角色新增/修改请求对象
 *
 * @author lucky
 */
@Data
@AutoMapper(target = AiChatRole.class, reverseConvertGenerate = false)
public class AiChatRoleSaveQuery {

    /**
     * 角色编号
     */
    @NotNull(message = "角色编号不能为空", groups = {Update.class})
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

    /**
     * 是否公开
     */
    @NotNull(message = "是否公开不能为空")
    private Boolean publicStatus;

    /**
     * 角色排序
     */
    @NotNull(message = "角色排序不能为空")
    private Integer sort;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空")
    @InEnum(AiStatusEnum.class)
    private Integer status;

}