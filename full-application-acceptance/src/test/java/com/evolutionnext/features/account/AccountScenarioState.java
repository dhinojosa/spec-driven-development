package com.evolutionnext.features.account;

import com.sun.net.httpserver.HttpServer;
import io.cucumber.guice.ScenarioScoped;

import java.net.URI;
import java.net.http.HttpResponse;

@ScenarioScoped
public final class AccountScenarioState {
    private HttpServer server;
    private URI baseUri;
    private HttpResponse<String> lastResponse;
    private String lastUserName;
    private String authenticationCookie;

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

    public void rememberLastUserName(String lastUserName) {
        this.lastUserName = lastUserName;
    }

    public String lastUserName() {
        return lastUserName;
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

    public void stopServer() {
        if (server != null) {
            server.stop(0);
        }
    }
}
