# ACC-0001 Account Access

Governed by: `@ACC-0001`

Feature file:

```text
full-application-acceptance/src/main/resources/com/evolutionnext/features/account/account-access.feature
```

Step definition path:

```text
full-application-acceptance/src/test/java/com/evolutionnext/features/account
```

Business behavior summary:

Anonymous users can register an account with a user name and password.
Registered users can log in and reach the pomodoro page. Users with bad
credentials stay on the login page and see an invalid credentials message.

## Domain Aggregate

- [x] Create the account aggregate with account id, user name, password credential state, and authentication behavior.
- [x] Create account value objects for account id, user name, and password credential inputs.
- [x] Test account creation rules with unit tests for valid user names and passwords.
- [x] Test account authentication rules with jqwik-generated valid and invalid credentials.

## Domain Service

- [x] Confirm no stateless domain service is needed because the account aggregate enforces registration and authentication rules directly.
- [x] Document why no domain service is needed if the aggregate can enforce the account rules directly.
- [x] Skip domain service unit tests because no domain service is required for this feature.

## Application Service

- [x] Create an account command port for registering accounts and logging in.
- [x] Create an account query port for finding registered accounts and login outcomes observable by the application.
- [x] Implement the account application service behind the command port.
- [x] Test that every account command accepted by the command port produces state observable through the query port.
- [x] Test bad credentials through the application service without inspecting repository implementation details.

## Repository

- [x] Create the account repository output port.
- [x] Implement the account repository adapter.
- [x] Test with Testcontainers that every jqwik-generated saved account can be found by id.
- [x] Place account jqwik arbitraries under `src/test/java/com/evolutionnext/features/account/arbitrary`.

## Domain Event

- [x] Create past-tense account domain events for account registration and successful login if those events are needed by the feature.
- [x] Document domain events as aggregate-emitted state changes for registration and successful login.
- [x] Test emitted account domain events from aggregate behavior when events are created.

## Controller

- [x] Create an account HTTP handler for registration and login using the JDK simple web server APIs.
- [x] Create static HTML resources for account registration and login under `full-application/src/main/resources/account/anonymous`.
- [x] Create the pomodoro page resource under `full-application/src/main/resources/account`.
- [x] Test that every successful registration `POST` is observable through a corresponding `GET`.
- [x] Test that successful login redirects or routes to the pomodoro page.
- [x] Test that bad credentials return the login page with an invalid credentials message.

## E2E Testing

- [x] Create Cucumber step definitions under `full-application-acceptance/src/test/java/com/evolutionnext/features/account`.
- [x] Create scenario-scoped account state using Cucumber dependency injection.
- [x] Add `cucumber.properties` when glue or dependency injection configuration is required.
- [x] Configure Jib image creation for `full-application` if it is not already configured.
- [x] Configure `full-application-e2e` Docker Compose wiring for the application and Postgres.
- [x] Create or update `full-application-e2e/src/main/resources/.env` with `FULL_APPLICATION_VERSION=${project.version}`.
- [x] Add `init.sql` if the account repository schema requires database initialization.
- [x] Test registration, successful login, and failed login through Cucumber acceptance tests; add a gated REST Assured e2e health check for a running compose environment.
- [x] Skip Selenium coverage because this feature currently verifies HTTP behavior without browser-only interactions.

## Validation

- [x] Run `mvn verify`.
- [x] Document any validation blocker and the strongest narrower validation run: `mvn verify` passed; the compose-based e2e health check is gated and skipped unless `RUN_E2E=true` with the image and Docker Compose environment running.
