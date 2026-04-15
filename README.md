# Spec Driven Development

This project is for spec-driven-development.

It is a Maven multi-module project organized around a full application and
separate verification layers.

## Modules

- `full-application`
- `full-application-acceptance`
- `full-application-e2e`

## Toolchain

This project is currently being run with:

- Java: `26-zulu`
- Maven: `4.0.0-rc-5`
- Build command: `mvn validate`

Use SDKMAN to select the project toolchain:

```bash
sdk use java 26-zulu
sdk use maven 4.0.0-rc-5
mvn validate
```

## Build Management

The parent POM centralizes dependency versions with Maven BOM imports for JUnit,
Testcontainers, Cucumber, Log4j, SLF4J, jqwik, AssertJ, Jackson, and REST Assured.

The parent POM also pins Maven plugin versions for resources, compiler, Surefire,
Failsafe, Surefire reports, and project info reports.

## License

This project is licensed under the MIT License. See `LICENSE`.
