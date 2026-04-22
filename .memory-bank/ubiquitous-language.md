# Ubiquitous Language

This file records the shared language for the application domain and user
experience.

All implementation artifacts should adhere to this language where practical,
including:

- Cucumber specs
- `.ai-tasks` files
- memory-bank documentation
- package names
- class names
- test names
- page names
- handler names
- query and command names
- user-facing copy

Prefer domain terms from this file over ad hoc synonyms.

If a term is ambiguous, settle the term here before expanding implementation
that depends on it.

## Current Terms

- `welcome page`: the anonymous page at `/`
- `dashboard`: the authenticated entry page after login
- `todo today page`: an authenticated destination page
- `activity inventory page`: an authenticated destination page
- `record sheet page`: an authenticated destination page

## Naming Guidance

- Use business terms in specs and task files.
- Use corresponding technical names in code and routes without drifting away
  from the business term.
- Avoid using one term to mean both an anonymous page and an authenticated page.
- When a new term enters the project, add it here before it spreads across
  specs, tasks, code, and tests.
