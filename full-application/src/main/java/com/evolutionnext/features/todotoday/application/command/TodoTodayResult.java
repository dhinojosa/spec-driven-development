package com.evolutionnext.features.todotoday.application.command;

public sealed interface TodoTodayResult permits TodoTodayResult.TaskAdded,
    TodoTodayResult.TaskEstimateSet,
    TodoTodayResult.TaskCompletedPomodorosSet,
    TodoTodayResult.TaskMarkedComplete,
    TodoTodayResult.TaskNotFound {
    record TaskAdded(String taskName) implements TodoTodayResult {
    }

    record TaskEstimateSet(String taskName, int estimatedPomodoros) implements TodoTodayResult {
    }

    record TaskCompletedPomodorosSet(String taskName, int completedPomodoros) implements TodoTodayResult {
    }

    record TaskMarkedComplete(String taskName) implements TodoTodayResult {
    }

    record TaskNotFound(String taskName) implements TodoTodayResult {
    }
}
