# PROC-0002 Ubiquitous Language Governance

Governed by: Procedural task

Business spec: Not applicable

Technical spec: Not applicable

Reason no executable spec applies:

This task records the decision to maintain a dedicated ubiquitous language for
the application and to require implementation artifacts to adhere to that
language.

## Procedure Change

- [x] Define `.memory-bank/ubiquitous-language.md` as the durable source for shared application vocabulary.
- [x] Record that specs, `.ai-tasks`, memory-bank notes, tests, classes, handlers, routes, and user-facing copy should adhere to the ubiquitous language.
- [x] Record that ambiguous or newly introduced business terms should be settled in the ubiquitous language before spreading through implementation.

## Files Updated

- [x] Create `.memory-bank/ubiquitous-language.md`.
- [x] Update `.memory-bank/project.md` to require adherence to the ubiquitous language.
- [x] Update `.memory-bank/spec-driven-development.md` so specs and steps align to the ubiquitous language.
- [x] Update `AGENTS.md` so implementation work reads and follows the ubiquitous language.

## Initial Vocabulary

- [x] Record `welcome page` as the anonymous page at `/`.
- [x] Record `dashboard` as the authenticated entry page after login.
- [x] Record `todo today page` as an authenticated destination page.
- [x] Record `activity inventory page` as an authenticated destination page.
- [x] Record `record sheet page` as an authenticated destination page.

## Validation

- [x] Verify the ubiquitous language file is referenced by the memory bank and `AGENTS.md`.
- [x] Verify the procedure gives future commits a stable governance identifier to reference as `PROC-0002`.
