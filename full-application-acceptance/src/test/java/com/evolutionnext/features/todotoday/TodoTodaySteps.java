package com.evolutionnext.features.todotoday;

import com.evolutionnext.AccountApplication;
import com.evolutionnext.features.account.infrastructure.adapter.out.InMemoryAccountRepository;
import com.google.inject.Inject;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public final class TodoTodaySteps {
    private final TodoTodayScenarioState state;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Inject
    public TodoTodaySteps(TodoTodayScenarioState state) {
        this.state = state;
    }

    @Before
    public void startApplication() {
        state.rememberServer(new AccountApplication().start(0, new InMemoryAccountRepository()));
    }

    @After
    public void stopApplication() {
        state.stopServer();
    }

    @Given("a logged in user")
    public void aLoggedInUser() throws Exception {
        post("/account/register", "userName=casey&password=correct-horse-battery-staple");
        state.rememberLastResponse(post("/account/login", "userName=casey&password=correct-horse-battery-staple"));
        assertThat(state.lastResponse().body()).contains("Dashboard");
    }

    @And("the user is on the todo today page")
    public void userIsOnTheTodoTodayPage() throws Exception {
        state.rememberLastResponse(get("/todo-today"));
        assertThat(state.lastResponse().statusCode()).isEqualTo(200);
        assertThat(state.lastResponse().body()).contains("Todo today page");
    }

    @When("the user adds a task named {string}")
    public void userAddsTaskNamed(String taskName) throws Exception {
        state.rememberCurrentTaskName(taskName);
        state.rememberLastResponse(post("/todo-today/task", "taskName=" + encode(taskName)));
    }

    @And("the user sets the task estimate to {int} pomodoros")
    public void userSetsTheTaskEstimateToPomodoros(int estimate) throws Exception {
        state.rememberLastResponse(post("/todo-today/task/estimate",
            "taskName=%s&estimatedPomodoros=%d".formatted(encode(state.currentTaskName()), estimate)));
    }

    @Then("the todo today page shows the task {string}")
    public void todoTodayPageShowsTheTask(String taskName) {
        assertThat(state.lastResponse().body()).contains(taskName);
    }

    @And("the todo today page shows the task estimate as {int} pomodoros")
    public void todoTodayPageShowsTheTaskEstimateAsPomodoros(int estimate) {
        assertThat(state.lastResponse().body()).contains(estimate + " pomodoros");
    }

    @And("the todo today page shows a large task warning")
    public void todoTodayPageShowsALargeTaskWarning() {
        assertThat(state.lastResponse().body()).contains("Large task warning");
    }

    @And("the todo today page shows the task {string} with an estimate of {int} pomodoros")
    public void todoTodayPageShowsTheTaskWithEstimate(String taskName, int estimate) throws Exception {
        userAddsTaskNamed(taskName);
        userSetsTheTaskEstimateToPomodoros(estimate);
    }

    @And("the task {string} has completed {int} pomodoros")
    public void taskHasCompletedPomodoros(String taskName, int completedPomodoros) throws Exception {
        state.rememberCurrentTaskName(taskName);
        state.rememberLastResponse(post("/todo-today/task/completed-pomodoros",
            "taskName=%s&completedPomodoros=%d".formatted(encode(taskName), completedPomodoros)));
    }

    @When("the user marks the task {string} complete")
    public void userMarksTheTaskComplete(String taskName) throws Exception {
        state.rememberCurrentTaskName(taskName);
        state.rememberLastResponse(post("/todo-today/task/complete", "taskName=" + encode(taskName)));
    }

    @Then("the todo today page shows the task {string} as complete")
    public void todoTodayPageShowsTheTaskAsComplete(String taskName) {
        assertThat(state.lastResponse().body()).contains(taskName);
        assertThat(state.lastResponse().body()).contains("todo-task--complete");
    }

    @And("the completed task {string} is struck through")
    public void completedTaskIsStruckThrough(String taskName) {
        assertThat(state.lastResponse().body()).contains("todo-task--complete");
        assertThat(state.lastResponse().body()).contains(taskName);
    }

    @And("the completed task {string} is greyed out")
    public void completedTaskIsGreyedOut(String taskName) {
        assertThat(state.lastResponse().body()).contains("todo-task--complete");
    }

    @And("the todo today page shows the task {string} completed {word} the estimate")
    public void todoTodayPageShowsCompletionStatus(String taskName, String status) {
        assertThat(state.lastResponse().body()).contains("Completed " + status + " the estimate");
        assertThat(state.lastResponse().body()).contains(taskName);
    }

    private HttpResponse<String> get(String path) throws Exception {
        return send(HttpRequest.newBuilder(state.baseUri().resolve(path)).GET());
    }

    private HttpResponse<String> post(String path, String body) throws Exception {
        return send(HttpRequest.newBuilder(state.baseUri().resolve(path))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(body)));
    }

    private HttpResponse<String> send(HttpRequest.Builder builder) throws Exception {
        if (state.authenticationCookie() != null) {
            builder.header("Cookie", state.authenticationCookie());
        }
        var response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        response.headers().firstValue("Set-Cookie").ifPresent(this::rememberAuthenticationCookie);
        return response;
    }

    private void rememberAuthenticationCookie(String setCookieHeader) {
        var cookie = setCookieHeader.split(";", 2)[0];
        if (cookie.endsWith("=")) {
            state.clearAuthenticationCookie();
        } else {
            state.rememberAuthenticationCookie(cookie);
        }
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
