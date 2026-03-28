# Copilot Instructions

在本仓库内工作时：
1. 先读取 `AGENTS.md`。
2. 再读取 `docs/AI/PROJECT_CONTEXT.md` 与 `docs/AI/WORKFLOW.md`。
3. 编码完成后必须更新 `docs/AI/HANDOFF.md`（追加记录）。
4. 搜索/模糊/全文检索能力必须走内存索引算法，不得使用 SQL LIKE/JOIN 做检索计算。
5. 输出建议时优先保证：需求一致性、可验证性、可交接性。
6. 当用户请求“准备 commit/提交信息/commit message”时，优先使用 `skills/git-commit-mentor/SKILL.md` 的流程：先取证与草稿，再请求用户批准，未经批准不得执行 `git commit`。
7. 执行 `git commit` 时禁止在正文使用字面量 `\n`；必须使用真实多行文本（推荐 `git commit -F <message-file>`）。
