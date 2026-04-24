package com.evolutionnext.features.todotoday;

import com.sun.net.httpserver.HttpServer;
import io.cucumber.guice.ScenarioScoped;

import java.net.URI;
import java.net.http.HttpResponse;

@ScenarioScoped
public final class TodoTodayScenarioState {
    private HttpServer server;
    private URI baseUri;
    private HttpResponse<String> lastResponse;
    private String authenticationCookie;
    private String currentTaskName;

    public void rememberServer(HttpServer server) {
        this.server = server;
        this.baseUri = URI.create("http://localhost:" + server.getAddress().getPort());
    }

    public URI baseUri() {
        return baseUri;
    }

    public void rememberLastResponse(HttpResponse<String> lastResponse) {
        this.lastResponse = lastResponse;
    }

    public HttpResponse<String> lastResponse() {
        return lastResponse;
    }

    public void rememberAuthenticationCookie(String authenticationCookie) {
        this.authenticationCookie = authenticationCookie;
    }

    public String authenticationCookie() {
        return authenticationCookie;
    }

    public void clearAuthenticationCookie() {
        this.authenticationCookie = null;
    }

    public void rememberCurrentTaskName(String currentTaskName) {
        this.currentTaskName = currentTaskName;
    }

    public String currentTaskName() {
        return currentTaskName;
    }

    public void stopServer() {
        if (server != null) {
            server.stop(0);
        }
    }
}
