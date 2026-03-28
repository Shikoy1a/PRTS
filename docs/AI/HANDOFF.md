# HANDOFF LOG

> 用途：跨会话、跨 AI 的最小必要交接记录。
> 规则：每次开发结束后追加，不要覆盖历史。

## 2026-03-28
### 会话目标
- 建立统一 AI 协作与记忆机制，降低换会话/换模型的上下文丢失。

### 已完成
- 新建 AGENTS 入口与 AI 工作流文档。
- 汇总当前实现状态、差距和风险到 PROJECT_CONTEXT。

### 关键发现
- 内存索引检索结构已存在（Trie + NGram）。
- 路线规划核心算法已实现（图 + Dijkstra）。
- 自动化测试缺失。
- 数据备份恢复/AIGC动画/压缩存储尚未落地。

### 下次优先任务（建议）
1. 建立后端 `src/test` 基础测试骨架，先覆盖 Route/Food/Diary 关键路径。
2. 修复配置安全问题：移除明文数据库密码，改环境变量。
3. 补齐开发日志，保证文档进度和代码进度一致。

### 变更文件
- AGENTS.md
- docs/AI/PROJECT_CONTEXT.md
- docs/AI/WORKFLOW.md
- docs/AI/HANDOFF.md

## 2026-03-28（约束澄清补充）
### 会话目标
- 将“数据库仅持久化、运行时内存处理”与“核心模块禁用 Java 内置集合实现”明确写入项目文档。

### 已完成
- 在需求文档（根目录与 docs 目录）补充课程实现约束与数据库职责约束。
- 在开发规范文档（根目录与 docs 目录）补充“数据结构实现约束（课程要求）”。
- 在技术设计文档（根目录与 docs 目录）补充课程约束说明。
- 在 AI 协作文档（AGENTS/WORKFLOW/PROJECT_CONTEXT）同步新增硬约束。

### 关键决策
- 数据库定位为持久化层，不承担核心检索/匹配/排序/关联计算。
- 课程考核范围内的核心数据结构与算法模块，禁止直接使用 Java 内置集合实现作为最终实现。

### 变更文件
- Requirements Documendation.md
- docs/Requirements/Requirements Documendation.md
- Specification.md
- docs/Development Specification/Specification.md
- Technical Design Document.md
- docs/Tech/Technical Design Document.md
- AGENTS.md
- docs/AI/WORKFLOW.md
- docs/AI/PROJECT_CONTEXT.md
- docs/AI/HANDOFF.md

## 2026-03-28（阶段变更总览）
### 记录目的
- 汇总截至当前会话的全部关键变更，作为对外同步和阶段验收依据。

### 新增文件
- AGENTS.md
- .github/copilot-instructions.md
- CLAUDE.md
- GEMINI.md
- .cursorrules
- docs/AI/PROJECT_CONTEXT.md
- docs/AI/WORKFLOW.md
- docs/AI/HANDOFF.md

### 已修改文件
- Requirements Documendation.md
- docs/Requirements/Requirements Documendation.md
- Specification.md
- docs/Development Specification/Specification.md
- Technical Design Document.md
- docs/Tech/Technical Design Document.md
- AGENTS.md
- docs/AI/PROJECT_CONTEXT.md
- docs/AI/WORKFLOW.md
- docs/AI/HANDOFF.md

### 变更主题归档
- 建立跨会话、跨 AI 的统一协作入口与记忆机制。
- 明确检索约束：数据库仅持久化，运行时采用“预加载 -> 内存索引/数据结构 -> 应用层算法”。
- 明确课程实现约束：核心数据结构与算法模块不得直接使用 Java 内置集合实现，需采用自定义结构（MyList/MyMap/MySet/MyPriorityQueue）。
- 在需求、规范、技术设计、AI 协作文档中完成双份（根目录与 docs）同步。

### 当前状态备注
- 文档层约束已统一。
- 代码层仍存在大量 `java.util` 集合使用，后续需按模块逐步替换并补测试。

## 2026-03-28（项目计划书初稿）
### 会话目标
- 产出课程设计“项目计划书”混合版初稿，突出需求实现主线并降低 AI 章节比重。

### 已完成
- 新建计划书文档：`docs/Project Plan/Project Plan.md`。
- 完成项目计划书主体结构与正文初稿，覆盖目标、范围、约束、WBS、里程碑、质量、风险、交付与验收。
- AI 辅助开发章节采用轻量定位（提效工具），强调项目核心评分仍是需求实现与约束合规。
- 文档结构按 Markdown -> HTML -> Word 迁移友好原则编排。

### 变更文件
- docs/Project Plan/Project Plan.md
- docs/AI/HANDOFF.md

## 2026-03-28（成员分工与数据结构优先级优化）
### 会话目标
- 按真实团队成员信息细化分工，并强化数据结构实现在计划中的优先级与可见度。

### 已完成
- 将成员分工替换为真实信息：陈逸（组长）、程小路，并明确“全栈参与+偏重分工”。
- 将 WBS、RACI、周计划中的负责人与协作关系同步改为真实姓名。
- 新增“数据结构实现优先级”章节，明确 MyList/MyMap/MySet/MyPriorityQueue 的前置实现策略。
- 在周计划（第2-8周）与里程碑退出标准中前置并强化数据结构替换工作。
- 调整部分表达为更接地气、可执行导向，减少空泛表述。

### 变更文件
- docs/Project Plan/Project Plan.md
- docs/AI/HANDOFF.md

## 2026-03-28（项目经理级细化迭代）
### 会话目标
- 在既有全学期计划基础上进一步细化到可执行管理层面，提升专业性与落地性。

### 已完成
- 在计划书中新增 WBS 三级任务包（含 FR 关联、前置依赖、负责人、DoD）。
- 新增 RACI 责任矩阵与固定会议节奏（计划会、复盘会、Gate评审会）。
- 新增关键路径与依赖关系表，明确延误影响与纠偏动作。
- 新增阶段化测试计划明细（进入条件/退出条件/产出）。
- 新增 AI 全流程 SOP、人工确认点、AI相关风险与控制策略。
- 新增 FR-里程碑-交付物追踪表，强化验收可追踪性。

### 变更文件
- docs/Project Plan/Project Plan.md
- docs/AI/HANDOFF.md

## 2026-03-28（项目计划书重构 v1.0）
### 会话目标
- 按真实项目标准重构项目计划书，增强可执行性与落地细节，降低空泛描述。

### 已完成
- 对 `docs/Project Plan/Project Plan.md` 执行推倒重写，升级为可执行版本。
- 增加核心需求清单与优先级、技术栈总览、关键模块实施计划。
- 增加迭代节奏、里程碑退出条件、质量门禁、风险触发信号与升级机制。
- 调整 AI 章节为低比重辅助定位，强调需求实现与工程执行主线。
- 保留 Markdown 到 HTML 再到 Word 的格式迁移规范，确保后续排版可控。

### 变更文件
- docs/Project Plan/Project Plan.md
- docs/AI/HANDOFF.md

## 2026-03-28（全学期计划重构与AI章节修正）
### 会话目标
- 将项目计划改为面向全学期（16周）的详细执行计划，并修正 AI 章节表达偏差。

### 已完成
- 将项目周期改为 16 周全期计划，替换原 6 周短期迭代描述。
- 按课程教学节点建立 Gate 表，明确每个课堂节点对应目标与必交付。
- 重写周计划为第1周到第16周逐周任务清单，明确“何时做什么、输出什么”。
- 增加里程碑退出标准（M1-M4）与可量化判定条件。
- 重写 AI 章节为中等篇幅：明确 AI 在需求、设计、开发、测试、交付全流程参与方式，并补充治理机制与效果度量。

### 变更文件
- docs/Project Plan/Project Plan.md
- docs/AI/HANDOFF.md

## 2026-03-28（分工均衡与交叉协作修订）
### 会话目标
- 将项目分工从“单人负责”改为“共同执行、各有侧重、平均贡献”的表达方式。

### 已完成
- 重写 WBS 责任字段：由单一“负责人”改为“主责+协作”，并在任务层面轮换主责，体现两人共同投入。
- 调整角色职责描述：明确“组长负责节奏协调，不代表主要功劳归属”。
- 重写 RACI 矩阵：核心工作项统一为双人共同执行与共同负责，避免割裂分工。
- 保留“侧重”而非“独占”：陈逸偏算法与架构收敛，程小路偏前端体验与测试闭环。

### 变更文件
- docs/Project Plan/Project Plan.md
- docs/AI/HANDOFF.md

## 2026-03-28（计划书去标注与HTML排版导出）
### 会话目标
- 移除计划书中的编号型标注信息，输出更易读的 HTML 版本。

### 已完成
- 清理计划书中的编号标注字段与空标注列（如需求编号/WBS编码/Gate/风险编号等）。
- 修复自动清理后出现的残留异常文本，统一为可读表达。
- 重新生成带样式的 HTML 文档，优化标题层次、表格样式、移动端阅读与整体视觉排版。

### 变更文件
- docs/Project Plan/Project Plan.md
- docs/Project Plan/Project Plan.html
- docs/AI/HANDOFF.md

## 2026-03-28（新增 git-commit-mentor skill）
### 会话目标
- 将 `minEngine` 仓库中的 `git-commit-mentor` 协作技能引入当前项目，统一提交信息生成流程。

### 已完成
- 新增技能文件：`skills/git-commit-mentor/SKILL.md`。
- 在 Copilot 指令入口中增加该技能触发约定与安全约束（未经用户批准不得执行 `git commit`）。

### 变更文件
- skills/git-commit-mentor/SKILL.md
- .github/copilot-instructions.md
- docs/AI/HANDOFF.md

## 2026-03-28（自定义数据结构框架首版落地）
### 会话目标
- 先盘点项目中 `java.util` 数据结构使用现状，再提供同名接口/实现的自定义数据结构包，支持后续通过替换 import 解耦。

### 已完成
- 完成 `java.util` 使用清单扫描（去重）：ArrayList、Collections、Comparator、Date、HashMap、Iterator、List、Map、NoSuchElementException、Objects、PriorityQueue、Set。
- 新增自定义数据结构包 `com.travel.ds`，接口名与 Java 习惯保持一致：`Collection`、`List`、`Set`、`Map`、`Queue`、`Deque`。
- 新增实现类：`ArrayList`、`LinkedList`、`HashMap`、`HashSet`、`PriorityQueue`、`ArrayDeque`。
- 新增工具类：`Collections`（含 `sort/reverse/swap`）。
- 通过编辑器诊断验证新增目录无语法错误（当前环境无 Maven 命令，未执行 `mvn compile`）。

### 变更文件
- src/main/java/com/travel/ds/Collection.java
- src/main/java/com/travel/ds/List.java
- src/main/java/com/travel/ds/Set.java
- src/main/java/com/travel/ds/Map.java
- src/main/java/com/travel/ds/Queue.java
- src/main/java/com/travel/ds/Deque.java
- src/main/java/com/travel/ds/ArrayList.java
- src/main/java/com/travel/ds/LinkedList.java
- src/main/java/com/travel/ds/HashMap.java
- src/main/java/com/travel/ds/HashSet.java
- src/main/java/com/travel/ds/PriorityQueue.java
- src/main/java/com/travel/ds/ArrayDeque.java
- src/main/java/com/travel/ds/Collections.java
- docs/AI/HANDOFF.md

## 2026-03-28（数据库连接失败忽略开关）
### 会话目标
- 支持在非数据库开发场景下启动应用：数据库连接失败时可按开关忽略并继续运行。

### 已完成
- 在 `InMemoryDataLoader` 增加配置开关：
  - `app.storage.preload.enabled`（是否执行启动预加载，默认 `true`）
  - `app.debug.ignore-db-connection-failure`（数据库连接失败时是否忽略，默认 `false`）
- 在 `application.yml` 新增上述配置项与说明注释。
- 实测结果：
  - 依赖解析成功（`mvn dependency:resolve` -> `BUILD SUCCESS`）
  - 编译成功（`mvn -DskipTests compile` -> `BUILD SUCCESS`）
  - 启动时设置 `APP_DEBUG_IGNORE_DB_CONNECTION_FAILURE=true`，数据库连接失败被告警后跳过预加载，应用仍成功启动（Tomcat 8080 started）。

### 变更文件
- src/main/java/com/travel/storage/InMemoryDataLoader.java
- src/main/resources/application.yml
- docs/AI/HANDOFF.md

## 2026-03-28（开发环境默认关闭鉴权）
### 会话目标
- 增加用户登录鉴权开关，并在开发环境默认关闭鉴权，提升无数据库场景下的开发与调试效率。

### 已完成
- 在安全配置中增加开关：`app.security.auth-enabled`（默认 `true`）。
- 当开关为 `false` 时，放行全部请求并跳过 JWT 过滤链注入。
- 在主配置中设置默认值为开启鉴权。
- 新增 `application-dev.yml`，在 `dev` profile 下默认将鉴权开关设为关闭。
- 验证结果：
  - `mvn -DskipTests compile` 编译成功。
  - 使用 `SPRING_PROFILES_ACTIVE=dev` 启动时，日志出现 `Security auth is disabled by config: app.security.auth-enabled=false`。
  - 同时在数据库不可达场景下，应用仍可启动并监听 8080 端口（依赖此前数据库失败忽略开关）。

### 变更文件
- src/main/java/com/travel/security/SecurityConfig.java
- src/main/resources/application.yml
- src/main/resources/application-dev.yml
- docs/AI/HANDOFF.md

## 2026-03-28（开发种子数据自动加载）
### 会话目标
- 在开发环境数据库不可用时，自动注入 5~10 条可测种子数据，保障前端联调与检索/推荐/路径功能验证。

### 已完成
- 新增 `DevSeedDataLoader`，覆盖以下内存数据：用户、兴趣、景区、标签及关联、建筑、道路、设施、餐厅、美食、游记、游记目的地、评论。
- 在 `InMemoryDataLoader` 中接入种子数据回填触发点：
  - 预加载显式关闭时触发
  - 数据库连接失败且开启忽略开关时触发
- 新增配置：
  - `app.dev-seed.enabled`（全局默认 `false`）
  - 在 `application-dev.yml` 中默认开启 `app.dev-seed.enabled=true`
  - 在 `application-dev.yml` 中默认开启 `app.debug.ignore-db-connection-failure=true`
- 验证结果：
  - `mvn -DskipTests compile` -> BUILD SUCCESS
  - `mvn spring-boot:run -Dspring-boot.run.profiles=dev` 启动日志出现 `Dev seed data loaded successfully`。
  - 接口验证通过：
    - `/api/recommendation/hot?page=1&size=3` 返回 5 条景区总量中的分页结果
    - `/api/food/search?areaId=201&page=1&size=5` 返回种子美食数据
    - `/api/route/map-data?areaId=201` 返回节点与边数据

### 变更文件
- src/main/java/com/travel/storage/DevSeedDataLoader.java
- src/main/java/com/travel/storage/InMemoryDataLoader.java
- src/main/resources/application.yml
- src/main/resources/application-dev.yml
- docs/AI/HANDOFF.md

## 2026-03-28（开发种子数据改造为 JSON 文件驱动）
### 会话目标
- 将 `DevSeedDataLoader` 中写死测试数据迁移为资源目录 JSON 文件，并补齐全量数据类型，确保无库开发可覆盖核心联调场景。

### 已完成
- 重构 `DevSeedDataLoader`：改为从 `app.dev-seed.path` 指定目录读取 JSON 并注入内存仓库。
- 新增 13 个 seed 文件（全量覆盖）：
  - `users`、`user_interests`、`scenic_areas`、`tags`、`scenic_area_tags`
  - `buildings`、`roads`、`facilities`、`restaurants`、`foods`
  - `diaries`、`diary_destinations`、`comments`
- 保持原有回退触发策略不变：数据库预加载失败时（dev 下开启忽略）自动加载种子数据。
- 新增配置：`app.dev-seed.path: classpath:dev-seed`。

### 验证结果
- `mvn -DskipTests compile` -> BUILD SUCCESS。
- `mvn spring-boot:run -Dspring-boot.run.profiles=dev` 启动日志显示：
  - `Dev seed data loaded from JSON path classpath:dev-seed successfully`
  - 已加载计数：users=6, scenicAreas=6, facilities=7, foods=8, diaries=6, comments=8。
- 接口验证通过：
  - `/api/recommendation/hot?page=1&size=10`
  - `/api/diary?page=1&size=10`
  - `/api/facility/search?limit=20`

### 变更文件
- src/main/java/com/travel/storage/DevSeedDataLoader.java
- src/main/resources/application.yml
- src/main/resources/dev-seed/users.json
- src/main/resources/dev-seed/user_interests.json
- src/main/resources/dev-seed/scenic_areas.json
- src/main/resources/dev-seed/tags.json
- src/main/resources/dev-seed/scenic_area_tags.json
- src/main/resources/dev-seed/buildings.json
- src/main/resources/dev-seed/roads.json
- src/main/resources/dev-seed/facilities.json
- src/main/resources/dev-seed/restaurants.json
- src/main/resources/dev-seed/foods.json
- src/main/resources/dev-seed/diaries.json
- src/main/resources/dev-seed/diary_destinations.json
- src/main/resources/dev-seed/comments.json
- docs/AI/HANDOFF.md

## 2026-03-28（自定义数据结构可用性与 java.util 一致性验证）
### 会话目标
- 验证 `com.travel.ds` 全部自定义数据结构可用，并与 `java.util` 对应容器在核心行为上保持一致。

### 已完成
- 修正原有基线测试中 `ArrayList#indexOf` 的错误断言。
- 新增对照测试文件 `CustomDataStructuresParityTest`，覆盖：
  - `ArrayList`、`LinkedList`、`HashMap`、`HashSet`、`PriorityQueue`、`ArrayDeque`、`Collections`
  - 核心行为：增删改查、空值处理、越界/空容器异常、迭代与顺序、排序/反转/交换效果
  - 对照方式：与 `java.util` 同操作、同断言结果
- 修复实现差异：`PriorityQueue#offer` 增加 null 拒绝（抛 `NullPointerException`），对齐 `java.util.PriorityQueue`。

### 验证结果
- 执行：`mvn "-Dtest=CustomDataStructuresTest,CustomDataStructuresParityTest" test -q`
- 结果：`TESTS_OK`（全部通过）。

### 变更文件
- src/main/java/com/travel/ds/PriorityQueue.java
- src/test/java/com/travel/ds/CustomDataStructuresTest.java
- src/test/java/com/travel/ds/CustomDataStructuresParityTest.java
- docs/AI/HANDOFF.md

## 2026-03-28（提交信息换行格式修复与 commit-mentor 强化）
### 会话目标
- 修复最近一次提交正文中 `\\n` 字面量导致的格式问题，并增强 `git-commit-mentor` 规则避免复发。

### 已完成
- 准备对最近一次提交执行 amend，改为真实多行提交正文（不含 `\\n` 字面量）。
- 更新 `skills/git-commit-mentor/SKILL.md`：新增提交执行硬规则，强制使用真实多行文本并优先 `git commit -F`。
- 更新 `.github/copilot-instructions.md`：补充仓库级禁止 `\\n` 字面量规则。

### 变更文件
- skills/git-commit-mentor/SKILL.md
- .github/copilot-instructions.md
- docs/AI/HANDOFF.md

