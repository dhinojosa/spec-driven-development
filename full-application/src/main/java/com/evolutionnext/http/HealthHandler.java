package com.evolutionnext.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public final class HealthHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpResponses.text(exchange, 200, "OK");
    }
}
