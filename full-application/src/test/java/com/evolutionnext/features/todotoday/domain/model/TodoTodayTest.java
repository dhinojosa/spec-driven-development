package com.evolutionnext.features.todotoday.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TodoTodayTest {
    @Test
    void tasksCanBeAddedEstimatedAndCompleted() {
        var todoToday = new TodoToday()
            .addTask(new TodoTodayTaskName("Prepare workshop outline"))
            .setEstimate(new TodoTodayTaskName("Prepare workshop outline"), 3)
            .setCompletedPomodoros(new TodoTodayTaskName("Prepare workshop outline"), 2)
            .markComplete(new TodoTodayTaskName("Prepare workshop outline"));

        var task = todoToday.taskNamed(new TodoTodayTaskName("Prepare workshop outline"));

        assertThat(task.estimatedPomodoros()).isEqualTo(3);
        assertThat(task.completedPomodoros()).isEqualTo(2);
        assertThat(task.complete()).isTrue();
        assertThat(task.completionStatus()).isEqualTo(TodoTodayCompletionStatus.UNDER);
    }

    @Test
    void tasksAboveSixPomodorosShowLargeTaskWarning() {
        var task = TodoTodayTask.add(new TodoTodayTaskName("Build conference workshop"))
            .setEstimate(7);

        assertThat(task.hasLargeTaskWarning()).isTrue();
    }
}
