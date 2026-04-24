# Spec-Driven Development

This repository uses Cucumber acceptance specifications as the source of truth
for business-facing behavior.

Specifications and implementation terms should adhere to the shared vocabulary
in `.memory-bank/ubiquitous-language.md`.

## Source Of Truth

Acceptance behavior lives in:

```text
full-application-acceptance/src/test/resources/<package-name>/*.feature
```

Each governed feature or scenario must have an identifier tag:

```gherkin
@ACC-0001
Feature: Account presence
```

Behavioral revisions to an existing governed feature use a suffixed identifier:

```gherkin
@ACC-0001-02
Scenario: Anonymous user starts from the main page
```

Use the root identifier, such as `ACC-0001`, as the stable feature identity.
Use suffixed revisions, such as `ACC-0001-02`, when an existing governed
feature changes behavior, flow, validation, output, or acceptance intent.

## Cucumber Layout

Cucumber feature specifications live under `src/test/resources` in the
`full-application-acceptance` module.

Use this path shape:

```text
full-application-acceptance/src/test/resources/<package-name>/*.feature
```

Cucumber step definitions live under `src/test/java` in the
`full-application-acceptance` module.

Use package names that correspond to the feature package where practical, so the
feature files and step definitions remain easy to map.

Acceptance tests may require shared scenario state. When state is needed, use
Cucumber dependency injection instead of static mutable state.

Prefer wording in feature files and step definitions that matches the
ubiquitous language exactly, especially for page names, user roles, and
business actions.

Add or update `cucumber.properties` when Cucumber glue, object factory, plugin,
or dependency injection configuration is required.

## Task Mapping

Each spec identifier maps to one task file in `.ai-tasks/`.

Example:

```text
@ACC-0001 -> .ai-tasks/account-presence-ACC-0001.md
```

Revision identifiers map to their own task files.

Example:

```text
@ACC-0001-02 -> .ai-tasks/account-presence-ACC-0001-02.md
```

The task file contains the implementation plan as markdown checkboxes.

## Business Versus Technical Tasks

Business behavior must be driven by Cucumber specs and `ACC-####` identifiers.

Technical work may use `TECH-####` identifiers when no business behavior is
being specified.

Technical tasks must not introduce business behavior unless a Cucumber spec is
added.

## Workflow

1. Start from the Cucumber spec.
2. Add or update the spec identifier tag.
3. If the spec change is behaviorally new, create a new suffixed identifier and
   matching `.ai-tasks` file.
4. If the spec change is only wording or clarification with no behavioral
   impact, update the existing task notes instead of creating a revision task.
5. Present the task plan before implementation.
6. Implement vertically through the relevant modules.
7. Mark completed task checkboxes.
8. Run validation appropriate to the changed behavior.

When only one scenario in a feature changes, prefer applying the suffixed
revision tag at the scenario level so governance remains precise.

## Vertical Slice Rule

A feature should move through the system as one vertical slice, from
business-facing spec to implementation and verification.

Do not implement feature behavior without a Cucumber spec and task plan unless
the user explicitly asks for that.

A vertical slice is not complete unless the verification is complete for that
slice. When the business behavior is visible in the browser, completion
requires browser-level UI verification in addition to domain, application,
controller, and acceptance coverage.
