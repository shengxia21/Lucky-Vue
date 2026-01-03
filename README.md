<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">lucky v1.0.0</h1>
<h4 align="center">基于SpringBoot+Vue前后端分离的Java快速开发框架</h4>
<p align="center">
	<a href="https://gitee.com/fushengxuyu/lucky-vue/stargazers"><img src="https://gitee.com/fushengxuyu/lucky-vue/badge/star.svg?theme=dark"></a>
	<a href="https://gitee.com/fushengxuyu/lucky-vue"><img src="https://img.shields.io/badge/lucky-v1.0.0-brightgreen.svg"></a>
	<a href="https://gitee.com/fushengxuyu/lucky-vue/blob/master/LICENSE"><img src="https://img.shields.io/github/license/mashape/apistatus.svg"></a>
</p>
<p align="center">
    <img src="https://img.shields.io/badge/Spring%20Boot-3.5.8-blue.svg" alt="Downloads">
    <img src="https://img.shields.io/badge/Vue-3.5-blue.svg" alt="Downloads">
</p>

## 平台简介

Lucky-Vue是一套专注于AI大模型应用开发的开源项目。

* 前端采用Vue3、Element Plus。
* 后端采用Spring Boot、Spring Security、Redis & Jwt。
* 集成多款主流AI大模型，支持DeepSeek、通义千问、抖音豆包、腾讯混元等。
* 提供完整的AI对话、绘画、知识库等核心功能模块。

## 内置功能

1.  用户管理：用户是系统操作者，该功能主要完成系统用户配置。
2.  部门管理：配置系统组织机构（公司、部门、小组），树结构展现支持数据权限。
3.  岗位管理：配置系统用户所属担任职务。
4.  菜单管理：配置系统菜单，操作权限，按钮权限标识等。
5.  角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
6.  字典管理：对系统中经常使用的一些较为固定的数据进行维护。
7.  参数管理：对系统动态配置常用参数。
8.  通知公告：系统通知公告信息发布维护。
9.  操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
10. 登录日志：系统登录日志记录查询包含登录异常。
11. 在线用户：当前系统中活跃用户状态监控。
12. 代码生成：前后端代码的生成（java、html、xml、sql）支持CRUD下载 。
13. 系统接口：根据业务代码自动生成相关的api接口文档。
14. 服务监控：监视当前系统CPU、内存、磁盘、堆栈等相关信息。
15. 缓存监控：对系统的缓存信息查询，命令统计等。
16. 连接池监视：监视当前系统数据库连接池状态，可进行分析SQL找出系统性能瓶颈。
17. AI 对话：集成AI对话功能，用户可以与系统进行自然语言交互。
18. AI 绘画：集成AI绘画功能，用户可以通过自然语言描述生成对应的图片。(待实现)
19. API 密钥：配置自己的API密钥，以使用AI功能。
20. 模型配置：配置使用的AI模型，如DeepSeek、通义千问等。
21. 聊天管理：查看和管理AI对话的聊天记录。
22. 绘画管理：查看和管理AI生成的绘画记录。

## 后续实现功能

1. AI聊天角色：配置AI聊天角色，每个角色拥有自己的系统提示词。
2. 文件上传：AI对话聊天支持用户上传文件、图片等。
3. 联网功能：集成大模型联网搜索功能，联网后AI模型可以根据网络搜索结果进行回答。
4. AI思维导图：集成AI思维导图功能，用户可以通过自然语言描述生成对应的思维导图。
5. AI知识库：配置AI知识库，AI大模型会根据知识库的内容进行智能问答。
6. 视频创作：集成AI视频，用户可以通过自然语言描述生成对应的视频。
7. RAG功能：集成RAG功能，提供应用场景。
8. MCP功能：集成MCP功能，提供应用场景。
9. Agent功能：集成Agent功能，提供应用场景。

## AI技术说明及区别

| 技术名称 | 核心角色 / 比喻 | 解决的主要问题 | 典型应用场景 |
|----------|-----------------|----------------|-------------|
| RAG | 知识管家 | 大模型知识陈旧、易产生"幻觉"（胡说八道）。 | 企业智能客服、法律/医疗知识问答、内部文档查询。 |
| MCP | 万能转换器 / 统一插座 | 大模型与外部工具、数据源连接困难，接口不统一。 | 让Agent能一键调用浏览器查资料、读取Excel表格、发送邮件等。 |
| Agent | 智能助理 | 大模型只能被动回答，无法主动规划并执行复杂任务。 | 自动旅行规划、智能编码助手、自动化数据分析与报告生成。 |

- RAG：为AI注入最新、可信的知识
- MCP：打破AI与外部工具的壁垒
- Agent：让AI从“答”到“做”

在实际应用中，这三项技术往往协同工作，组成强大的AI应用“黄金三角”：
- Agent作为“大脑”和“指挥者”，负责规划任务流程。
- MCP作为“手和脚”，为Agent提供统一的方式来调用各种外部工具。
- RAG作为“记忆库”，为决策和生成提供精准、最新的知识支持。

## 在线体验

- 演示账号：lucky
- 演示密码：admin123

演示地址：https://yuanlin.asia

## 演示图

<table>
    <tr>
        <td><img src=".image/AI对话1.png"/></td>
        <td><img src=".image/AI对话2.png"/></td>
    </tr>
    <tr>
        <td><img src=".image/API密钥.png"/></td>
        <td><img src=".image/模型配置.png"/></td>
    </tr>
    <tr>
        <td><img src=".image/聊天管理-对话列表.png"/></td>
        <td><img src=".image/聊天管理-消息列表.png"/></td>
    </tr>
    <tr>
        <td><img src=".image/绘画管理.png"/></td>
    </tr>
</table>

## 特此声明

本项目是基于[RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue)项目进行的二次开发，感谢作者提供的优秀代码。
