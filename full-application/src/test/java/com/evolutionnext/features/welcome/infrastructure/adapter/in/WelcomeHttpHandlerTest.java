package com.evolutionnext.features.welcome.infrastructure.adapter.in;

import com.evolutionnext.AccountApplication;
import com.evolutionnext.features.account.infrastructure.adapter.out.InMemoryAccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

class WelcomeHttpHandlerTest {
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
    void rootRouteReturnsWelcomePage() throws Exception {
        var response = get("/");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.headers().firstValue("Content-Type"))
            .hasValueSatisfying(contentType -> assertThat(contentType).contains("text/html"));
        assertThat(response.body()).contains("One focused session at a time.");
    }

    @Test
    void homePageReferencesPomodoroTimeImageAsset() throws Exception {
        var response = get("/");

        assertThat(response.body()).contains("/assets/welcome-pomodoro-technique.jpg");
    }

    @Test
    void homePageReferencesTomatoImageAsset() throws Exception {
        var response = get("/");

        assertThat(response.body()).contains("/assets/welcome-tomato.png");
    }

    @Test
    void staticAssetHandlerServesTomatoImage() throws Exception {
        var response = get("/assets/welcome-tomato.png");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.headers().firstValue("Content-Type"))
            .hasValueSatisfying(contentType -> assertThat(contentType).contains("image/png"));
    }

    @Test
    void staticAssetHandlerServesPomodoroTimeImage() throws Exception {
        var response = get("/assets/welcome-pomodoro-technique.jpg");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.headers().firstValue("Content-Type"))
            .hasValueSatisfying(contentType -> assertThat(contentType).contains("image/jpeg"));
    }

    private HttpResponse<String> get(String path) throws Exception {
        return httpClient.send(HttpRequest.newBuilder(baseUri.resolve(path)).GET().build(),
            HttpResponse.BodyHandlers.ofString());
    }
}
