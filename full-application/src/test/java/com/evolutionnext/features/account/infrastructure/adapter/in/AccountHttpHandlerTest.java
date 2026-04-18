package com.evolutionnext.features.account.infrastructure.adapter.in;

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

class AccountHttpHandlerTest {
    private com.sun.net.httpserver.HttpServer server;
    private URI baseUri;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @BeforeEach
    void startServer() {
        server = new AccountApplication().start(0, new InMemoryAccountRepository());
        baseUri = URI.create("http://localhost:" + server.getAddress().getPort());
    }

    @AfterEach
    void stopServer() {
        server.stop(0);
    }

    @Test
    void registrationPostCanBeObservedThroughGet() throws Exception {
        var response = post("/account/register", "userName=casey&password=correct-horse-battery-staple");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(get("/account?userName=" + encode("casey")).body()).contains("casey");
    }

    @Test
    void successfulLoginShowsPomodoroPage() throws Exception {
        post("/account/register", "userName=casey&password=correct-horse-battery-staple");

        var response = post("/account/login", "userName=casey&password=correct-horse-battery-staple");

        assertThat(response.body()).contains("Pomodoro");
    }

    @Test
    void badCredentialsShowInvalidMessage() throws Exception {
        post("/account/register", "userName=casey&password=correct-horse-battery-staple");

        var response = post("/account/login", "userName=casey&password=wrong-password");

        assertThat(response.statusCode()).isEqualTo(401);
        assertThat(response.body()).contains("Invalid user name or password");
    }

    private HttpResponse<String> get(String path) throws Exception {
        return httpClient.send(HttpRequest.newBuilder(baseUri.resolve(path)).GET().build(),
            HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> post(String path, String body) throws Exception {
        return httpClient.send(HttpRequest.newBuilder(baseUri.resolve(path))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build(),
            HttpResponse.BodyHandlers.ofString());
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
