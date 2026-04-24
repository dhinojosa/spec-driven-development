package com.evolutionnext.features.todotoday.domain.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public final class TodoToday {
    private final Map<String, TodoTodayTask> tasks;

    public TodoToday() {
        this(new LinkedHashMap<>());
    }

    private TodoToday(Map<String, TodoTodayTask> tasks) {
        this.tasks = tasks;
    }

    public TodoToday addTask(TodoTodayTaskName taskName) {
        var updatedTasks = new LinkedHashMap<>(tasks);
        updatedTasks.put(taskName.value(), TodoTodayTask.add(taskName));
        return new TodoToday(updatedTasks);
    }

    public TodoToday setEstimate(TodoTodayTaskName taskName, int estimatedPomodoros) {
        return updateTask(taskName, task -> task.setEstimate(estimatedPomodoros));
    }

    public TodoToday setCompletedPomodoros(TodoTodayTaskName taskName, int completedPomodoros) {
        return updateTask(taskName, task -> task.setCompletedPomodoros(completedPomodoros));
    }

    public TodoToday markComplete(TodoTodayTaskName taskName) {
        return updateTask(taskName, TodoTodayTask::markComplete);
    }

    public List<TodoTodayTask> tasks() {
        return List.copyOf(tasks.values());
    }

    public TodoTodayTask taskNamed(TodoTodayTaskName taskName) {
        var task = tasks.get(taskName.value());
        if (task == null) {
            throw new IllegalArgumentException("Task not found: " + taskName.value());
        }
        return task;
    }

    private TodoToday updateTask(TodoTodayTaskName taskName, UnaryOperator<TodoTodayTask> operator) {
        var existingTask = taskNamed(taskName);
        var updatedTasks = new LinkedHashMap<>(tasks);
        updatedTasks.put(taskName.value(), operator.apply(existingTask));
        return new TodoToday(updatedTasks);
    }
}
