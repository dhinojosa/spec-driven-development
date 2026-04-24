# ACC-0006 Todo Today

Governed by: `@ACC-0006`

Feature file:

```text
full-application-acceptance/src/test/resources/com.evolutionnext.feature.todotoday/todo-today.feature
```

Step definition path:

```text
full-application-acceptance/src/test/java/com/evolutionnext/features/todotoday
```

Business behavior summary:

Logged in users can manage the todo today page by adding tasks with pomodoro
estimates, seeing a warning for large tasks above 6 pomodoros, and completing
tasks with under, on, or over estimate outcomes shown on the page.

## Domain Aggregate

- [x] Create a todo today aggregate that holds today’s tasks and their pomodoro estimates.
- [x] Create a task entity or value object with task name, estimated pomodoros, completed pomodoros, and completion state.
- [x] Model completion status relative to estimate as under, on, or over the estimate.
- [x] Add aggregate tests for adding tasks, setting estimates, setting completed pomodoros, and marking tasks complete.

## Domain Service

- [x] Keep large-task warning and completion-status calculation inside the aggregate unless a separate stateless rule object is required.
- [x] Record in the task whether a separate domain service is unnecessary for this slice.
- [x] Test the warning threshold and completion-state outcomes through the aggregate or a stateless rule object if one is introduced.

## Application Service

- [x] Create command ports for adding a task, setting an estimate, setting completed pomodoros, and marking a task complete.
- [x] Create a query port for reading the todo today page state.
- [x] Implement command and query application services behind those ports.
- [x] Test that tasks added through the command port are observable through the query port.
- [x] Test that estimates above 6 pomodoros produce a large-task warning in query state.
- [x] Test that under, on, and over completion is observable through the query port.

## Repository

- [x] Create a todo today repository output port.
- [x] Create an in-memory repository adapter for the first vertical slice.
- [x] Add repository tests that prove saved todo-today state can be read back.
- [x] Keep persistence in-memory for `ACC-0006` unless the scope is explicitly expanded.

## Domain Event

- [x] Record that no domain event is introduced for this slice unless task-added or task-completed behavior is explicitly needed outside the feature.
- [x] Keep the category visible and mark it unnecessary if no event is added.
- [x] Do not invent domain events just to satisfy the category.

## Controller

- [x] Create a dedicated todo-today HTTP handler for the feature.
- [x] Wire `/todo-today` to the feature handler.
- [x] Update the todo-today page resource so the page visibly supports adding a task.
- [x] Update the todo-today page resource so the page visibly supports setting an estimate from the page.
- [x] Update the todo-today page resource so the page visibly supports setting completed pomodoros from the page.
- [x] Update the todo-today page resource so the page visibly supports marking a task complete.
- [x] Add UI support for showing the large-task warning.
- [x] Add UI support for showing under, on, and over estimate status.
- [x] Add UI support for showing completed tasks as struck through and greyed out.
- [x] Add controller tests for adding a task with an initial estimate.
- [x] Add controller tests for warning when estimate is above 6 pomodoros.
- [x] Add controller tests for completing a task under the estimate.
- [x] Add controller tests for completing a task on the estimate.
- [x] Add controller tests for completing a task over the estimate.

## E2E Testing

- [x] Create Cucumber step definitions under `full-application-acceptance/src/test/java/com/evolutionnext/features/todotoday`.
- [x] Add scenario state for authenticated todo-today interactions.
- [x] Implement acceptance coverage for adding a task with an initial estimate.
- [x] Implement acceptance coverage for estimating a task above 6 pomodoros.
- [x] Implement acceptance coverage for completing a task relative to the estimate through the scenario outline examples.
- [x] Add Selenium coverage in `full-application-e2e` for the browser-visible flow.
- [x] Verify the browser-visible flow can add a task, set an estimate, show a large-task warning, set completed pomodoros, and mark the task complete.
- [x] Do not treat `ACC-0006` as complete until the browser-level test exists, since this feature is visibly UI-driven.

## Validation

- [x] Run `mvn verify`.
- [x] Confirm `@ACC-0006` passes for the add-task scenario.
- [x] Confirm `@ACC-0006` passes for the large-task warning scenario.
- [x] Confirm `@ACC-0006` passes for the under, on, and over estimate scenario outline examples.
- [x] Confirm the Selenium or browser-level test passes for the visible todo-today flow.
