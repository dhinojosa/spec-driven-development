# PROC-0001 Spec Revision Governance

Governed by: Procedural task

Business spec: Not applicable

Technical spec: Not applicable

Reason no executable spec applies:

This task records a workflow decision for how governed Cucumber specifications
and `.ai-tasks` files evolve when an existing accepted feature changes.

## Procedure Change

- [x] Define `PROC-####` as the identifier family for workflow and governance changes.
- [x] Record that behavioral revisions to an existing governed acceptance feature use suffixed identifiers such as `ACC-0001-02`.
- [x] Record that a suffixed behavioral revision maps to its own task file such as `.ai-tasks/account-access-ACC-0001-02.md`.
- [x] Record that wording-only or clarification-only edits stay on the existing task file instead of creating a revision task.
- [x] Record that scenario-level revision tags are preferred when only one scenario changes.

## Files Updated

- [x] Update `.memory-bank/spec-driven-development.md` with the revision-tag procedure.
- [x] Update `.memory-bank/task-workflow.md` with the revision-task naming and usage rule.
- [x] Update `AGENTS.md` so the procedure is operational during implementation work.

## Examples

- [x] Document the example revision identifier `@ACC-0001-02`.
- [x] Document the matching task filename example `.ai-tasks/account-presence-ACC-0001-02.md`.

## Validation

- [x] Verify the procedure is written consistently across the memory bank and `AGENTS.md`.
- [x] Verify the procedure gives commit messages a stable governance identifier to reference as `PROC-0001`.
