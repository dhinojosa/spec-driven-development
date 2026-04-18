package com.evolutionnext.http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class HttpResponses {
    private HttpResponses() {
    }

    public static void html(HttpExchange exchange, int status, String body) throws IOException {
        bytes(exchange, status, "text/html; charset=utf-8", body.getBytes(StandardCharsets.UTF_8));
    }

    public static void text(HttpExchange exchange, int status, String body) throws IOException {
        bytes(exchange, status, "text/plain; charset=utf-8", body.getBytes(StandardCharsets.UTF_8));
    }

    public static void bytes(HttpExchange exchange, int status, String contentType, byte[] body) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(status, body.length);
        try (var responseBody = exchange.getResponseBody()) {
            responseBody.write(body);
        }
    }
}
