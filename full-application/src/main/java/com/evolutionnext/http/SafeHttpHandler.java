package com.evolutionnext.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public final class SafeHttpHandler implements HttpHandler {
    private final HttpHandler delegate;

    public SafeHttpHandler(HttpHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            delegate.handle(exchange);
        } catch (Exception exception) {
            System.err.printf("Request handling failed for %s %s%n",
                exchange.getRequestMethod(),
                exchange.getRequestURI());
            exception.printStackTrace(System.err);
            try {
                HttpResponses.text(exchange, 500, "Internal server error");
            } catch (IOException responseException) {
                responseException.printStackTrace(System.err);
                throw responseException;
            }
        }
    }
}
