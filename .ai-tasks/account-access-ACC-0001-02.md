# ACC-0001-02 Account Access Registration To Dashboard

Governed by: `@ACC-0001-02`

Feature file:

```text
full-application-acceptance/src/test/resources/com/evolutionnext/features/account/account-access.feature
```

Step definition path:

```text
full-application-acceptance/src/test/java/com/evolutionnext/features/account
```

Business behavior summary:

Anonymous users can still register an account with a user name and password,
but after successful registration they are shown the dashboard page instead of
the login page. Existing login success and invalid-login behavior remain
unchanged.

## Domain Aggregate

- [x] Confirm no aggregate change is required because account registration and authentication rules remain unchanged.
- [x] Document that `ACC-0001-02` changes post-registration navigation only, not account business rules.
- [x] Skip new aggregate tests because no domain invariant or aggregate behavior changes.

## Domain Service

- [x] Confirm no domain service is required because the change is limited to HTTP flow and page routing.
- [x] Document that `ACC-0001-02` introduces no new domain coordination.
- [x] Skip domain service tests because no domain service behavior changes.

## Application Service

- [x] Confirm no command or query port change is required if successful registration remains the same application outcome.
- [x] Document that registration still succeeds through the same command path and remains observable through the existing query path.
- [x] Skip new application service tests unless controller work exposes a missing application seam.

## Repository

- [x] Confirm no repository change is required because persistence behavior is unchanged.
- [x] Document that `ACC-0001-02` does not alter repository contracts or stored account state.
- [x] Skip new repository integration and property tests because storage expectations remain unchanged.

## Domain Event

- [x] Confirm no domain event change is required because successful registration still represents the same business state change.
- [x] Document that event semantics remain unchanged and only the HTTP response target changes.
- [x] Skip new domain event tests because domain event behavior is unchanged.

## Controller

- [x] Add `@ACC-0001-02` above the revised registration scenario while leaving `@ACC-0001` as the stable feature identifier.
- [x] Create a dashboard page resource under `full-application/src/main/resources/account`.
- [x] Decide and document the initial dashboard content as links to the todo today page, activity inventory page, and record sheet page.
- [x] Change successful registration handling so the user is shown the dashboard page instead of the login page.
- [x] Update controller tests so successful registration returns dashboard-page content instead of login-page content.
- [x] Keep `POST /account/register` observable through `GET /account?userName=...`.
- [x] Keep successful login and invalid-login behavior unchanged for this revision unless the spec changes further.

## E2E Testing

- [x] Update the revised Cucumber scenario so `@ACC-0001-02` governs only the first scenario.
- [x] Update step definitions so `the user is shown the dashboard page` asserts the dashboard response.
- [x] Keep the login success and invalid-login scenarios under their current behavior without unnecessary tag churn.
- [x] Verify acceptance coverage proves successful registration reaches the dashboard page.

## Validation

- [x] Run `mvn verify`.
- [x] Confirm the `@ACC-0001-02` registration scenario passes with the user shown the dashboard page.
- [x] Confirm login success still reaches the pomodoro page.
- [x] Confirm bad credentials still return the invalid-login page.
