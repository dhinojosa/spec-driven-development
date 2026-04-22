# ACC-0001-03 Account Access Login To Dashboard

Governed by: `@ACC-0001-03`

Feature file:

```text
full-application-acceptance/src/test/resources/com/evolutionnext/features/account/account-access.feature
```

Step definition path:

```text
full-application-acceptance/src/test/java/com/evolutionnext/features/account
```

Business behavior summary:

Registered users can still log in with valid credentials, but successful login
now takes them to the dashboard page instead of the pomodoro page. Registration
to dashboard and invalid-login behavior remain unchanged.

## Domain Aggregate

- [x] Confirm no aggregate change is required because authentication rules remain unchanged.
- [x] Document that `ACC-0001-03` changes post-login navigation only, not account business rules.
- [x] Skip new aggregate tests because no domain invariant or aggregate behavior changes.

## Domain Service

- [x] Confirm no domain service is required because the change is limited to HTTP flow and page routing.
- [x] Document that `ACC-0001-03` introduces no new domain coordination.
- [x] Skip domain service tests because no domain service behavior changes.

## Application Service

- [x] Confirm no command or query port change is required if successful login remains the same application outcome.
- [x] Document that login still succeeds through the same command path and remains observable through existing application behavior.
- [x] Skip new application service tests unless controller work exposes a missing application seam.

## Repository

- [x] Confirm no repository change is required because persistence behavior is unchanged.
- [x] Document that `ACC-0001-03` does not alter repository contracts or stored account state.
- [x] Skip new repository integration and property tests because storage expectations remain unchanged.

## Domain Event

- [x] Confirm no domain event change is required because successful login still represents the same business state change.
- [x] Document that event semantics remain unchanged and only the HTTP response target changes.
- [x] Skip new domain event tests because domain event behavior is unchanged.

## Controller

- [x] Keep `@ACC-0001-03` governing only the login-to-dashboard scenario.
- [x] Reuse the existing dashboard page resource for successful login.
- [x] Change successful login handling so the user is shown the dashboard page instead of the pomodoro page.
- [x] Update controller tests so successful login returns dashboard-page content instead of pomodoro-page content.
- [x] Keep registration-to-dashboard behavior unchanged.
- [x] Keep invalid-login behavior unchanged.

## E2E Testing

- [x] Update step definitions so `the user is taken to the dashboard page` asserts the dashboard response.
- [x] Keep the registration and invalid-login scenarios under their current behavior without unnecessary tag churn.
- [x] Verify acceptance coverage proves successful login reaches the dashboard page.

## Validation

- [x] Run `mvn verify`.
- [x] Confirm the `@ACC-0001-03` login scenario passes with the user shown the dashboard page.
- [x] Confirm registration still reaches the dashboard page.
- [x] Confirm bad credentials still return the invalid-login page.
