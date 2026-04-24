package com.evolutionnext.features.todotoday.port.in;

import com.evolutionnext.features.todotoday.application.command.TodoTodayCommand;
import com.evolutionnext.features.todotoday.application.command.TodoTodayResult;

public interface LoggedInTodoTodayCommandPort {
    TodoTodayResult execute(TodoTodayCommand command);
}
