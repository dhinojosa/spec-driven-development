# ACC-0001-04 Account Access Short Password Validation

Governed by: `@ACC-0001-04`

Feature file:

```text
full-application-acceptance/src/test/resources/com/evolutionnext/features/account/account-access.feature
```

Step definition path:

```text
full-application-acceptance/src/test/java/com/evolutionnext/features/account
```

Business behavior summary:

Anonymous users who try to register with a password shorter than 8 characters
remain on the account registration page, see the validation message, keep the
entered user name, and see an empty password field.

## Domain Aggregate

- [x] Keep the existing password validation rule in the domain model.
- [x] Document that `ACC-0001-04` exposes the existing minimum password length rule as governed behavior.
- [x] Skip new aggregate tests because the rule already exists in the domain and this revision governs the observable outcome.

## Domain Service

- [x] Confirm no domain service is required because validation still occurs through the aggregate value objects.
- [x] Document that `ACC-0001-04` introduces no new domain coordination.
- [x] Skip domain service tests because no domain service behavior changes.

## Application Service

- [x] Add an explicit invalid-registration result so registration validation failures do not escape as uncaught exceptions.
- [x] Return the validation message and entered user name through the application service result.
- [x] Test that short-password registration returns the invalid-registration result.

## Repository

- [x] Confirm no repository change is required because invalid registration should not persist anything.
- [x] Document that `ACC-0001-04` does not alter repository contracts or stored account state.
- [x] Skip new repository integration and property tests because storage behavior remains unchanged for valid registrations.

## Domain Event

- [x] Confirm no domain event change is required because invalid registration does not create a new business state change.
- [x] Document that invalid registration does not emit a new domain event.
- [x] Skip new domain event tests because domain event behavior is unchanged.

## Controller

- [x] Render the registration page with the entered user name preserved after short-password validation failure.
- [x] Render the validation message `Password must be at least 8 characters` on the registration page.
- [x] Render the password field empty after validation failure.
- [x] Keep successful registration-to-dashboard behavior unchanged.
- [x] Update controller tests to verify the short-password response code, message, preserved user name, and empty password field.

## E2E Testing

- [x] Update acceptance steps so the registration page can be asserted after validation failure.
- [x] Add acceptance assertions for the password validation message, preserved user name, and empty password field.
- [x] Verify acceptance coverage proves short-password registration stays on the registration page.

## Validation

- [x] Run `mvn verify`.
- [x] Confirm the `@ACC-0001-04` scenario passes.
- [x] Confirm valid registration still reaches the dashboard page.
- [x] Confirm valid login still reaches the dashboard page.
