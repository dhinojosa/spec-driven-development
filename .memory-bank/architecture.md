# Architecture

The application should be built as vertical feature slices.

For a business capability, evaluate the full path from domain model to
externally observable behavior. If a layer is not needed for a feature, the task
plan must state why.

## Module Responsibilities

- `full-application`: application code, domain model, adapters, controllers,
  persistence, unit tests, and integration tests.
- `full-application-acceptance`: Cucumber acceptance specifications for
  business-facing behavior.
- `full-application-e2e`: end-to-end tests that run against packaged application
  images and supporting infrastructure.

## Application Layers

Feature tasks should consider:

- domain
- domain events
- domain services
- application services
- repository ports and adapters
- controllers
- domain unit tests
- application service unit tests
- controller unit tests
- repository integration tests with Testcontainers and jqwik
- application service integration tests
- controller integration tests

## End-To-End Layer

End-to-end tests should run through `full-application-e2e`.

When needed, `full-application` should produce an application image using Jib.
The e2e module should use Docker Compose to run the application image and
supporting services such as Postgres.

## Architectural Style

Use ports and adapters where it helps keep the application core independent from
infrastructure concerns.

Keep dependency direction clear:

- domain code should not depend on application services, controllers, or
  infrastructure
- application services may depend on domain code and ports
- adapters implement ports
- controllers call application services
