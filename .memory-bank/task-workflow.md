# Task Workflow

Task files live in `.ai-tasks/` and are governed by Cucumber spec identifiers.

## Naming

Use this filename pattern:

```text
.ai-tasks/<feature-slug>-<identifier>.md
```

Example:

```text
.ai-tasks/account-presence-ACC-0001.md
```

The identifier must match a Cucumber tag such as `@ACC-0001`.

## Required Sections

Each task file should include:

- title with the identifier and feature name
- governed spec identifier
- feature file path
- business behavior summary
- implementation plan as markdown checkboxes
- validation checklist

## Checkbox Rules

- Use unchecked boxes for planned work: `- [ ]`
- Mark completed work immediately after it is done: `- [x]`
- Do not mark a task complete before implementation and validation are complete.
- If a task becomes unnecessary, keep it visible and explain why.

## Plan Before Work

Before implementing a feature task, present the plan to the user.

The user may ask to implement:

- one task
- a subset of tasks
- all tasks for the governed spec

When the work is complete, update the task file checkboxes and report the
validation performed.
