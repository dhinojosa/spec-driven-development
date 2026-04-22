# ACC-0001-05 Account Access Logout From Secure Pages

Governed by: `@ACC-0001-05`

Feature file:

```text
full-application-acceptance/src/test/resources/com/evolutionnext/features/account/account-access.feature
```

Step definition path:

```text
full-application-acceptance/src/test/java/com/evolutionnext/features/account
```

Business behavior summary:

Authenticated users can log out from any secure page. The secure pages are the
dashboard page, todo today page, activity inventory page, and record sheet
page. Logging out returns the user to the home page and removes secure-area
navigation.

## Domain Aggregate

- [x] Confirm no aggregate change is required because logout is session and navigation behavior, not account-state behavior.
- [x] Document that `ACC-0001-05` introduces no new account aggregate rule.
- [x] Skip aggregate tests because no domain invariant changes.

## Domain Service

- [x] Confirm no domain service is required because logout does not add domain coordination.
- [x] Document that `ACC-0001-05` is handled outside the domain layer.
- [x] Skip domain service tests because no domain service behavior changes.

## Application Service

- [x] Confirm no new command or query port is required because logout remains an HTTP and authenticated-session concern for the current application shape.
- [x] Document that authenticated state is represented through a lightweight cookie for the current scope.
- [x] Skip new application-service tests because logout does not add a new application-service seam.

## Repository

- [x] Confirm no repository change is required because logout does not persist account state.
- [x] Document that `ACC-0001-05` introduces no repository contract changes.
- [x] Skip repository integration and property tests because persistence behavior is unchanged.

## Domain Event

- [x] Confirm no domain event change is required because logout is not modeled as a domain event in the current scope.
- [x] Document that logout does not emit a new domain event.
- [x] Skip domain event tests because domain event behavior is unchanged.

## Controller

- [x] Add a lightweight authenticated cookie on successful registration and successful login.
- [x] Add a logout endpoint that clears the authenticated cookie and returns the home page.
- [x] Add secure page routes for the dashboard page, todo today page, activity inventory page, and record sheet page.
- [x] Add a visible `Log out` button to every secure page.
- [x] Keep the home page free of secure-area navigation.
- [x] Update controller tests to verify secure pages show the logout button and logout returns the home page without secure-area navigation.

## E2E Testing

- [x] Add acceptance steps for the user being logged in, navigating to each secure page, clicking the logout button, and verifying the home page has no secure-area navigation.
- [x] Implement the scenario outline examples for dashboard, todo today, activity inventory, and record sheet.
- [x] Verify acceptance coverage proves logout works from every secure page.

## Validation

- [x] Run `mvn verify`.
- [x] Confirm the `@ACC-0001-05` scenario outline passes for all secure-page examples.
- [x] Confirm valid registration still reaches the dashboard page.
- [x] Confirm valid login still reaches the dashboard page.
