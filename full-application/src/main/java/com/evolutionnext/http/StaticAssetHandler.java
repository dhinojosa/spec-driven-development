package com.evolutionnext.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URLConnection;

public final class StaticAssetHandler implements HttpHandler {
    private final ResourceLoader resourceLoader;

    public StaticAssetHandler(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var resourceName = exchange.getRequestURI().getPath().replaceFirst("^/assets/", "assets/");
        var bytes = resourceLoader.bytes(resourceName);
        if (bytes == null) {
            HttpResponses.text(exchange, 404, "Not found");
            return;
        }
        var contentType = URLConnection.guessContentTypeFromName(resourceName);
        HttpResponses.bytes(exchange, 200, contentType == null ? "application/octet-stream" : contentType, bytes);
    }
}
