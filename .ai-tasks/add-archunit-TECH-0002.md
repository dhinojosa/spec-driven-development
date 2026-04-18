# TECH-0002 Add ArchUnit Boundary Rules

Governed by: Technical task

Business spec: Not applicable

Reason no Cucumber spec applies:

This task adds architecture enforcement for the existing hexagonal and DDD
style. It does not introduce business behavior.

## Build / Tooling

- [x] Add the ArchUnit JUnit dependency to `full-application`.
- [x] Add an ArchUnit test package under `full-application/src/test/java/com/evolutionnext/architecture`.
- [x] Configure the architecture tests to analyze `com.evolutionnext`.
- [x] Run `mvn verify` to prove the architecture tests are part of the full verification suite.

## Hexagonal Dependency Rules

- [x] Add a rule that domain code does not depend on application, port, infrastructure, HTTP, or runner packages.
- [x] Add a rule that application code does not depend on infrastructure adapter packages or HTTP handler packages.
- [x] Add a rule that `port/in` and `port/out` packages do not depend on infrastructure adapter packages.
- [x] Add a rule that infrastructure adapters may depend on ports and application services, but not the other way around.
- [x] Add a rule that HTTP handlers live under `infrastructure/adapter/in`.
- [x] Add a rule that repository adapters live under `infrastructure/adapter/out`.

## DDD Domain Rules

- [x] Add a rule that aggregate and value object classes live under `domain/model`.
- [x] Add a rule that domain events live under `domain/events`.
- [x] Add a rule that domain event class names are past-tense enough to end with `Registered`, `Created`, `Updated`, `Deleted`, `LoggedIn`, `Submitted`, `Canceled`, or another approved past-tense verb.
- [x] Add a rule that domain services live under `domain/service` and have names ending in `DomainService`.
- [x] Add a rule that domain services are stateless by rejecting non-static mutable fields.
- [x] Add a rule that domain model classes do not use JDBC, HTTP server, Testcontainers, Cucumber, REST Assured, or other infrastructure/test framework types.

## Port Naming Rules

- [x] Add a rule that inbound command ports live under `port/in` and end with `CommandPort`.
- [x] Add a rule that inbound query ports live under `port/in` and end with `QueryPort`.
- [x] Add a rule that command and query port names follow `<Role><Aggregate><Command|Query>Port`.
- [x] Add a rule that output repositories live under `port/out` and end with `Repository`.

## Application Service Rules

- [x] Add a rule that command application services implement command ports only.
- [x] Add a rule that query application services implement query ports only.
- [x] Add a rule that no application service implements both a command port and a query port.
- [x] Add a rule that application services live under `application/service`.
- [x] Add a rule that application service names end with `ApplicationService`.

## Repository Rules

- [x] Add a rule that repository adapter classes implement `port/out` repository interfaces.
- [x] Add a rule that repository adapters are tested by a corresponding property or integration test when persistence is introduced.

## Documentation

- [x] Document the ArchUnit rule categories in `.memory-bank/architecture.md`.
- [x] Document any intentional rule exceptions in the ArchUnit test class with a reason.

## Validation

- [x] Run `mvn verify`.
- [x] Confirm architecture tests fail when a temporary known-bad dependency is introduced, then remove the known-bad dependency.
- [x] Confirm the existing account feature passes all architecture rules.
