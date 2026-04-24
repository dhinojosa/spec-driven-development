package com.evolutionnext.features.todotoday.application.service;

import com.evolutionnext.features.todotoday.application.query.TodoTodayQueryResult;
import com.evolutionnext.features.todotoday.port.in.LoggedInTodoTodayQueryPort;
import com.evolutionnext.features.todotoday.port.out.LoggedInTodoTodayRepository;

public final class LoggedInTodoTodayQueryApplicationService implements LoggedInTodoTodayQueryPort {
    private final LoggedInTodoTodayRepository repository;

    public LoggedInTodoTodayQueryApplicationService(LoggedInTodoTodayRepository repository) {
        this.repository = repository;
    }

    @Override
    public TodoTodayQueryResult currentView() {
        return new TodoTodayQueryResult.CurrentTodoTodayView(repository.current().tasks().stream()
            .map(task -> new TodoTodayQueryResult.TodoTodayTaskView(
                task.taskName().value(),
                task.estimatedPomodoros(),
                task.completedPomodoros(),
                task.hasLargeTaskWarning(),
                task.complete(),
                task.completionStatus()))
            .toList());
    }
}
