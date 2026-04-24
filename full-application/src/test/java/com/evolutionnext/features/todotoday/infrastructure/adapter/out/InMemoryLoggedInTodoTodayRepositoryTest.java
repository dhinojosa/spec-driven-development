package com.evolutionnext.features.todotoday.infrastructure.adapter.out;

import com.evolutionnext.features.todotoday.domain.model.TodoToday;
import com.evolutionnext.features.todotoday.domain.model.TodoTodayTaskName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryLoggedInTodoTodayRepositoryTest {
    @Test
    void savedTodoTodayStateCanBeReadBack() {
        var repository = new InMemoryLoggedInTodoTodayRepository();
        var todoToday = new TodoToday()
            .addTask(new TodoTodayTaskName("Prepare workshop outline"))
            .setEstimate(new TodoTodayTaskName("Prepare workshop outline"), 3);

        repository.save(todoToday);

        assertThat(repository.current().taskNamed(new TodoTodayTaskName("Prepare workshop outline")).estimatedPomodoros())
            .isEqualTo(3);
    }
}
