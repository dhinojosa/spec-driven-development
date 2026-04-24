@ACC-0006
Feature: Todo today

    As a logged in user
    I want to manage today's tasks
    So that I can decide what to work on next

    Scenario: User adds a task with an initial estimate
        Given a logged in user
        And the user is on the todo today page
        When the user adds a task named "Prepare workshop outline"
        And the user sets the task estimate to 3 pomodoros
        Then the todo today page shows the task "Prepare workshop outline"
        And the todo today page shows the task estimate as 3 pomodoros

    Scenario: User estimates a task above 6 pomodoros
        Given a logged in user
        And the user is on the todo today page
        When the user adds a task named "Build conference workshop"
        And the user sets the task estimate to 7 pomodoros
        Then the todo today page shows the task "Build conference workshop"
        And the todo today page shows the task estimate as 7 pomodoros
        And the todo today page shows a large task warning

    Scenario Outline: User completes a task relative to the estimate
        Given a logged in user
        And the user is on the todo today page
        And the todo today page shows the task "<task>" with an estimate of 3 pomodoros
        And the task "<task>" has completed <completed> pomodoros
        When the user marks the task "<task>" complete
        Then the todo today page shows the task "<task>" as complete
        And the completed task "<task>" is struck through
        And the completed task "<task>" is greyed out
        And the todo today page shows the task "<task>" completed <status> the estimate

        Examples:
            | task                      | completed | status |
            | Prepare workshop outline  | 2         | under  |
            | Review activity inventory | 3         | on     |
            | Update record sheet       | 4         | over   |
