package com.evolutionnext.features.todotoday.port.in;

import com.evolutionnext.features.todotoday.application.query.TodoTodayQueryResult;

public interface LoggedInTodoTodayQueryPort {
    TodoTodayQueryResult currentView();
}
