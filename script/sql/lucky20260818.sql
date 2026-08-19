/*
 Navicat Premium Data Transfer

 Source Server         : 本地连接8.0
 Source Server Type    : MySQL
 Source Server Version : 80042
 Source Host           : localhost:3308
 Source Schema         : lucky-vue

 Target Server Type    : MySQL
 Target Server Version : 80042
 File Encoding         : 65001

 Date: 18/08/2026 18:16:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ai_api_key
-- ----------------------------
DROP TABLE IF EXISTS `ai_api_key`;
CREATE TABLE `ai_api_key`  (
  `id` bigint(0) NOT NULL COMMENT '编号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `provider` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '提供商',
  `api_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密钥',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'API 地址',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `create_dept` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI API 秘钥表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ai_api_key
-- ----------------------------
INSERT INTO `ai_api_key` VALUES (1, 'DeepSeek', 'DeepSeek', 'sk-8dxx', 'https://api.deepseek.com', '0', '0', NULL, 'admin', '2026-01-27 12:19:44', '', NULL);
INSERT INTO `ai_api_key` VALUES (2, '通义千问', 'TongYi', 'sk-8dxx', 'https://dashscope.aliyuncs.com', '0', '0', NULL, 'admin', '2026-01-27 12:19:44', '', NULL);
INSERT INTO `ai_api_key` VALUES (5, '抖音豆包', 'DouBao', 'sk-8dxx', NULL, '0', '0', NULL, 'admin', '2026-01-27 12:19:44', '', NULL);
INSERT INTO `ai_api_key` VALUES (6, '腾讯混元', 'HunYuan', 'sk-8dxx', NULL, '0', '0', NULL, 'admin', '2026-01-27 12:19:44', '', NULL);
INSERT INTO `ai_api_key` VALUES (8, 'Kimi', 'Moonshot', 'sk-8dxx', NULL, '0', '0', NULL, 'admin', '2026-01-27 12:19:44', '', NULL);

-- ----------------------------
-- Table structure for ai_chat_conversation
-- ----------------------------
DROP TABLE IF EXISTS `ai_chat_conversation`;
CREATE TABLE `ai_chat_conversation`  (
  `id` bigint(0) NOT NULL COMMENT '编号',
  `user_id` bigint(0) NOT NULL COMMENT '用户编号',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对话标题',
  `pinned` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否置顶（0否 1是）',
  `role_id` bigint(0) NULL DEFAULT NULL COMMENT '聊天角色编号',
  `history_message_count` int(0) NOT NULL COMMENT '携带历史消息数',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `create_dept` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI 聊天对话表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ai_chat_conversation
-- ----------------------------

-- ----------------------------
-- Table structure for ai_chat_message
-- ----------------------------
DROP TABLE IF EXISTS `ai_chat_message`;
CREATE TABLE `ai_chat_message`  (
  `id` bigint(0) NOT NULL COMMENT '编号',
  `user_id` bigint(0) NOT NULL COMMENT '用户编号',
  `conversation_id` bigint(0) NOT NULL COMMENT '对话编号',
  `provider` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '提供商',
  `model` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型标志',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息类型',
  `system_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '系统消息',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '聊天内容',
  `reasoning_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '推理内容',
  `prompt_tokens` int(0) NULL DEFAULT NULL COMMENT '提示词 Token 数量',
  `completion_tokens` int(0) NULL DEFAULT NULL COMMENT '生成 Token 数量',
  `total_tokens` int(0) NULL DEFAULT NULL COMMENT '总 Token 数量',
  `segment_ids` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '知识库段落编号数组',
  `web_search_pages` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联网搜索的网页内容数组',
  `attachment_urls` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '附件 URL 数组',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `create_dept` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_conversation_id`(`conversation_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI 聊天消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ai_chat_message
-- ----------------------------

-- ----------------------------
-- Table structure for ai_chat_role
-- ----------------------------
DROP TABLE IF EXISTS `ai_chat_role`;
CREATE TABLE `ai_chat_role`  (
  `id` bigint(0) NOT NULL COMMENT '编号',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户编号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色头像',
  `persona` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色设定',
  `knowledge_ids` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '引用的知识库编号列表',
  `tool_ids` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '引用的工具编号列表',
  `mcp_client_names` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '引用的 MCP Client 名字列表',
  `is_public` tinyint(0) NOT NULL DEFAULT 1 COMMENT '是否公开（0否 1是）',
  `sort` int(0) NOT NULL COMMENT '排序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `create_dept` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI 聊天角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ai_chat_role
-- ----------------------------
INSERT INTO `ai_chat_role` VALUES (1, 1, '全栈开发人员', '/profile/upload/2026/01/28/002_20260128202911A001.jpg', '作为全栈Web开发人员，您的角色包括设计、开发和支持前端和后端Web应用程序。\n您应该具备HTML、CSS、JavaScript等技术的知识和经验，以及Python、Java、Ruby等后端编程语言的知识和经验。\n您还应该具备使用React、Angular、Vue.js、Express、Django、Next.js、Flask或Ruby on Rails等Web框架的经验。同时，具备数据库、应用架构、安全性、性能最佳实践、调试、故障排除和自动化测试的经验也非常重要。与其他开发人员、设计师和利益相关者合作对于创建用户友好的Web应用程序至关重要。', '', '', '', 1, 1, '0', '0', NULL, 'admin', '2026-01-27 11:38:50', '', NULL);
INSERT INTO `ai_chat_role` VALUES (2, 2, '全栈工程师 - F', '/profile/upload/2026/01/28/1697014140316_20260128203657A005.jpg', '指导方针\n1.沟通\n使用用户所要求的语言回复。\n仅讨论与编程相关的话题；礼貌地拒绝无关的查询。\n2.代码提供\n仅在请求时提供代码，并要求用户提供明确的规范（语言、框架和功能）。如果用户未提供足够的信息，拒绝回答。\n对于代码片段使用Markdown格式。\n所有代码示例默认使用TypeScript。\n使用TailwindCSS进行样式处理。\n3.特定技术要求\n使用Vue或Pinia时，采用组合API（即使用setup）\n在优化或修正代码时，仅输出修改的部分，并指明应插入的位置。\n对于Spring，除非明确要求，否则省略导入语句。\n对于.NET，除非明确要求，否则省略命名空间语句。', '', '', '', 0, 2, '0', '0', '105', 'lucky', '2026-01-27 11:38:53', '', NULL);
INSERT INTO `ai_chat_role` VALUES (20, NULL, '技术博客摘要专家', '/profile/upload/2026/03/15/shaonian_20260315105143A001.jpg', '你是谁\n你是一个技术专家，经常阅读各种技术博客，善于整理信息和总结。\n\n你要做什么\n接下来，用户将给你一篇博客文章，请你仔细阅读并理解其中的内容，梳理对应的关系，理清楚前后的逻辑，最终生成一段 200-250 字左右的摘要内容。\n\n要求\n以第一人称（笔者）来描述这段摘要的内容\n摘要文字须符合博客文章中作者的语气、风格、特性等\n以 Markdown 的格式返回最终的内容，比如可以包含列表、引用、换行、加粗、斜体等任何 Markdown 的格式\n摘要只需要文字，无需图片', NULL, NULL, NULL, 1, 4, '0', '0', NULL, 'admin', '2026-03-15 10:51:46', '', NULL);
INSERT INTO `ai_chat_role` VALUES (21, NULL, 'Node.js 优化师', '/profile/upload/2026/03/15/002_20260315105726A002.jpg', '我想让你充当 Node.js 工程师，帮助我修改和优化我的脚本。你将分析我的现有代码，提出改进建议，并提供优化后的代码示例。以下是一些具体任务示例：\n\n1.代码审查：检查我的 Node.js 代码，并指出存在的问题和改进空间。\n2.性能优化：识别代码中的性能瓶颈，并提供优化建议，例如减少不必要的计算、优化数据库查询、使用缓存等。\n3.异步编程：帮助将回调函数转换为使用 Promise 或 async/await 的异步代码，以提高代码的可读性和维护性。\n4.错误处理：改进错误处理机制，确保应用程序能够更稳健地处理异常情况。\n5.代码重构：重构代码以提高其结构、可读性和可维护性，遵循最佳实践和设计模式。\n6.依赖管理：检查并优化项目中的依赖项，确保使用最新的稳定版本，并移除不必要的依赖项。\n7.安全性增强：识别并修复代码中的安全漏洞，例如输入验证、身份验证和授权、敏感数据保护等。\n8.测试覆盖率：改进单元测试和集成测试的覆盖率，确保代码的可靠性和健壮性。\n9.文档编写：为现有代码编写详细的注释和文档，帮助其他开发人员理解和维护代码。\n通过详细的分析、改进建议和优化后的代码示例，你将帮助我提升 Node.js 脚本的性能、可靠性和可维护性。', NULL, NULL, NULL, 1, 3, '0', '0', NULL, 'admin', '2026-03-15 10:58:12', '', NULL);
INSERT INTO `ai_chat_role` VALUES (22, NULL, 'Emoji 生成', '/profile/upload/2026/03/15/001_20260315105839A003.jpeg', '你现在是一个 emoji 表情生成工具，无论我说什么，你都只回复我与内容重点最相关的 emoji 表情\n\n比如我说：绘画\n你则回复我：🎨', NULL, NULL, NULL, 1, 5, '0', '0', NULL, 'admin', '2026-03-15 10:59:03', '', NULL);

-- ----------------------------
-- Table structure for ai_image
-- ----------------------------
DROP TABLE IF EXISTS `ai_image`;
CREATE TABLE `ai_image`  (
  `id` bigint(0) NOT NULL COMMENT '编号',
  `user_id` bigint(0) NOT NULL COMMENT '用户编号',
  `provider` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '提供商',
  `model` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型标识',
  `prompt` varchar(1200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '提示词',
  `width` int(0) NOT NULL COMMENT '图片宽度',
  `height` int(0) NOT NULL COMMENT '图片高度',
  `generate_status` tinyint(0) NOT NULL DEFAULT 10 COMMENT '生成状态（10进行中 20已完成 30已失败）',
  `pic_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片地址',
  `finish_time` datetime(0) NULL DEFAULT NULL COMMENT '完成时间',
  `options` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '绘制参数',
  `error_message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '绘画错误信息',
  `is_public` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否公开（0否 1是）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `create_dept` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI 绘画表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ai_image
-- ----------------------------
INSERT INTO `ai_image` VALUES (21, 1, 'TongYi', 'qwen-image-plus', '一间有着精致窗户的花店，漂亮的木质门，摆放着花朵', 1328, 1328, 20, '/profile/drawImage/2025/12/04/8994a22c-1260-4112-981c-4696189dcb1d.png', '2025-12-04 02:38:32', '{}', NULL, 0, '0', NULL, 'admin', '2025-12-04 02:38:18', '', NULL);
INSERT INTO `ai_image` VALUES (22, 1, 'TongYi', 'qwen-image-plus', '国风水墨风格，一个长长黑发的男人，金色的发簪，飞舞着金色的蝴蝶，白色的服装，高细节，高质量，深蓝色背景，背景中有若隐若现的水墨竹林。', 1328, 1328, 20, '/profile/drawImage/2025/12/04/39831e0f-0996-47f1-af35-241d6b2cf4d6.png', '2025-12-04 02:45:00', '{}', NULL, 0, '0', NULL, 'admin', '2025-12-04 02:44:11', '', NULL);
INSERT INTO `ai_image` VALUES (23, 1, 'TongYi', 'qwen-image-plus', '近景镜头 | 近景镜头，18岁的中国女孩，古代服饰，圆脸，看着镜头，民族优雅的服装，商业摄影，室外，电影级光照，半身特写，精致的淡妆，锐利的边缘。', 1328, 1328, 20, '/profile/drawImage/2025/12/04/9a299072-848f-4d45-a98b-a2f93143934d.png', '2025-12-04 04:16:22', '{}', NULL, 0, '0', NULL, 'admin', '2025-12-04 04:16:08', '', NULL);
INSERT INTO `ai_image` VALUES (29, 1, 'TongYi', 'qwen-image-plus', '由羊毛毡制成的大熊猫，头戴大檐帽，穿着蓝色警服马甲，扎着腰带，携带警械装备，戴着蓝色手套，穿着皮鞋，大步奔跑姿态，毛毡效果，周围是动物王国城市街道商户，高级滤镜，路灯，动物王国，奇妙童趣，憨态可掬，夜晚，明亮，自然，可爱，4K，毛毡材质，摄影镜头，居中构图，毛毡风格，皮克斯风格，逆光。', 1328, 1328, 20, '/profile/drawImage/2025/12/04/251c8b7d-1255-4aa1-a1cf-8d550ede293d.png', '2025-12-04 04:58:26', '{}', NULL, 0, '0', NULL, 'admin', '2025-12-04 04:58:13', '', NULL);
INSERT INTO `ai_image` VALUES (31, 1, 'TongYi', 'qwen-image-plus', '航拍视角 | 展示了大雪，村庄，道路，灯火，树木。航拍视角，逼真效果。', 1328, 1328, 20, '/profile/drawImage/2025/12/04/df020aed-61da-4010-9d3b-d7e009086ace.png', '2025-12-04 05:21:06', '{}', NULL, 0, '0', NULL, 'admin', '2025-12-04 05:20:53', '', NULL);
INSERT INTO `ai_image` VALUES (37, 1, 'TongYi', 'qwen-image-plus', '远景镜头 | 展示了远景镜头，在壮丽的雪山背景下，两个小小的人影站在远处山顶，背对着镜头，静静地观赏着日落的美景。夕阳的余晖洒在雪山上，呈现出一片金黄色的光辉，与蔚蓝的天空形成鲜明对比。两人仿佛被这壮观的自然景象所吸引，整个画面充满了宁静与和谐。', 1328, 1328, 20, '/profile/drawImage/2025/12/04/8473059b-8860-4567-8d67-9cdddac76966.png', '2025-12-04 17:30:06', '{}', NULL, 1, '0', NULL, 'admin', '2025-12-04 17:29:23', '', NULL);
INSERT INTO `ai_image` VALUES (38, 2, 'TongYi', 'qwen-image-plus', '25岁中国女孩，圆脸，看着镜头，优雅的民族服装，商业摄影，室外，电影级光照，半身特写，精致的淡妆，锐利的边缘。', 1328, 1328, 20, '/profile/drawImage/2025/12/04/314a0626-f041-4fc3-9b3c-c92b5611977b.png', '2025-12-04 17:32:54', '{}', NULL, 1, '0', 105, 'lucky', '2025-12-04 17:32:41', '', NULL);
INSERT INTO `ai_image` VALUES (39, 2, 'TongYi', 'qwen-image-plus', '俯视视角 | 我从空中俯瞰冰湖，中心有一艘小船，周围环绕着漩涡图案和充满活力的蓝色海水。螺旋深渊，该场景是从上方以自上而下的视角拍摄的，展示了复杂的细节，例如表面的波纹和积雪覆盖的地面下的层。眺望冰冷的广阔天地。营造出一种令人敬畏的宁静感。', 1328, 1328, 20, '/profile/drawImage/2025/12/04/132f6f18-2d49-4e2b-be2b-6d946f522810.png', '2025-12-04 17:41:34', '{}', NULL, 1, '0', 105, 'lucky', '2025-12-04 17:41:20', '', NULL);
INSERT INTO `ai_image` VALUES (41, 2, 'TongYi', 'qwen-image-plus', '仰视视角 | 展示了热带地区的壮观景象，高大的椰子树如同参天巨人般耸立，枝叶茂盛，直指蓝天。镜头采用仰视视角，让观众仿佛置身树下，感受大自然的雄伟与生机。阳光透过树叶间隙洒落，形成斑驳光影，增添了几分神秘与浪漫。整个画面充满了热带风情，让人仿佛能闻到椰香，感受到微风拂面的惬意。', 1328, 1328, 20, '/profile/drawImage/2025/12/05/14a5db52-a28e-4931-9bdc-2b4456f20b75.png', '2025-12-05 03:41:41', '{}', NULL, 1, '0', 105, 'lucky', '2025-12-05 03:41:27', '', NULL);
INSERT INTO `ai_image` VALUES (42, 2, 'TongYi', 'qwen-image-plus', '超广角镜头 | 超广角镜头，碧海蓝天下的海岛，阳光透过树叶缝隙，洒下斑驳光影。', 1328, 1328, 20, '/profile/drawImage/2025/12/05/0d163307-087c-470b-b2f2-d019ddc9e503.png', '2025-12-05 06:00:07', '{}', NULL, 1, '0', 105, 'lucky', '2025-12-05 05:59:54', '', NULL);
INSERT INTO `ai_image` VALUES (43, 2, 'TongYi', 'qwen-image-plus', '深灰色大海中一条粉红色的发光河流，具有极简、美丽和审美的氛围，具有超现实风格的电影灯光。', 1328, 1328, 20, '/profile/drawImage/2025/12/10/a15a22da-cb5c-4c6c-95ec-48ddea5ba896.png', '2025-12-10 03:48:04', '{}', '', 1, '0', 105, 'lucky', '2025-12-10 03:47:50', '', NULL);
INSERT INTO `ai_image` VALUES (57, 2, 'TongYi', 'wan2.6-image', '一副典雅庄重的对联悬挂于厅堂之中，房间是个安静古典的中式布置，桌子上放着一些青花瓷，对联上左书“义本生知人机同道善思新”，右书“通云赋智乾坤启数高志远”， 横批“智启千问”，字体飘逸，在中间挂着一幅中国风的画作，内容是岳阳楼。', 1280, 1280, 20, '/profile/drawImage/2026/02/27/d8eecc76-1327-4979-a9d0-3ba28a8e013c.png', '2026-02-27 23:25:58', '{\"negativePrompt\":\"\",\"promptExtend\":\"false\"}', NULL, 0, '0', 105, 'lucky', '2026-02-27 23:21:54', '', NULL);
INSERT INTO `ai_image` VALUES (66, 2, 'TongYi', 'wan2.6-image', '采用近景特写镜头拍摄的东亚年轻女性，呈现户外雪地场景。她体型纤瘦，呈站立姿势，身体微微向右侧倾斜，头部抬起看向画面上方，姿态自然放松。她的面部是典型东亚长相，肤色白皙，脸颊带有自然的红润感，五官清秀：眼睛是深棕色，眼型偏圆，眼神略带惊讶地望向上方，眼白部分可见；眉毛是深黑色，形状自然弯长；鼻子小巧挺直，嘴唇涂有红色口红，唇瓣微张，表情带着轻微的惊讶或好奇。她的头发是深黑色长直发，发丝被风吹得略显凌乱，部分垂在脸颊两侧，头顶佩戴一顶深灰色的头盔，头盔边缘露出少量发丝。服装是蓝白拼接的厚重外套，外套材质看起来是毛绒与布料结合，显得温暖厚实，适合雪地环境。背景是被白雪覆盖的户外场景，远处可见模糊的树木轮廓，天空是明亮的浅蓝色，带有少量白云，光线是强烈的自然日光，照亮人物面部与头发，形成清晰的光影，色调以蓝、白、黑为主，整体风格清新自然。镜头的近景视角放大了人物的表情与细节，营造出户外雪地的真实氛围。', 1280, 1280, 20, '/profile/drawImage/2026/02/28/01c8c99b-53fe-4e82-8d76-3c572ca508c7.png', '2026-02-28 14:31:59', '{\"negativePrompt\":\"\",\"promptExtend\":\"false\"}', NULL, 0, '0', 105, 'lucky', '2026-02-28 14:27:56', '', NULL);

-- ----------------------------
-- Table structure for ai_model
-- ----------------------------
DROP TABLE IF EXISTS `ai_model`;
CREATE TABLE `ai_model`  (
  `id` bigint(0) NOT NULL COMMENT '编号',
  `key_id` bigint(0) NOT NULL COMMENT 'API 秘钥编号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型名称',
  `provider` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '提供商',
  `model` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型标志',
  `type` tinyint(0) NOT NULL DEFAULT 1 COMMENT '模型类型（1对话 2图片 3语音 4视频 5向量 6重排序）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `sort` int(0) NOT NULL COMMENT '排序',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `create_dept` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_key_id`(`key_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI 模型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ai_model
-- ----------------------------
INSERT INTO `ai_model` VALUES (1, 1, 'deepseek-flash', 'DeepSeek', 'deepseek-v4-flash', 1, '0', 1, '0', NULL, 'admin', '2026-01-27 12:18:31', '', NULL);
INSERT INTO `ai_model` VALUES (2, 1, 'deepseek-pro', 'DeepSeek', 'deepseek-v4-pro', 1, '0', 1, '0', NULL, 'admin', '2026-01-27 12:18:31', '', NULL);
INSERT INTO `ai_model` VALUES (3, 2, '通义千问-kimi-k2.7-code', 'TongYi', 'kimi-k2.7-code', 1, '0', 2, '0', NULL, 'admin', '2026-01-27 12:18:31', '', NULL);
INSERT INTO `ai_model` VALUES (4, 2, '通义万象-wan2.6-i2v-flash', 'TongYi', 'wan2.6-i2v-flash', 2, '0', 3, '0', NULL, 'admin', '2026-01-27 12:18:31', '', NULL);
INSERT INTO `ai_model` VALUES (5, 2, '通义千问-qwen-image-plus', 'TongYi', 'qwen-image-plus-2026-01-09', 2, '0', 3, '0', NULL, 'admin', '2026-01-27 12:18:31', '', NULL);
INSERT INTO `ai_model` VALUES (6, 5, '豆包-doubao-seed-1.6', 'DouBao', 'doubao-seed-1-6-251015', 1, '1', 6, '0', NULL, 'admin', '2026-01-27 12:18:31', '', NULL);
INSERT INTO `ai_model` VALUES (8, 6, '混元turbos', 'HunYuan', 'hunyuan-turbos-latest', 1, '1', 8, '0', NULL, 'admin', '2026-01-27 12:18:31', '', NULL);
INSERT INTO `ai_model` VALUES (10, 8, '月之暗面-kimi', 'Moonshot', 'kimi-k2-turbo-preview', 1, '1', 10, '0', NULL, 'admin', '2026-01-27 12:18:31', '', NULL);
INSERT INTO `ai_model` VALUES (15, 2, '通义千问-glm-5.2', 'TongYi', 'glm-5.2', 1, '0', 2, '0', NULL, 'admin', '2026-02-06 19:20:22', '', NULL);
INSERT INTO `ai_model` VALUES (16, 2, '通义千问-qwen3.7-max', 'TongYi', 'qwen3.7-max', 1, '0', 2, '0', NULL, 'admin', '2026-02-06 19:45:45', '', NULL);
INSERT INTO `ai_model` VALUES (17, 2, '通义千问-qwen3.7-plus', 'TongYi', 'qwen3.7-plus', 1, '0', 2, '0', NULL, 'admin', '2026-02-06 19:46:24', '', NULL);
INSERT INTO `ai_model` VALUES (19, 2, '通义千问-qwen-image-max', 'TongYi', 'qwen-image-max', 2, '1', 3, '0', NULL, 'admin', '2026-02-28 00:48:03', '', NULL);

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` bigint(0) NOT NULL COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `tpl_web_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '前端模板类型（element-ui模版 element-plus模版）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成功能作者',
  `form_col_num` int(0) NULL DEFAULT 1 COMMENT '表单布局（单列 双列 三列）',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '其它生成选项',
  `create_dept` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成业务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_table
-- ----------------------------

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` bigint(0) NOT NULL COMMENT '编号',
  `table_id` bigint(0) NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  `create_dept` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成业务表字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` bigint(0) NOT NULL COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_dept` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '参数配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config` VALUES (4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'false', 'Y', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (6, '用户登录-黑名单列表', 'sys.login.blackIPList', '', 'Y', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');
INSERT INTO `sys_config` VALUES (7, '用户管理-初始密码修改策略', 'sys.account.initPasswordModify', '1', 'Y', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '0：初始密码修改策略关闭，没有任何提示，1：提醒用户，如果未修改初始密码，则在登录时就会提醒修改密码对话框');
INSERT INTO `sys_config` VALUES (8, '用户管理-账号密码更新周期', 'sys.account.passwordValidateDays', '0', 'Y', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '密码更新周期（填写数字，数据初始化值为0不限制，若修改必须为大于0小于365的正整数），如果超过这个周期登录系统时，则在登录时就会提醒修改密码对话框');
INSERT INTO `sys_config` VALUES (9, '用户管理-密码字符范围', 'sys.account.chrtype', '0', 'Y', NULL, 'admin', '2026-04-17 23:34:24', '', NULL, '默认任意字符范围，0任意（密码可以输入任意字符），1数字（密码只能为0-9数字），2英文字母（密码只能为a-z和A-Z字母），3字母和数字（密码必须包含字母，数字）,4字母数字和特殊字符（目前支持的特殊字符包括：~!@#$%^&*()-=_+）');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint(0) NOT NULL COMMENT '部门id',
  `parent_id` bigint(0) NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int(0) NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `create_dept` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', '心云科技', 0, 'lucky', '15888888888', 'lucky@qq.com', '0', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL);
INSERT INTO `sys_dept` VALUES (101, 100, '0,100', '深圳总公司', 1, 'lucky', '15888888888', 'lucky@qq.com', '0', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL);
INSERT INTO `sys_dept` VALUES (102, 100, '0,100', '长沙分公司', 2, 'lucky', '15888888888', 'lucky@qq.com', '0', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL);
INSERT INTO `sys_dept` VALUES (103, 101, '0,100,101', '研发部门', 2, 'lucky', '15888888888', 'lucky@qq.com', '0', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL);
INSERT INTO `sys_dept` VALUES (104, 101, '0,100,101', '市场部门', 3, 'lucky', '15888888888', 'lucky@qq.com', '0', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL);
INSERT INTO `sys_dept` VALUES (105, 101, '0,100,101', '测试部门', 4, 'lucky', '15888888888', 'lucky@qq.com', '0', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL);
INSERT INTO `sys_dept` VALUES (106, 101, '0,100,101', '财务部门', 5, 'lucky', '15888888888', 'lucky@qq.com', '0', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL);
INSERT INTO `sys_dept` VALUES (107, 101, '0,100,101', '运维部门', 6, 'lucky', '15888888888', 'lucky@qq.com', '0', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL);
INSERT INTO `sys_dept` VALUES (108, 102, '0,100,102', '市场部门', 1, 'lucky', '15888888888', 'lucky@qq.com', '0', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL);
INSERT INTO `sys_dept` VALUES (109, 102, '0,100,102', '财务部门', 2, 'lucky', '15888888888', 'lucky@qq.com', '0', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint(0) NOT NULL COMMENT '字典编码',
  `dict_sort` int(0) NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_dept` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '默认分组');
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '系统分组');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '通知');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '公告');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '关闭状态');
INSERT INTO `sys_dict_data` VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '其他操作');
INSERT INTO `sys_dict_data` VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '新增操作');
INSERT INTO `sys_dict_data` VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '修改操作');
INSERT INTO `sys_dict_data` VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '删除操作');
INSERT INTO `sys_dict_data` VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '授权操作');
INSERT INTO `sys_dict_data` VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '导出操作');
INSERT INTO `sys_dict_data` VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '导入操作');
INSERT INTO `sys_dict_data` VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '强退操作');
INSERT INTO `sys_dict_data` VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '生成操作');
INSERT INTO `sys_dict_data` VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '清空操作');
INSERT INTO `sys_dict_data` VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (34, 5, '通义千问', 'TongYi', 'ai_provider', '', NULL, 'N', '0', NULL, 'admin', '2025-11-15 03:13:51', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (38, 9, 'DeepSeek', 'DeepSeek', 'ai_provider', '', NULL, 'N', '0', NULL, 'admin', '2025-11-15 03:13:51', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (39, 13, '智谱', 'ZhiPu', 'ai_provider', '', NULL, 'N', '0', NULL, 'admin', '2025-11-15 03:13:51', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (41, 10, '字节豆包', 'DouBao', 'ai_provider', '', NULL, 'N', '0', NULL, 'admin', '2025-11-15 03:13:51', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (42, 11, '腾讯混元', 'HunYuan', 'ai_provider', '', NULL, 'N', '0', NULL, 'admin', '2025-11-15 03:13:51', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (43, 12, '硅基流动', 'SiliconFlow', 'ai_provider', '', NULL, 'N', '0', NULL, 'admin', '2025-11-15 03:13:51', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (45, 15, '月之暗灭', 'Moonshot', 'ai_provider', '', NULL, 'N', '0', NULL, 'admin', '2025-11-15 03:13:51', '', NULL, '');
INSERT INTO `sys_dict_data` VALUES (47, 1, '进行中', '10', 'ai_image_generate_status', NULL, 'primary', 'N', '0', NULL, 'admin', '2025-12-09 02:26:57', '', NULL, '绘制中');
INSERT INTO `sys_dict_data` VALUES (48, 2, '已完成', '20', 'ai_image_generate_status', NULL, 'success', 'N', '0', NULL, 'admin', '2025-12-09 02:27:18', '', NULL, '绘制完成');
INSERT INTO `sys_dict_data` VALUES (49, 3, '已失败', '30', 'ai_image_generate_status', NULL, 'danger', 'N', '0', NULL, 'admin', '2025-12-09 02:27:39', '', NULL, '绘制失败');
INSERT INTO `sys_dict_data` VALUES (50, 1, '聊天', '1', 'ai_model_type', NULL, 'info', 'N', '0', NULL, 'admin', '2025-12-09 02:29:44', '', NULL, '聊天模型');
INSERT INTO `sys_dict_data` VALUES (51, 2, '图像', '2', 'ai_model_type', NULL, 'primary', 'N', '0', NULL, 'admin', '2025-12-09 02:30:16', '', NULL, '图像模型');
INSERT INTO `sys_dict_data` VALUES (52, 3, '音频', '3', 'ai_model_type', NULL, 'success', 'N', '0', NULL, 'admin', '2025-12-09 02:30:34', '', NULL, '音频模型');
INSERT INTO `sys_dict_data` VALUES (53, 4, '视频', '4', 'ai_model_type', NULL, 'warning', 'N', '0', NULL, 'admin', '2025-12-09 02:30:59', '', NULL, '视频模型');
INSERT INTO `sys_dict_data` VALUES (54, 5, '向量', '5', 'ai_model_type', NULL, 'danger', 'N', '0', NULL, 'admin', '2025-12-09 02:31:17', '', NULL, '向量模型');
INSERT INTO `sys_dict_data` VALUES (55, 6, '重排', '6', 'ai_model_type', NULL, 'danger', 'N', '0', NULL, 'admin', '2025-12-09 02:31:34', '', NULL, '重排模型');
INSERT INTO `sys_dict_data` VALUES (56, 1, '是', 'true', 'boolean_string', NULL, 'primary', 'Y', '0', NULL, 'admin', '2025-12-12 23:28:48', '', NULL, '是');
INSERT INTO `sys_dict_data` VALUES (57, 2, '否', 'false', 'boolean_string', NULL, 'danger', 'N', '0', NULL, 'admin', '2025-12-12 23:29:03', '', NULL, '否');
INSERT INTO `sys_dict_data` VALUES (58, 1, '文件系统', 'filesystem', 'ai_mcp_client_name', NULL, 'primary', 'N', '0', NULL, 'admin', '2026-01-28 20:23:12', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint(0) NOT NULL COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_dept` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '登录状态列表');
INSERT INTO `sys_dict_type` VALUES (11, 'AI 服务提供商', 'ai_provider', '0', NULL, 'admin', '2025-11-15 02:15:43', '', NULL, 'AI 服务提供商列表');
INSERT INTO `sys_dict_type` VALUES (12, 'AI 图片生成状态', 'ai_image_generate_status', '0', NULL, 'admin', '2025-12-09 02:24:51', '', NULL, 'AI 图片生成状态列表');
INSERT INTO `sys_dict_type` VALUES (13, 'AI 模型类型', 'ai_model_type', '0', NULL, 'admin', '2025-12-09 02:28:46', '', NULL, 'AI 模型类型列表');
INSERT INTO `sys_dict_type` VALUES (14, 'Bool是否类型', 'boolean_string', '0', NULL, 'admin', '2025-12-12 23:28:06', '', NULL, '是否类型列表');
INSERT INTO `sys_dict_type` VALUES (15, 'AI MCP 客户端名称', 'ai_mcp_client_name', '0', NULL, 'admin', '2026-01-28 20:22:46', '', NULL, 'AI MCP 客户端名称列表');

-- ----------------------------
-- Table structure for sys_login_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_info`;
CREATE TABLE `sys_login_info`  (
  `info_id` bigint(0) NOT NULL COMMENT '访问ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '提示消息',
  `login_time` datetime(0) NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `idx_sys_login_info_s`(`status`) USING BTREE,
  INDEX `idx_sys_login_info_lt`(`login_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统访问记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_login_info
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(0) NOT NULL COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(0) NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int(0) NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '路由名称',
  `is_frame` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_dept` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 1, 'system', NULL, '', '', '1', '0', 'M', '0', '0', '', 'system', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 2, 'monitor', NULL, '', '', '1', '0', 'M', '0', '0', '', 'monitor', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '系统监控目录');
INSERT INTO `sys_menu` VALUES (3, '系统工具', 0, 3, 'tool', NULL, '', '', '1', '0', 'M', '0', '0', '', 'tool', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '系统工具目录');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', '', '1', '0', 'C', '0', '0', 'system:user:list', 'user', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', '', '1', '0', 'C', '0', '0', 'system:role:list', 'peoples', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', '', '1', '0', 'C', '0', '0', 'system:menu:list', 'tree-table', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', '', '1', '0', 'C', '0', '0', 'system:dept:list', 'tree', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '部门管理菜单');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', '', '1', '0', 'C', '0', '0', 'system:post:list', 'post', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '岗位管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', '', '1', '0', 'C', '0', '0', 'system:dict:list', 'dict', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', '', '1', '0', 'C', '0', '0', 'system:config:list', 'edit', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '参数设置菜单');
INSERT INTO `sys_menu` VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', '', '1', '0', 'C', '0', '0', 'system:notice:list', 'message', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '通知公告菜单');
INSERT INTO `sys_menu` VALUES (108, '日志管理', 2, 5, 'log', '', '', '', '1', '0', 'M', '0', '0', '', 'log', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '日志管理菜单');
INSERT INTO `sys_menu` VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', '', '1', '0', 'C', '0', '0', 'monitor:online:list', 'online', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '在线用户菜单');
INSERT INTO `sys_menu` VALUES (112, '服务监控', 2, 2, 'server', 'monitor/server/index', '', '', '1', '0', 'C', '0', '0', 'monitor:server:list', 'server', NULL, 'admin', '2025-09-25 01:58:09', '', NULL, '服务监控菜单');
INSERT INTO `sys_menu` VALUES (114, '缓存列表', 2, 4, 'cacheList', 'monitor/cache/list', '', '', '1', '0', 'C', '0', '0', 'monitor:cache:list', 'redis-list', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '缓存列表菜单');
INSERT INTO `sys_menu` VALUES (116, '代码生成', 3, 1, 'gen', 'tool/gen/index', '', '', '1', '0', 'C', '0', '0', 'tool:gen:list', 'code', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '代码生成菜单');
INSERT INTO `sys_menu` VALUES (117, '系统接口', 3, 2, 'swagger', 'tool/swagger/index', '', '', '1', '0', 'C', '0', '0', 'tool:swagger:list', 'swagger', NULL, 'admin', '2025-10-17 02:00:00', '', NULL, '系统接口菜单');
INSERT INTO `sys_menu` VALUES (500, '操作日志', 108, 1, 'operLog', 'monitor/operLog/index', '', '', '1', '0', 'C', '0', '0', 'monitor:operLog:list', 'form', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '操作日志菜单');
INSERT INTO `sys_menu` VALUES (501, '登录日志', 108, 2, 'loginInfo', 'monitor/loginInfo/index', '', '', '1', '0', 'C', '0', '0', 'monitor:loginInfo:list', 'loginInfo', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '登录日志菜单');
INSERT INTO `sys_menu` VALUES (1000, '用户查询', 100, 1, '', '', '', '', '1', '0', 'F', '0', '0', 'system:user:query', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1001, '用户新增', 100, 2, '', '', '', '', '1', '0', 'F', '0', '0', 'system:user:add', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1002, '用户修改', 100, 3, '', '', '', '', '1', '0', 'F', '0', '0', 'system:user:edit', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1003, '用户删除', 100, 4, '', '', '', '', '1', '0', 'F', '0', '0', 'system:user:remove', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1004, '用户导出', 100, 5, '', '', '', '', '1', '0', 'F', '0', '0', 'system:user:export', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1005, '用户导入', 100, 6, '', '', '', '', '1', '0', 'F', '0', '0', 'system:user:import', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1006, '重置密码', 100, 7, '', '', '', '', '1', '0', 'F', '0', '0', 'system:user:resetPwd', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1007, '角色查询', 101, 1, '', '', '', '', '1', '0', 'F', '0', '0', 'system:role:query', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1008, '角色新增', 101, 2, '', '', '', '', '1', '0', 'F', '0', '0', 'system:role:add', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1009, '角色修改', 101, 3, '', '', '', '', '1', '0', 'F', '0', '0', 'system:role:edit', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1010, '角色删除', 101, 4, '', '', '', '', '1', '0', 'F', '0', '0', 'system:role:remove', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1011, '角色导出', 101, 5, '', '', '', '', '1', '0', 'F', '0', '0', 'system:role:export', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1012, '菜单查询', 102, 1, '', '', '', '', '1', '0', 'F', '0', '0', 'system:menu:query', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1013, '菜单新增', 102, 2, '', '', '', '', '1', '0', 'F', '0', '0', 'system:menu:add', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1014, '菜单修改', 102, 3, '', '', '', '', '1', '0', 'F', '0', '0', 'system:menu:edit', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1015, '菜单删除', 102, 4, '', '', '', '', '1', '0', 'F', '0', '0', 'system:menu:remove', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1016, '部门查询', 103, 1, '', '', '', '', '1', '0', 'F', '0', '0', 'system:dept:query', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1017, '部门新增', 103, 2, '', '', '', '', '1', '0', 'F', '0', '0', 'system:dept:add', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1018, '部门修改', 103, 3, '', '', '', '', '1', '0', 'F', '0', '0', 'system:dept:edit', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1019, '部门删除', 103, 4, '', '', '', '', '1', '0', 'F', '0', '0', 'system:dept:remove', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1020, '岗位查询', 104, 1, '', '', '', '', '1', '0', 'F', '0', '0', 'system:post:query', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1021, '岗位新增', 104, 2, '', '', '', '', '1', '0', 'F', '0', '0', 'system:post:add', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1022, '岗位修改', 104, 3, '', '', '', '', '1', '0', 'F', '0', '0', 'system:post:edit', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1023, '岗位删除', 104, 4, '', '', '', '', '1', '0', 'F', '0', '0', 'system:post:remove', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1024, '岗位导出', 104, 5, '', '', '', '', '1', '0', 'F', '0', '0', 'system:post:export', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1025, '字典查询', 105, 1, '#', '', '', '', '1', '0', 'F', '0', '0', 'system:dict:query', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1026, '字典新增', 105, 2, '#', '', '', '', '1', '0', 'F', '0', '0', 'system:dict:add', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1027, '字典修改', 105, 3, '#', '', '', '', '1', '0', 'F', '0', '0', 'system:dict:edit', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1028, '字典删除', 105, 4, '#', '', '', '', '1', '0', 'F', '0', '0', 'system:dict:remove', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1029, '字典导出', 105, 5, '#', '', '', '', '1', '0', 'F', '0', '0', 'system:dict:export', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1030, '参数查询', 106, 1, '#', '', '', '', '1', '0', 'F', '0', '0', 'system:config:query', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1031, '参数新增', 106, 2, '#', '', '', '', '1', '0', 'F', '0', '0', 'system:config:add', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1032, '参数修改', 106, 3, '#', '', '', '', '1', '0', 'F', '0', '0', 'system:config:edit', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1033, '参数删除', 106, 4, '#', '', '', '', '1', '0', 'F', '0', '0', 'system:config:remove', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1034, '参数导出', 106, 5, '#', '', '', '', '1', '0', 'F', '0', '0', 'system:config:export', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1035, '公告查询', 107, 1, '#', '', '', '', '1', '0', 'F', '0', '0', 'system:notice:query', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1036, '公告新增', 107, 2, '#', '', '', '', '1', '0', 'F', '0', '0', 'system:notice:add', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1037, '公告修改', 107, 3, '#', '', '', '', '1', '0', 'F', '0', '0', 'system:notice:edit', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1038, '公告删除', 107, 4, '#', '', '', '', '1', '0', 'F', '0', '0', 'system:notice:remove', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1039, '操作查询', 500, 1, '#', '', '', '', '1', '0', 'F', '0', '0', 'monitor:operLog:query', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1040, '操作删除', 500, 2, '#', '', '', '', '1', '0', 'F', '0', '0', 'monitor:operLog:remove', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1041, '日志导出', 500, 3, '#', '', '', '', '1', '0', 'F', '0', '0', 'monitor:operLog:export', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1042, '登录查询', 501, 1, '#', '', '', '', '1', '0', 'F', '0', '0', 'monitor:loginInfo:query', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1043, '登录删除', 501, 2, '#', '', '', '', '1', '0', 'F', '0', '0', 'monitor:loginInfo:remove', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1044, '日志导出', 501, 3, '#', '', '', '', '1', '0', 'F', '0', '0', 'monitor:loginInfo:export', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1045, '账户解锁', 501, 4, '#', '', '', '', '1', '0', 'F', '0', '0', 'monitor:loginInfo:unlock', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1046, '在线查询', 109, 1, '#', '', '', '', '1', '0', 'F', '0', '0', 'monitor:online:query', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1047, '批量强退', 109, 2, '#', '', '', '', '1', '0', 'F', '0', '0', 'monitor:online:batchLogout', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1048, '单条强退', 109, 3, '#', '', '', '', '1', '0', 'F', '0', '0', 'monitor:online:forceLogout', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1055, '生成查询', 116, 1, '#', '', '', '', '1', '0', 'F', '0', '0', 'tool:gen:query', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1056, '生成修改', 116, 2, '#', '', '', '', '1', '0', 'F', '0', '0', 'tool:gen:edit', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1057, '生成删除', 116, 3, '#', '', '', '', '1', '0', 'F', '0', '0', 'tool:gen:remove', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1058, '导入代码', 116, 4, '#', '', '', '', '1', '0', 'F', '0', '0', 'tool:gen:import', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1059, '预览代码', 116, 5, '#', '', '', '', '1', '0', 'F', '0', '0', 'tool:gen:preview', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1060, '生成代码', 116, 6, '#', '', '', '', '1', '0', 'F', '0', '0', 'tool:gen:code', '#', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1061, 'AI 大模型', 0, 4, 'ai', NULL, NULL, '', '1', '0', 'M', '0', '0', '', 'ai', NULL, 'admin', '2025-12-08 00:21:07', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1062, 'AI 对话', 1061, 1, 'chat', 'ai/chat/index', NULL, '', '1', '0', 'C', '0', '0', '', 'chat', NULL, 'admin', '2025-12-08 00:35:48', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1063, 'AI 绘画', 1061, 2, 'image', 'ai/image/index', NULL, '', '1', '0', 'C', '0', '0', '', 'image', NULL, 'admin', '2025-12-08 00:36:50', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1064, '控制台', 1061, 5, 'console', NULL, NULL, '', '1', '0', 'M', '0', '0', NULL, 'console', NULL, 'admin', '2025-12-08 00:41:30', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1065, '模型配置', 1064, 2, 'model', 'ai/console/model/index', NULL, '', '1', '0', 'C', '0', '0', 'ai:model:list', 'model', NULL, 'admin', '2025-12-08 00:43:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1066, '聊天管理', 1064, 3, 'chatManager', 'ai/console/chat/index', NULL, '', '1', '0', 'C', '0', '0', 'ai:chat-conversation:list', 'chatManager', NULL, 'admin', '2025-12-08 01:46:58', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1067, 'API 密钥', 1064, 1, 'api-key', 'ai/console/apiKey/index', NULL, '', '1', '0', 'C', '0', '0', 'ai:api-key:list', 'key', NULL, 'admin', '2025-12-08 01:55:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1068, '绘画管理', 1064, 4, 'imageManager', 'ai/console/image/index', NULL, '', '1', '0', 'C', '0', '0', 'ai:image:list', 'imageManager', NULL, 'admin', '2025-12-08 02:01:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1069, 'API 密钥查询', 1067, 1, '', NULL, NULL, '', '1', '0', 'F', '0', '0', 'ai:api-key:query', '#', NULL, 'admin', '2025-12-08 02:25:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1070, 'API 密钥创建', 1067, 2, '', NULL, NULL, '', '1', '0', 'F', '0', '0', 'ai:api-key:add', '#', NULL, 'admin', '2025-12-08 02:26:19', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1071, 'API 密钥更新', 1067, 3, '', NULL, NULL, '', '1', '0', 'F', '0', '0', 'ai:api-key:edit', '#', NULL, 'admin', '2025-12-08 02:26:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1072, 'API 密钥删除', 1067, 4, '', NULL, NULL, '', '1', '0', 'F', '0', '0', 'ai:api-key:remove', '#', NULL, 'admin', '2025-12-09 03:06:49', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1073, '模型配置查询', 1065, 1, '#', '', NULL, '', '1', '0', 'F', '0', '0', 'ai:model:query', '#', NULL, 'admin', '2025-12-10 18:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1074, '模型配置新增', 1065, 2, '#', '', NULL, '', '1', '0', 'F', '0', '0', 'ai:model:add', '#', NULL, 'admin', '2025-12-10 18:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1075, '模型配置修改', 1065, 3, '#', '', NULL, '', '1', '0', 'F', '0', '0', 'ai:model:edit', '#', NULL, 'admin', '2025-12-10 18:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1076, '模型配置删除', 1065, 4, '#', '', NULL, '', '1', '0', 'F', '0', '0', 'ai:model:remove', '#', NULL, 'admin', '2025-12-10 18:07:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1077, '绘图更新', 1068, 1, '', NULL, NULL, '', '1', '0', 'F', '0', '0', 'ai:image:edit', '#', NULL, 'admin', '2025-12-13 06:17:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1078, '绘画删除', 1068, 2, '', NULL, NULL, '', '1', '0', 'F', '0', '0', 'ai:image:remove', '#', NULL, 'admin', '2025-12-13 06:18:51', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1081, '聊天角色', 1064, 5, 'chatRole', 'ai/console/chatRole/index', NULL, '', '1', '0', 'C', '0', '0', 'ai:chat-role:list', 'chatRole', NULL, 'admin', '2026-01-27 22:25:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1082, '聊天角色查询', 1081, 1, '#', '', NULL, '', '1', '0', 'F', '0', '0', 'ai:chat-role:query', '#', NULL, 'admin', '2026-01-27 22:25:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1083, '聊天角色新增', 1081, 2, '#', '', NULL, '', '1', '0', 'F', '0', '0', 'ai:chat-role:add', '#', NULL, 'admin', '2026-01-27 22:25:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1084, '聊天角色修改', 1081, 3, '#', '', NULL, '', '1', '0', 'F', '0', '0', 'ai:chat-role:edit', '#', NULL, 'admin', '2026-01-27 22:25:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1085, '聊天角色删除', 1081, 4, '#', '', NULL, '', '1', '0', 'F', '0', '0', 'ai:chat-role:remove', '#', NULL, 'admin', '2026-01-27 22:25:08', '', NULL, '');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` bigint(0) NOT NULL COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob NULL COMMENT '公告内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_dept` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通知公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, '温馨提醒：2025-07-01 lucky新版本发布啦', '2', 0x3C703EE696B0E78988E69CACE58685E5AEB93C2F703E, '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '管理员');
INSERT INTO `sys_notice` VALUES (2, '维护通知：2025-07-01 lucky系统凌晨维护', '1', 0xE7BBB4E68AA4E58685E5AEB9, '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '管理员');

-- ----------------------------
-- Table structure for sys_notice_read
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice_read`;
CREATE TABLE `sys_notice_read`  (
  `read_id` bigint(0) NOT NULL COMMENT '已读主键',
  `notice_id` bigint(0) NOT NULL COMMENT '公告id',
  `user_id` bigint(0) NOT NULL COMMENT '用户id',
  `read_time` datetime(0) NOT NULL COMMENT '阅读时间',
  PRIMARY KEY (`read_id`) USING BTREE,
  UNIQUE INDEX `uk_user_notice`(`user_id`, `notice_id`) USING BTREE COMMENT '同一用户同一公告只记录一次'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '公告已读记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_notice_read
-- ----------------------------

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint(0) NOT NULL COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '模块标题',
  `business_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '返回参数',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint(0) NULL DEFAULT 0 COMMENT '消耗时间（单位：毫秒）',
  PRIMARY KEY (`oper_id`) USING BTREE,
  INDEX `idx_sys_oper_log_bt`(`business_type`) USING BTREE,
  INDEX `idx_sys_oper_log_s`(`status`) USING BTREE,
  INDEX `idx_sys_oper_log_ot`(`oper_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint(0) NOT NULL COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int(0) NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_dept` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '岗位信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'ceo', '董事长', 1, '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_post` VALUES (2, 'se', '项目经理', 2, '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_post` VALUES (3, 'hr', '人力资源', 3, '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');
INSERT INTO `sys_post` VALUES (4, 'user', '普通员工', 4, '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(0) NOT NULL COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(0) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `create_dept` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', 1, 1, '0', '0', '105', 'admin', '2025-08-17 23:07:13', '', NULL, '普通角色');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint(0) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(0) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色和部门关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES (2, 100);
INSERT INTO `sys_role_dept` VALUES (2, 101);
INSERT INTO `sys_role_dept` VALUES (2, 105);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint(0) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(0) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (2, 3);
INSERT INTO `sys_role_menu` VALUES (2, 100);
INSERT INTO `sys_role_menu` VALUES (2, 101);
INSERT INTO `sys_role_menu` VALUES (2, 102);
INSERT INTO `sys_role_menu` VALUES (2, 103);
INSERT INTO `sys_role_menu` VALUES (2, 104);
INSERT INTO `sys_role_menu` VALUES (2, 105);
INSERT INTO `sys_role_menu` VALUES (2, 106);
INSERT INTO `sys_role_menu` VALUES (2, 107);
INSERT INTO `sys_role_menu` VALUES (2, 108);
INSERT INTO `sys_role_menu` VALUES (2, 109);
INSERT INTO `sys_role_menu` VALUES (2, 112);
INSERT INTO `sys_role_menu` VALUES (2, 114);
INSERT INTO `sys_role_menu` VALUES (2, 116);
INSERT INTO `sys_role_menu` VALUES (2, 117);
INSERT INTO `sys_role_menu` VALUES (2, 500);
INSERT INTO `sys_role_menu` VALUES (2, 501);
INSERT INTO `sys_role_menu` VALUES (2, 1000);
INSERT INTO `sys_role_menu` VALUES (2, 1001);
INSERT INTO `sys_role_menu` VALUES (2, 1002);
INSERT INTO `sys_role_menu` VALUES (2, 1003);
INSERT INTO `sys_role_menu` VALUES (2, 1004);
INSERT INTO `sys_role_menu` VALUES (2, 1005);
INSERT INTO `sys_role_menu` VALUES (2, 1006);
INSERT INTO `sys_role_menu` VALUES (2, 1007);
INSERT INTO `sys_role_menu` VALUES (2, 1008);
INSERT INTO `sys_role_menu` VALUES (2, 1009);
INSERT INTO `sys_role_menu` VALUES (2, 1010);
INSERT INTO `sys_role_menu` VALUES (2, 1011);
INSERT INTO `sys_role_menu` VALUES (2, 1012);
INSERT INTO `sys_role_menu` VALUES (2, 1013);
INSERT INTO `sys_role_menu` VALUES (2, 1014);
INSERT INTO `sys_role_menu` VALUES (2, 1015);
INSERT INTO `sys_role_menu` VALUES (2, 1016);
INSERT INTO `sys_role_menu` VALUES (2, 1017);
INSERT INTO `sys_role_menu` VALUES (2, 1018);
INSERT INTO `sys_role_menu` VALUES (2, 1019);
INSERT INTO `sys_role_menu` VALUES (2, 1020);
INSERT INTO `sys_role_menu` VALUES (2, 1021);
INSERT INTO `sys_role_menu` VALUES (2, 1022);
INSERT INTO `sys_role_menu` VALUES (2, 1023);
INSERT INTO `sys_role_menu` VALUES (2, 1024);
INSERT INTO `sys_role_menu` VALUES (2, 1025);
INSERT INTO `sys_role_menu` VALUES (2, 1026);
INSERT INTO `sys_role_menu` VALUES (2, 1027);
INSERT INTO `sys_role_menu` VALUES (2, 1028);
INSERT INTO `sys_role_menu` VALUES (2, 1029);
INSERT INTO `sys_role_menu` VALUES (2, 1030);
INSERT INTO `sys_role_menu` VALUES (2, 1031);
INSERT INTO `sys_role_menu` VALUES (2, 1032);
INSERT INTO `sys_role_menu` VALUES (2, 1033);
INSERT INTO `sys_role_menu` VALUES (2, 1034);
INSERT INTO `sys_role_menu` VALUES (2, 1035);
INSERT INTO `sys_role_menu` VALUES (2, 1036);
INSERT INTO `sys_role_menu` VALUES (2, 1037);
INSERT INTO `sys_role_menu` VALUES (2, 1038);
INSERT INTO `sys_role_menu` VALUES (2, 1039);
INSERT INTO `sys_role_menu` VALUES (2, 1040);
INSERT INTO `sys_role_menu` VALUES (2, 1041);
INSERT INTO `sys_role_menu` VALUES (2, 1042);
INSERT INTO `sys_role_menu` VALUES (2, 1043);
INSERT INTO `sys_role_menu` VALUES (2, 1044);
INSERT INTO `sys_role_menu` VALUES (2, 1045);
INSERT INTO `sys_role_menu` VALUES (2, 1046);
INSERT INTO `sys_role_menu` VALUES (2, 1047);
INSERT INTO `sys_role_menu` VALUES (2, 1048);
INSERT INTO `sys_role_menu` VALUES (2, 1055);
INSERT INTO `sys_role_menu` VALUES (2, 1056);
INSERT INTO `sys_role_menu` VALUES (2, 1057);
INSERT INTO `sys_role_menu` VALUES (2, 1058);
INSERT INTO `sys_role_menu` VALUES (2, 1059);
INSERT INTO `sys_role_menu` VALUES (2, 1060);
INSERT INTO `sys_role_menu` VALUES (2, 1061);
INSERT INTO `sys_role_menu` VALUES (2, 1062);
INSERT INTO `sys_role_menu` VALUES (2, 1063);
INSERT INTO `sys_role_menu` VALUES (2, 1064);
INSERT INTO `sys_role_menu` VALUES (2, 1065);
INSERT INTO `sys_role_menu` VALUES (2, 1066);
INSERT INTO `sys_role_menu` VALUES (2, 1067);
INSERT INTO `sys_role_menu` VALUES (2, 1068);
INSERT INTO `sys_role_menu` VALUES (2, 1069);
INSERT INTO `sys_role_menu` VALUES (2, 1070);
INSERT INTO `sys_role_menu` VALUES (2, 1072);
INSERT INTO `sys_role_menu` VALUES (2, 1073);
INSERT INTO `sys_role_menu` VALUES (2, 1074);
INSERT INTO `sys_role_menu` VALUES (2, 1075);
INSERT INTO `sys_role_menu` VALUES (2, 1076);
INSERT INTO `sys_role_menu` VALUES (2, 1077);
INSERT INTO `sys_role_menu` VALUES (2, 1078);
INSERT INTO `sys_role_menu` VALUES (2, 1081);
INSERT INTO `sys_role_menu` VALUES (2, 1082);
INSERT INTO `sys_role_menu` VALUES (2, 1083);
INSERT INTO `sys_role_menu` VALUES (2, 1084);
INSERT INTO `sys_role_menu` VALUES (2, 1085);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `dept_id` bigint(0) NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `phone_number` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `pwd_update_date` datetime(0) NULL DEFAULT NULL COMMENT '密码最后更新时间',
  `create_dept` bigint(0) NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, NULL, 'admin', '心云', '00', 'lucky@163.com', '15888888888', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', NULL, '2025-08-17 23:07:13', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '管理员');
INSERT INTO `sys_user` VALUES (2, 105, 'lucky', '幸运', '00', 'lucky@qq.com', '15666666666', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', NULL, '2025-08-17 23:07:13', NULL, 'admin', '2025-08-17 23:07:13', '', NULL, '系统测试用户');
INSERT INTO `sys_user` VALUES (3, 105, 'xiaowu', '小吴', '00', 'lucky999@qq.com', '15999999999', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', NULL, '2026-01-07 21:47:21', NULL, 'admin', '2026-01-07 21:47:21', '', NULL, '新建用户');

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `post_id` bigint(0) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, 1);
INSERT INTO `sys_user_post` VALUES (2, 2);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `role_id` bigint(0) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户和角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2);

SET FOREIGN_KEY_CHECKS = 1;
