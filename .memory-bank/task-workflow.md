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

## Task Identifiers

Use `ACC-####` for business-facing acceptance tasks governed by Cucumber specs.

Use `TECH-####` for technical tasks that are not directly tied to business
behavior.

`ACC-####` tasks must reference a matching Cucumber tag, such as `@ACC-0001`.

`TECH-####` tasks must state why no Cucumber spec applies.

Technical tasks must still use direct action items, include validation, and
update checkboxes as work is completed.

## Required Sections

Each task file should include:

- title with the identifier and feature name
- governed spec identifier
- feature file path
- step definition path
- business behavior summary
- implementation plan as markdown checkboxes grouped by category
- validation checklist

## Required Task Categories

Present implementation tasks by category in this order:

- Domain Aggregate
- Domain Service
- Application Service
- Repository
- Domain Event
- Controller
- E2E Testing

Each category must include the required implementation tasks and the necessary
tests that will be created for that category.

For each planned test, describe how it will be created. Include the intended
test type, module, target behavior, and supporting tools such as Cucumber,
Testcontainers, jqwik, or controller test utilities.

When a task includes Cucumber acceptance coverage, include direct action items
for feature files under `full-application-acceptance/src/main/resources`, step
definitions under `full-application-acceptance/src/test/java`, scenario state,
and `cucumber.properties` when glue or dependency injection configuration is
required.

Task plans must include the jqwik and Testcontainers properties defined in
`.memory-bank/architecture.md` when the feature includes repositories,
application services, or controllers.

The E2E Testing category must include Jib image creation for `full-application`
and Docker Compose wiring in `full-application-e2e` when a feature requires
end-to-end verification.

When e2e verification applies, include direct action items for creating or
updating the `full-application-e2e` `.env` resource, Maven resource filtering,
Docker Compose file, optional `init.sql`, and Testcontainers/REST Assured,
jqwik, or Selenium tests as appropriate.

If a category does not apply to a feature, keep the category visible and explain
why it is unnecessary for that feature.

## Checkbox Rules

- Use unchecked boxes for planned work: `- [ ]`
- Mark completed work immediately after it is done: `- [x]`
- Do not mark a task complete before implementation and validation are complete.
- If a task becomes unnecessary, keep it visible and explain why.

## Task Wording

Tasks must be direct, concrete action items.

Use imperative verbs such as `Create`, `Add`, `Implement`, `Wire`, `Test`,
`Configure`, `Validate`, or `Document`.

Do not create discussion tasks such as `Discuss`, `Consider`, `Decide whether`,
`Figure out`, or `Let's discuss`.

If a decision is required before implementation, write the task as the action
needed to resolve it, such as `Choose the repository persistence strategy and
record the decision`, then complete the decision before implementation work
continues.

## Plan Before Work

Before implementing a feature task, present the plan to the user.

The user may ask to implement:

- one task
- a subset of tasks
- all tasks for the governed spec

When the work is complete, update the task file checkboxes and report the
validation performed.

## Validation Rule

For every implementation change, run the full available Maven verification suite
unless the user explicitly asks for a narrower check or the suite is blocked by
environment constraints.

Default command:

```bash
mvn verify
```

Documentation-only or POM-only changes may use narrower validation such as:

```bash
mvn validate
```

If `mvn verify` cannot run, report why, run the strongest available narrower
validation, and document the validation gap in the task file.

Do not mark implementation tasks complete unless implementation and validation
are complete, or the remaining validation gap is explicitly recorded.
