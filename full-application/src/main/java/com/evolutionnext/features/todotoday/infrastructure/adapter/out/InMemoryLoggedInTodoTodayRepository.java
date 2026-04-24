package com.evolutionnext.features.todotoday.infrastructure.adapter.out;

import com.evolutionnext.features.todotoday.domain.model.TodoToday;
import com.evolutionnext.features.todotoday.port.out.LoggedInTodoTodayRepository;

public final class InMemoryLoggedInTodoTodayRepository implements LoggedInTodoTodayRepository {
    private TodoToday todoToday = new TodoToday();

    @Override
    public TodoToday current() {
        return todoToday;
    }

    @Override
    public void save(TodoToday todoToday) {
        this.todoToday = todoToday;
    }
}
