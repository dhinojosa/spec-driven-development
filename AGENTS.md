# Agent Instructions

This repository is for spec-driven-development.

Before making feature changes, read:

- `.memory-bank/project.md`
- `.memory-bank/architecture.md`
- `.memory-bank/spec-driven-development.md`
- `.memory-bank/task-workflow.md`

## Required Workflow

- Business behavior starts from Cucumber specs in `full-application-acceptance`.
- Governed specs use identifier tags such as `@ACC-0001`.
- Behavioral revisions to an existing governed feature use suffixed tags such as `@ACC-0001-02`.
- Each governed spec must have a matching task file in `.ai-tasks/`.
- Create a new suffixed task file when a spec change alters behavior, flow, validation, output, or acceptance intent.
- Update the existing task only when the spec edit is non-behavioral wording or clarification.
- Prefer scenario-level revision tags when only one scenario changed.
- Show the task plan before implementing feature behavior.
- Implement features as vertical slices through the relevant modules.
- Mark completed task checkboxes when work is done.

## Build

Use SDKMAN:

```bash
sdk use java 26-zulu
sdk use maven 4.0.0-rc-5
mvn validate
```

For implementation changes, run the full available Maven verification suite:

```bash
mvn verify
```

If full verification is blocked by environment constraints, report the blocker,
run the strongest narrower validation available, and document the validation gap
in the relevant task file.

## Repository Shape

- `full-application`: application implementation
- `full-application-acceptance`: Cucumber acceptance specifications
- `full-application-e2e`: end-to-end verification
- `.ai-tasks`: task plans governed by spec identifiers
- `.memory-bank`: durable project, architecture, and workflow context
