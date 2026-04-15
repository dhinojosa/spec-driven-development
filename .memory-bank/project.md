# Project

This project is for spec-driven-development.

The repository is intended to demonstrate and exercise a workflow where
business-facing Cucumber specifications drive implementation tasks and vertical
feature delivery.

## Project Preferences

- Keep durable context in `.memory-bank/`.
- Keep executable task plans in `.ai-tasks/`.
- Reserve `docs/` for future user-facing or project documentation.
- Prefer small, explicit changes.
- Prefer clear identifiers that connect specs, tasks, and implementation work.

## Toolchain

The project is currently run with:

- Java: `26-zulu`
- Maven: `4.0.0-rc-5`

Use:

```bash
sdk use java 26-zulu
sdk use maven 4.0.0-rc-5
mvn validate
```
