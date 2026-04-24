# PROC-0003 UI Test Completion Governance

Governed by: `PROC-0003`

Related memory bank files:

```text
.memory-bank/project.md
.memory-bank/spec-driven-development.md
.memory-bank/task-workflow.md
AGENTS.md
```

Procedure change summary:

Feature delivery is not complete unless it is test-complete for the implemented
slice. When a feature includes browser-visible behavior that matters to the
user, browser-level UI verification is required before the slice is described
as executed or delivered, unless the user explicitly accepts the gap.

## Scope

- [x] Record that vertical slices must be test-complete.
- [x] Record that browser-visible behavior requires browser-level UI
  verification.
- [x] Record that missing applicable test layers keep the governed task open.

## Files Updated

- [x] Update `.memory-bank/project.md` with the delivery and UI-test rule.
- [x] Update `.memory-bank/spec-driven-development.md` with the vertical-slice
  completion rule.
- [x] Update `.memory-bank/task-workflow.md` with the task-planning and
  completion rule for browser-visible features.
- [x] Update `AGENTS.md` so agent behavior follows the same rule.

## Validation

- [x] Confirm the memory bank and agent instructions all state the same rule.
- [x] Keep the procedure durable by recording it in `.ai-tasks/`.
