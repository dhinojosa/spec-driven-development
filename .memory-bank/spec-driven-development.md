# Spec-Driven Development

This repository uses Cucumber acceptance specifications as the source of truth
for business-facing behavior.

## Source Of Truth

Acceptance behavior lives in:

```text
full-application-acceptance/src/main/resources/<package-name>/*.feature
```

Each governed feature or scenario must have an identifier tag:

```gherkin
@ACC-0001
Feature: Account presence
```

## Cucumber Layout

Cucumber feature specifications live under `src/main/resources` in the
`full-application-acceptance` module.

Use this path shape:

```text
full-application-acceptance/src/main/resources/<package-name>/*.feature
```

Cucumber step definitions live under `src/test/java` in the
`full-application-acceptance` module.

Use package names that correspond to the feature package where practical, so the
feature files and step definitions remain easy to map.

Acceptance tests may require shared scenario state. When state is needed, use
Cucumber dependency injection instead of static mutable state.

Add or update `cucumber.properties` when Cucumber glue, object factory, plugin,
or dependency injection configuration is required.

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
