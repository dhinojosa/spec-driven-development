# Spec-Driven Development

This repository uses Cucumber acceptance specifications as the source of truth
for business-facing behavior.

## Source Of Truth

Acceptance behavior lives in:

```text
full-application-acceptance/src/test/resources/features/
```

Each governed feature or scenario must have an identifier tag:

```gherkin
@ACC-0001
Feature: Account presence
```

## Task Mapping

Each spec identifier maps to one task file in `.ai-tasks/`.

Example:

```text
@ACC-0001 -> .ai-tasks/account-presence-ACC-0001.md
```

The task file contains the implementation plan as markdown checkboxes.

## Workflow

1. Start from the Cucumber spec.
2. Add or update the spec identifier tag.
3. Create or update the matching `.ai-tasks` file.
4. Present the task plan before implementation.
5. Implement vertically through the relevant modules.
6. Mark completed task checkboxes.
7. Run validation appropriate to the changed behavior.

## Vertical Slice Rule

A feature should move through the system as one vertical slice, from
business-facing spec to implementation and verification.

Do not implement feature behavior without a Cucumber spec and task plan unless
the user explicitly asks for that.
