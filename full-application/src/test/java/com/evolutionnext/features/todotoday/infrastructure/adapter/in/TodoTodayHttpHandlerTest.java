package com.evolutionnext.features.todotoday.infrastructure.adapter.in;

import com.evolutionnext.AccountApplication;
import com.evolutionnext.features.account.infrastructure.adapter.out.InMemoryAccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class TodoTodayHttpHandlerTest {
    private com.sun.net.httpserver.HttpServer server;
    private URI baseUri;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private String authenticationCookie;

    @BeforeEach
    void startServer() {
        server = new AccountApplication().start(0, new InMemoryAccountRepository());
        baseUri = URI.create("http://localhost:" + server.getAddress().getPort());
    }

    @AfterEach
    void stopServer() {
        server.stop(0);
        authenticationCookie = null;
    }

    @Test
    void todoTodayPageSupportsAddingTasksAndSettingEstimate() throws Exception {
        registerAndStayLoggedIn();

        post("/todo-today/task", "taskName=" + encode("Prepare workshop outline"));
        var response = post("/todo-today/task/estimate",
            "taskName=%s&estimatedPomodoros=3".formatted(encode("Prepare workshop outline")));

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("Prepare workshop outline");
        assertThat(response.body()).contains("3 pomodoros");
        assertThat(response.body()).contains("Set estimate");
    }

    @Test
    void estimatesAboveSixShowLargeTaskWarning() throws Exception {
        registerAndStayLoggedIn();

        post("/todo-today/task", "taskName=" + encode("Build conference workshop"));
        var response = post("/todo-today/task/estimate",
            "taskName=%s&estimatedPomodoros=7".formatted(encode("Build conference workshop")));

        assertThat(response.body()).contains("Large task warning");
    }

    @Test
    void completedTasksShowStatusAndCompletedStyling() throws Exception {
        registerAndStayLoggedIn();

        post("/todo-today/task", "taskName=" + encode("Review activity inventory"));
        post("/todo-today/task/estimate",
            "taskName=%s&estimatedPomodoros=3".formatted(encode("Review activity inventory")));
        post("/todo-today/task/completed-pomodoros",
            "taskName=%s&completedPomodoros=3".formatted(encode("Review activity inventory")));
        var response = post("/todo-today/task/complete", "taskName=" + encode("Review activity inventory"));

        assertThat(response.body()).contains("todo-task--complete");
        assertThat(response.body()).contains("Completed on the estimate");
        assertThat(response.body()).contains("Complete Task");
    }

    private void registerAndStayLoggedIn() throws Exception {
        post("/account/register", "userName=casey&password=correct-horse-battery-staple");
    }

    private HttpResponse<String> post(String path, String body) throws Exception {
        var builder = HttpRequest.newBuilder(baseUri.resolve(path))
            .header("Content-Type", "application/x-www-form-urlencoded");
        if (authenticationCookie != null) {
            builder.header("Cookie", authenticationCookie);
        }
        var response = httpClient.send(builder.POST(HttpRequest.BodyPublishers.ofString(body)).build(),
            HttpResponse.BodyHandlers.ofString());
        response.headers().firstValue("Set-Cookie").ifPresent(this::rememberAuthenticationCookie);
        return response;
    }

    private void rememberAuthenticationCookie(String setCookieHeader) {
        var cookie = setCookieHeader.split(";", 2)[0];
        if (cookie.endsWith("=")) {
            authenticationCookie = null;
        } else {
            authenticationCookie = cookie;
        }
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
