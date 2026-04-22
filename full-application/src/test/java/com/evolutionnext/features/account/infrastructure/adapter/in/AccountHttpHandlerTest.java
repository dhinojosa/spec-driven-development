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
    void registerPageUsesSharedAccountDesign() throws Exception {
        var response = get("/account/register");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("Register account");
        assertThat(response.body()).contains("/assets/site.css");
        assertThat(response.body()).contains("/assets/welcome-pomodoro-technique.jpg");
        assertThat(response.body()).contains("/assets/welcome-tomato.png");
    }

    @Test
    void loginPageUsesSharedAccountDesign() throws Exception {
        var response = get("/account/login");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("Login");
        assertThat(response.body()).contains("/assets/site.css");
        assertThat(response.body()).contains("/assets/welcome-pomodoro-technique.jpg");
        assertThat(response.body()).contains("/assets/welcome-tomato.png");
    }

    @Test
    void registrationPostCanBeObservedThroughGet() throws Exception {
        var response = post("/account/register", "userName=casey&password=correct-horse-battery-staple");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("Dashboard");
        assertThat(response.body()).contains("/todo-today");
        assertThat(response.body()).contains("/activity-inventory");
        assertThat(response.body()).contains("/record-sheet");
        assertThat(get("/account?userName=" + encode("casey")).body()).contains("casey");
    }

    @Test
    void successfulLoginShowsDashboardPage() throws Exception {
        post("/account/register", "userName=casey&password=correct-horse-battery-staple");
        authenticationCookie = null;

        var response = post("/account/login", "userName=casey&password=correct-horse-battery-staple");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("Dashboard");
        assertThat(response.body()).contains("/todo-today");
        assertThat(response.body()).contains("/activity-inventory");
        assertThat(response.body()).contains("/record-sheet");
    }

    @Test
    void shortPasswordRegistrationShowsValidationOnRegistrationPage() throws Exception {
        var response = post("/account/register", "userName=casey&password=short");

        assertThat(response.statusCode()).isEqualTo(422);
        assertThat(response.body()).contains("Password must be at least 8 characters");
        assertThat(response.body()).contains("name=\"userName\" autocomplete=\"username\" value=\"casey\"");
        assertThat(response.body()).contains("name=\"password\" type=\"password\" autocomplete=\"new-password\" value=\"\"");
    }

    @Test
    void securePagesShowLogoutAndLogoutReturnsHomePage() throws Exception {
        post("/account/register", "userName=casey&password=correct-horse-battery-staple");

        assertThat(get("/dashboard").body()).contains("Log out");
        assertThat(get("/todo-today").body()).contains("Log out");
        assertThat(get("/activity-inventory").body()).contains("Log out");
        assertThat(get("/record-sheet").body()).contains("Log out");

        var response = get("/account/logout");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("One focused session at a time.");
        assertThat(response.body()).doesNotContain("/dashboard");
        assertThat(response.body()).doesNotContain("/todo-today");
        assertThat(response.body()).doesNotContain("/activity-inventory");
        assertThat(response.body()).doesNotContain("/record-sheet");
        assertThat(response.body()).doesNotContain("Log out");
    }

    @Test
    void badCredentialsShowInvalidMessage() throws Exception {
        post("/account/register", "userName=casey&password=correct-horse-battery-staple");
        authenticationCookie = null;

        var response = post("/account/login", "userName=casey&password=wrong-password");

        assertThat(response.statusCode()).isEqualTo(401);
        assertThat(response.body()).contains("Invalid user name or password");
        assertThat(response.body()).contains("/assets/site.css");
        assertThat(response.body()).contains("/assets/welcome-pomodoro-technique.jpg");
    }

    private HttpResponse<String> get(String path) throws Exception {
        return send(HttpRequest.newBuilder(baseUri.resolve(path)).GET());
    }

    private HttpResponse<String> post(String path, String body) throws Exception {
        return send(HttpRequest.newBuilder(baseUri.resolve(path))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(body)));
    }

    private HttpResponse<String> send(HttpRequest.Builder builder) throws Exception {
        if (authenticationCookie != null) {
            builder.header("Cookie", authenticationCookie);
        }
        var response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
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
