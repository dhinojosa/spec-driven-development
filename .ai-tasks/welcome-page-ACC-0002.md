# ACC-0002 Welcome Page

Governed by: `@ACC-0002`

Feature file:

```text
full-application-acceptance/src/test/resources/com.evolutionnext.feature.welcome/welcome.feature
```

Step definition path:

```text
full-application-acceptance/src/test/java/com/evolutionnext/features/welcome
```

Business behavior summary:

Anonymous users who visit the website home page see a welcome page for the
Pomodoro Time application, including a tomato image and a Pomodoro Time image.

## Domain Aggregate

- [x] Confirm no aggregate is required because the welcome page is anonymous static behavior without business state.
- [x] Document that `ACC-0002` introduces no domain model, entity, or value object.
- [x] Skip aggregate unit tests because there is no domain state or invariant to enforce.

## Domain Service

- [x] Confirm no domain service is required because no domain rule or coordination exists for this feature.
- [x] Document that `ACC-0002` introduces no stateless domain service.
- [x] Skip domain service tests because no domain service is created.

## Application Service

- [x] Confirm no application service is required if the welcome page is served directly by an HTTP handler and static resource.
- [x] Document that the feature is handled as anonymous static page delivery with no command or query port.
- [x] Skip application service tests because no application service is introduced.

## Repository

- [x] Confirm no repository is required because the welcome page does not persist or retrieve business data.
- [x] Document that `ACC-0002` introduces no output port or persistence adapter.
- [x] Skip repository integration tests because no persisted state exists for this feature.

## Domain Event

- [x] Confirm no domain event is required because viewing the welcome page does not create a business state change.
- [x] Document that `ACC-0002` emits no domain events.
- [x] Skip domain event tests because there are no events.

## Controller

- [x] Create a dedicated welcome HTTP handler under the welcome feature package.
- [x] Wire the root route `/` to the welcome handler without breaking existing routes.
- [x] Create the welcome HTML resource under `full-application/src/main/resources/welcome/anonymous`.
- [x] Add a tomato image asset under static resources and reference it from the welcome page.
- [x] Add a Pomodoro Time image asset under static resources and reference it from the welcome page.
- [x] Style the welcome page so the front page reads as a polished entry point for the application.
- [x] Test that `GET /` returns the welcome page successfully.
- [x] Test that the home page response references the Pomodoro Time image asset.
- [x] Test that the home page response references the tomato image.
- [x] Test that the static asset handler serves the tomato image successfully.
- [x] Test that the static asset handler serves the Pomodoro Time image successfully.

## E2E Testing

- [x] Create Cucumber step definitions under `full-application-acceptance/src/test/java/com/evolutionnext/features/welcome`.
- [x] Add acceptance steps for navigating to the home page as an anonymous user.
- [x] Add acceptance assertions for the welcome page content and the two welcome image references.
- [x] Skip Selenium unless the demo requires browser-level validation that the welcome images render visibly.

## Validation

- [x] Run `mvn verify`.
- [x] Confirm the `@ACC-0002` acceptance scenario passes.
- [x] Confirm the root route `/` serves the welcome page and existing account routes still behave correctly.
