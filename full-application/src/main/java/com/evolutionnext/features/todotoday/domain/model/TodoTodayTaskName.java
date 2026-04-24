package com.evolutionnext.features.todotoday.domain.model;

public record TodoTodayTaskName(String value) {
    public TodoTodayTaskName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Task name is required");
        }
    }
}
