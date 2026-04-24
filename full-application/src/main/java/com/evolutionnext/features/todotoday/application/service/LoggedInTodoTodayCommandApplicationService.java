package com.evolutionnext.features.todotoday.application.service;

import com.evolutionnext.features.todotoday.application.command.TodoTodayCommand;
import com.evolutionnext.features.todotoday.application.command.TodoTodayResult;
import com.evolutionnext.features.todotoday.domain.model.TodoTodayTaskName;
import com.evolutionnext.features.todotoday.port.in.LoggedInTodoTodayCommandPort;
import com.evolutionnext.features.todotoday.port.out.LoggedInTodoTodayRepository;

public final class LoggedInTodoTodayCommandApplicationService implements LoggedInTodoTodayCommandPort {
    private final LoggedInTodoTodayRepository repository;

    public LoggedInTodoTodayCommandApplicationService(LoggedInTodoTodayRepository repository) {
        this.repository = repository;
    }

    @Override
    public TodoTodayResult execute(TodoTodayCommand command) {
        return switch (command) {
            case TodoTodayCommand.AddTask(var taskName) -> {
                var todoToday = repository.current().addTask(new TodoTodayTaskName(taskName));
                repository.save(todoToday);
                yield new TodoTodayResult.TaskAdded(taskName);
            }
            case TodoTodayCommand.SetTaskEstimate(var taskName, var estimatedPomodoros) -> {
                try {
                    var todoToday = repository.current().setEstimate(new TodoTodayTaskName(taskName), estimatedPomodoros);
                    repository.save(todoToday);
                    yield new TodoTodayResult.TaskEstimateSet(taskName, estimatedPomodoros);
                } catch (IllegalArgumentException exception) {
                    yield new TodoTodayResult.TaskNotFound(taskName);
                }
            }
            case TodoTodayCommand.SetCompletedPomodoros(var taskName, var completedPomodoros) -> {
                try {
                    var todoToday = repository.current()
                        .setCompletedPomodoros(new TodoTodayTaskName(taskName), completedPomodoros);
                    repository.save(todoToday);
                    yield new TodoTodayResult.TaskCompletedPomodorosSet(taskName, completedPomodoros);
                } catch (IllegalArgumentException exception) {
                    yield new TodoTodayResult.TaskNotFound(taskName);
                }
            }
            case TodoTodayCommand.CompleteTask(var taskName) -> {
                try {
                    var todoToday = repository.current().markComplete(new TodoTodayTaskName(taskName));
                    repository.save(todoToday);
                    yield new TodoTodayResult.TaskMarkedComplete(taskName);
                } catch (IllegalArgumentException exception) {
                    yield new TodoTodayResult.TaskNotFound(taskName);
                }
            }
        };
    }
}
