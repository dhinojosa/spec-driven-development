package com.evolutionnext.features.todotoday.infrastructure.adapter.in;

import com.evolutionnext.features.todotoday.application.command.TodoTodayCommand;
import com.evolutionnext.features.todotoday.application.query.TodoTodayQueryResult;
import com.evolutionnext.features.todotoday.port.in.LoggedInTodoTodayCommandPort;
import com.evolutionnext.features.todotoday.port.in.LoggedInTodoTodayQueryPort;
import com.evolutionnext.http.AuthCookies;
import com.evolutionnext.http.FormParser;
import com.evolutionnext.http.HttpResponses;
import com.evolutionnext.http.ResourceLoader;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class TodoTodayHttpHandler implements HttpHandler {
    private final LoggedInTodoTodayCommandPort commandPort;
    private final LoggedInTodoTodayQueryPort queryPort;
    private final ResourceLoader resourceLoader;

    public TodoTodayHttpHandler(LoggedInTodoTodayCommandPort commandPort,
                                LoggedInTodoTodayQueryPort queryPort,
                                ResourceLoader resourceLoader) {
        this.commandPort = commandPort;
        this.queryPort = queryPort;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!AuthCookies.isAuthenticated(exchange)) {
            HttpResponses.html(exchange, 401, resourceLoader.text("welcome/anonymous/index.html"));
            return;
        }

        var method = exchange.getRequestMethod();
        var path = exchange.getRequestURI().getPath();
        if ("GET".equals(method) && "/todo-today".equals(path)) {
            HttpResponses.html(exchange, 200, todoTodayPage());
        } else if ("POST".equals(method) && "/todo-today/task".equals(path)) {
            addTask(exchange);
        } else if ("POST".equals(method) && "/todo-today/task/estimate".equals(path)) {
            setEstimate(exchange);
        } else if ("POST".equals(method) && "/todo-today/task/completed-pomodoros".equals(path)) {
            setCompletedPomodoros(exchange);
        } else if ("POST".equals(method) && "/todo-today/task/complete".equals(path)) {
            completeTask(exchange);
        } else {
            HttpResponses.text(exchange, 404, "Not found");
        }
    }

    private void addTask(HttpExchange exchange) throws IOException {
        var form = form(exchange);
        commandPort.execute(new TodoTodayCommand.AddTask(form.getOrDefault("taskName", "")));
        HttpResponses.html(exchange, 200, todoTodayPage());
    }

    private void setEstimate(HttpExchange exchange) throws IOException {
        var form = form(exchange);
        commandPort.execute(new TodoTodayCommand.SetTaskEstimate(
            form.getOrDefault("taskName", ""),
            Integer.parseInt(form.getOrDefault("estimatedPomodoros", "0"))));
        HttpResponses.html(exchange, 200, todoTodayPage());
    }

    private void setCompletedPomodoros(HttpExchange exchange) throws IOException {
        var form = form(exchange);
        commandPort.execute(new TodoTodayCommand.SetCompletedPomodoros(
            form.getOrDefault("taskName", ""),
            Integer.parseInt(form.getOrDefault("completedPomodoros", "0"))));
        HttpResponses.html(exchange, 200, todoTodayPage());
    }

    private void completeTask(HttpExchange exchange) throws IOException {
        var form = form(exchange);
        commandPort.execute(new TodoTodayCommand.CompleteTask(form.getOrDefault("taskName", "")));
        HttpResponses.html(exchange, 200, todoTodayPage());
    }

    private String todoTodayPage() {
        var currentView = (TodoTodayQueryResult.CurrentTodoTodayView) queryPort.currentView();
        var taskMarkup = currentView.tasks().stream()
            .map(task -> """
                <li class="todo-task%s">
                    <div class="todo-task__summary">
                        <span class="todo-task__name">%s</span>
                        <span class="todo-task__estimate">%d pomodoros</span>
                    </div>
                    %s
                    %s
                    <div class="todo-task__controls">
                        <form class="account-form todo-task__estimate-form" method="post" action="/todo-today/task/estimate">
                            <input type="hidden" name="taskName" value="%s">
                            <label>Estimate <input name="estimatedPomodoros" type="number" min="0" value="%d"></label>
                            <button type="submit">Set estimate</button>
                        </form>
                        <form class="account-form todo-task__completed-form" method="post" action="/todo-today/task/completed-pomodoros">
                            <input type="hidden" name="taskName" value="%s">
                            <label>Completed pomodoros <input name="completedPomodoros" type="number" min="0" value="%d"></label>
                            <button type="submit">Set completed pomodoros</button>
                        </form>
                        <form class="todo-task__complete-form" method="post" action="/todo-today/task/complete">
                            <input type="hidden" name="taskName" value="%s">
                            <button type="submit">Complete Task</button>
                        </form>
                    </div>
                </li>
                """.formatted(
                task.complete() ? " todo-task--complete" : "",
                escapeHtml(task.taskName()),
                task.estimatedPomodoros(),
                task.largeTaskWarning() ? "<p class=\"todo-task__warning\">Large task warning</p>" : "",
                task.complete()
                    ? "<p class=\"todo-task__status\">Completed " + task.completionStatus().name().toLowerCase()
                    + " the estimate</p>"
                    : "",
                escapeHtml(task.taskName()),
                task.estimatedPomodoros(),
                escapeHtml(task.taskName()),
                task.completedPomodoros(),
                escapeHtml(task.taskName())))
            .reduce("", String::concat);
        return resourceLoader.text("account/todo-today.html")
            .replace("{{TODO_EMPTY_STATE}}", currentView.tasks().isEmpty()
                ? "<p class=\"todo-empty\">No tasks yet for today.</p>"
                : "")
            .replace("{{TODO_TASKS}}", taskMarkup);
    }

    private static java.util.Map<String, String> form(HttpExchange exchange) throws IOException {
        return FormParser.parse(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
    }

    private static String escapeHtml(String value) {
        return value
            .replace("&", "&amp;")
            .replace("\"", "&quot;")
            .replace("<", "&lt;")
            .replace(">", "&gt;");
    }
}
