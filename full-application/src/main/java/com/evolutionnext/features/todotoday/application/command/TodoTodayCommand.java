package com.evolutionnext.features.todotoday.application.command;

public sealed interface TodoTodayCommand permits TodoTodayCommand.AddTask,
    TodoTodayCommand.SetTaskEstimate,
    TodoTodayCommand.SetCompletedPomodoros,
    TodoTodayCommand.CompleteTask {
    record AddTask(String taskName) implements TodoTodayCommand {
    }

    record SetTaskEstimate(String taskName, int estimatedPomodoros) implements TodoTodayCommand {
    }

    record SetCompletedPomodoros(String taskName, int completedPomodoros) implements TodoTodayCommand {
    }

    record CompleteTask(String taskName) implements TodoTodayCommand {
    }
}
