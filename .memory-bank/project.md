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

## Web Application Constraints

Use the JDK `SimpleFileServer` / simple web server APIs for this project.

Do not use Spring Web, servlet containers, server-side templating frameworks, or
other larger web frameworks unless the user explicitly changes this constraint.

Every feature or aggregate subdivision should provide its own HTTP handler.

Provide a dedicated static asset handler that serves resources from
`src/main/resources/assets`.

Provide a dedicated health handler for application health checks.

Static HTML files live under `src/main/resources` in a directory named for the
feature or aggregate they represent.

Example:

```text
src/main/resources/account/
```

Feature resource directories may be subdivided by use case or audience, such as:

```text
src/main/resources/account/admin/
src/main/resources/account/public/
src/main/resources/account/anonymous/
```

Do not use templating for this project. HTML files should be static resources.

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
