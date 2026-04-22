package com.evolutionnext.features.welcome.infrastructure.adapter.in;

import com.evolutionnext.http.HttpResponses;
import com.evolutionnext.http.ResourceLoader;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public final class WelcomeHttpHandler implements HttpHandler {
    private final ResourceLoader resourceLoader;

    public WelcomeHttpHandler(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var method = exchange.getRequestMethod();
        var path = exchange.getRequestURI().getPath();
        if ("GET".equals(method) && "/".equals(path)) {
            HttpResponses.html(exchange, 200, resourceLoader.text("welcome/anonymous/index.html"));
            return;
        }
        HttpResponses.text(exchange, 404, "Not found");
    }
}
