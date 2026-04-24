package com.evolutionnext.features.todotoday.application.service;

import com.evolutionnext.features.todotoday.application.command.TodoTodayCommand;
import com.evolutionnext.features.todotoday.application.query.TodoTodayQueryResult;
import com.evolutionnext.features.todotoday.infrastructure.adapter.out.InMemoryLoggedInTodoTodayRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoggedInTodoTodayApplicationServiceTest {
    @Test
    void tasksAddedThroughCommandPortAreVisibleThroughQueryPort() {
        var repository = new InMemoryLoggedInTodoTodayRepository();
        var commandService = new LoggedInTodoTodayCommandApplicationService(repository);
        var queryService = new LoggedInTodoTodayQueryApplicationService(repository);

        commandService.execute(new TodoTodayCommand.AddTask("Prepare workshop outline"));
        commandService.execute(new TodoTodayCommand.SetTaskEstimate("Prepare workshop outline", 3));

        var view = (TodoTodayQueryResult.CurrentTodoTodayView) queryService.currentView();

        assertThat(view.tasks()).singleElement().satisfies(task -> {
            assertThat(task.taskName()).isEqualTo("Prepare workshop outline");
            assertThat(task.estimatedPomodoros()).isEqualTo(3);
            assertThat(task.largeTaskWarning()).isFalse();
        });
    }

    @Test
    void largeTaskWarningAndCompletionStatusAreObservableThroughQueryPort() {
        var repository = new InMemoryLoggedInTodoTodayRepository();
        var commandService = new LoggedInTodoTodayCommandApplicationService(repository);
        var queryService = new LoggedInTodoTodayQueryApplicationService(repository);

        commandService.execute(new TodoTodayCommand.AddTask("Build conference workshop"));
        commandService.execute(new TodoTodayCommand.SetTaskEstimate("Build conference workshop", 7));
        commandService.execute(new TodoTodayCommand.SetCompletedPomodoros("Build conference workshop", 8));
        commandService.execute(new TodoTodayCommand.CompleteTask("Build conference workshop"));

        var view = (TodoTodayQueryResult.CurrentTodoTodayView) queryService.currentView();

        assertThat(view.tasks()).singleElement().satisfies(task -> {
            assertThat(task.largeTaskWarning()).isTrue();
            assertThat(task.complete()).isTrue();
            assertThat(task.completionStatus().name()).isEqualTo("OVER");
        });
    }
}
