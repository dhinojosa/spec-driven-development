package com.evolutionnext.features.account.infrastructure.adapter.in;

import com.evolutionnext.features.account.application.command.AccountCommand;
import com.evolutionnext.features.account.application.command.AccountResult;
import com.evolutionnext.features.account.application.query.AccountQueryResult;
import com.evolutionnext.features.account.domain.model.AccountId;
import com.evolutionnext.features.account.port.in.AnonymousAccountCommandPort;
import com.evolutionnext.features.account.port.in.AnonymousAccountQueryPort;
import com.evolutionnext.http.FormParser;
import com.evolutionnext.http.HttpResponses;
import com.evolutionnext.http.ResourceLoader;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public final class AccountHttpHandler implements HttpHandler {
    private final AnonymousAccountCommandPort commandPort;
    private final AnonymousAccountQueryPort queryPort;
    private final ResourceLoader resourceLoader;

    public AccountHttpHandler(AnonymousAccountCommandPort commandPort,
                              AnonymousAccountQueryPort queryPort,
                              ResourceLoader resourceLoader) {
        this.commandPort = commandPort;
        this.queryPort = queryPort;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var method = exchange.getRequestMethod();
        var path = exchange.getRequestURI().getPath();
        if ("GET".equals(method) && "/account/register".equals(path)) {
            HttpResponses.html(exchange, 200, resourceLoader.text("account/anonymous/register.html"));
        } else if ("POST".equals(method) && "/account/register".equals(path)) {
            register(exchange);
        } else if ("GET".equals(method) && "/account/login".equals(path)) {
            HttpResponses.html(exchange, 200, resourceLoader.text("account/anonymous/login.html"));
        } else if ("POST".equals(method) && "/account/login".equals(path)) {
            logIn(exchange);
        } else if ("GET".equals(method) && "/account/pomodoro".equals(path)) {
            HttpResponses.html(exchange, 200, resourceLoader.text("account/pomodoro.html"));
        } else if ("GET".equals(method) && "/account".equals(path)) {
            findAccount(exchange);
        } else {
            HttpResponses.text(exchange, 404, "Not found");
        }
    }

    private void register(HttpExchange exchange) throws IOException {
        var form = form(exchange);
        var result = commandPort.execute(new AccountCommand.RegisterAccount(AccountId.newId(),
            form.getOrDefault("userName", ""),
            form.getOrDefault("password", "")));
        if (result instanceof AccountResult.AccountRegistered) {
            HttpResponses.html(exchange, 200, resourceLoader.text("account/dashboard.html"));
        } else {
            HttpResponses.html(exchange, 409, resourceLoader.text("account/anonymous/register.html"));
        }
    }

    private void logIn(HttpExchange exchange) throws IOException {
        var form = form(exchange);
        var result = commandPort.execute(new AccountCommand.LogIn(form.getOrDefault("userName", ""),
            form.getOrDefault("password", "")));
        if (result instanceof AccountResult.LogInSucceeded) {
            HttpResponses.html(exchange, 200, resourceLoader.text("account/dashboard.html"));
        } else {
            HttpResponses.html(exchange, 401, resourceLoader.text("account/anonymous/login-invalid.html"));
        }
    }

    private void findAccount(HttpExchange exchange) throws IOException {
        var query = exchange.getRequestURI().getQuery();
        if (query == null || !query.startsWith("userName=")) {
            HttpResponses.text(exchange, 400, "userName is required");
            return;
        }
        var userName = query.substring("userName=".length());
        var result = queryPort.findByUserName(userName);
        if (result instanceof AccountQueryResult.AccountFound(AccountId accountId, String name)) {
            HttpResponses.text(exchange, 200, accountId.value() + " " + name);
        } else {
            try {
                var byId = queryPort.findById(new AccountId(UUID.fromString(userName)));
                if (byId instanceof AccountQueryResult.AccountFound(AccountId accountId, String name)) {
                    HttpResponses.text(exchange, 200, accountId.value() + " " + name);
                    return;
                }
            } catch (IllegalArgumentException ignored) {
                // Not a UUID lookup.
            }
            HttpResponses.text(exchange, 404, "Not found");
        }
    }

    private static java.util.Map<String, String> form(HttpExchange exchange) throws IOException {
        return FormParser.parse(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
    }
}
