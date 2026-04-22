package com.evolutionnext.http;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

class SafeHttpHandlerTest {
    private HttpServer server;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @AfterEach
    void stopServer() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    void returnsInternalServerErrorWhenDelegateThrows() throws Exception {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/boom", new SafeHttpHandler(exchange -> {
            throw new IllegalStateException("boom");
        }));
        server.start();

        var baseUri = URI.create("http://localhost:" + server.getAddress().getPort());
        var response = httpClient.send(HttpRequest.newBuilder(baseUri.resolve("/boom")).GET().build(),
            HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(500);
        assertThat(response.body()).contains("Internal server error");
    }
}
