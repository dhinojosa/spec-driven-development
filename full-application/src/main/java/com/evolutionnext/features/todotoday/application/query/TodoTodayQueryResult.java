package com.evolutionnext.features.todotoday.application.query;

import com.evolutionnext.features.todotoday.domain.model.TodoTodayCompletionStatus;

import java.util.List;

public sealed interface TodoTodayQueryResult permits TodoTodayQueryResult.CurrentTodoTodayView {
    record CurrentTodoTodayView(List<TodoTodayTaskView> tasks) implements TodoTodayQueryResult {
    }

    record TodoTodayTaskView(String taskName,
                             int estimatedPomodoros,
                             int completedPomodoros,
                             boolean largeTaskWarning,
                             boolean complete,
                             TodoTodayCompletionStatus completionStatus) {
    }
}
