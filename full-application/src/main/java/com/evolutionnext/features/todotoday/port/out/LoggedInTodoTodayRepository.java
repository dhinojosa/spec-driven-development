package com.evolutionnext.features.todotoday.port.out;

import com.evolutionnext.features.todotoday.domain.model.TodoToday;

public interface LoggedInTodoTodayRepository {
    TodoToday current();

    void save(TodoToday todoToday);
}
