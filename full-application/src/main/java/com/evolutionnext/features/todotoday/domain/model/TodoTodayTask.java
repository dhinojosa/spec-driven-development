package com.evolutionnext.features.todotoday.domain.model;

public record TodoTodayTask(TodoTodayTaskName taskName,
                            int estimatedPomodoros,
                            int completedPomodoros,
                            boolean complete) {
    public TodoTodayTask {
        if (estimatedPomodoros < 0) {
            throw new IllegalArgumentException("Estimate must be at least 0 pomodoros");
        }
        if (completedPomodoros < 0) {
            throw new IllegalArgumentException("Completed pomodoros must be at least 0");
        }
    }

    public static TodoTodayTask add(TodoTodayTaskName taskName) {
        return new TodoTodayTask(taskName, 0, 0, false);
    }

    public TodoTodayTask setEstimate(int estimatedPomodoros) {
        return new TodoTodayTask(taskName, estimatedPomodoros, completedPomodoros, complete);
    }

    public TodoTodayTask setCompletedPomodoros(int completedPomodoros) {
        return new TodoTodayTask(taskName, estimatedPomodoros, completedPomodoros, complete);
    }

    public TodoTodayTask markComplete() {
        return new TodoTodayTask(taskName, estimatedPomodoros, completedPomodoros, true);
    }

    public boolean hasLargeTaskWarning() {
        return estimatedPomodoros > 6;
    }

    public TodoTodayCompletionStatus completionStatus() {
        if (completedPomodoros < estimatedPomodoros) {
            return TodoTodayCompletionStatus.UNDER;
        }
        if (completedPomodoros == estimatedPomodoros) {
            return TodoTodayCompletionStatus.ON;
        }
        return TodoTodayCompletionStatus.OVER;
    }
}
