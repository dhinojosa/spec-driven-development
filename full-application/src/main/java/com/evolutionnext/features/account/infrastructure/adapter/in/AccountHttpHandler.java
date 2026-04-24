package com.evolutionnext.features.account.infrastructure.adapter.in;

import com.evolutionnext.features.account.application.command.AccountCommand;
import com.evolutionnext.features.account.application.command.AccountResult;
import com.evolutionnext.features.account.application.query.AccountQueryResult;
import com.evolutionnext.features.account.domain.model.AccountId;
import com.evolutionnext.features.account.port.in.AnonymousAccountCommandPort;
import com.evolutionnext.features.account.port.in.AnonymousAccountQueryPort;
import com.evolutionnext.http.AuthCookies;
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
            HttpResponses.html(exchange, 200, registrationPage("", ""));
        } else if ("POST".equals(method) && "/account/register".equals(path)) {
            register(exchange);
        } else if ("GET".equals(method) && "/account/login".equals(path)) {
            HttpResponses.html(exchange, 200, resourceLoader.text("account/anonymous/login.html"));
        } else if ("POST".equals(method) && "/account/login".equals(path)) {
            logIn(exchange);
        } else if ("GET".equals(method) && "/dashboard".equals(path)) {
            securePage(exchange, "account/dashboard.html");
        } else if ("GET".equals(method) && "/todo-today".equals(path)) {
            securePage(exchange, "account/todo-today.html");
        } else if ("GET".equals(method) && "/activity-inventory".equals(path)) {
            securePage(exchange, "account/activity-inventory.html");
        } else if ("GET".equals(method) && "/record-sheet".equals(path)) {
            securePage(exchange, "account/record-sheet.html");
        } else if ("GET".equals(method) && "/account/logout".equals(path)) {
            logOut(exchange);
        } else if ("GET".equals(method) && "/account/pomodoro".equals(path)) {
            securePage(exchange, "account/pomodoro.html");
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
        if (result instanceof AccountResult.AccountRegistered(AccountId ignored, String userName)) {
            AuthCookies.rememberAuthenticatedUser(exchange, userName);
            HttpResponses.html(exchange, 200, resourceLoader.text("account/dashboard.html"));
        } else if (result instanceof AccountResult.InvalidRegistration(String userName, String message)) {
            HttpResponses.html(exchange, 422, registrationPage(userName, message));
        } else if (result instanceof AccountResult.UserNameAlreadyExists(String userName)) {
            HttpResponses.html(exchange, 409, registrationPage(userName, "User name already exists"));
        } else {
            HttpResponses.html(exchange, 409, registrationPage("", ""));
        }
    }

    private void logIn(HttpExchange exchange) throws IOException {
        var form = form(exchange);
        var result = commandPort.execute(new AccountCommand.LogIn(form.getOrDefault("userName", ""),
            form.getOrDefault("password", "")));
        if (result instanceof AccountResult.LogInSucceeded(AccountId ignored, String userName)) {
            AuthCookies.rememberAuthenticatedUser(exchange, userName);
            HttpResponses.html(exchange, 200, resourceLoader.text("account/dashboard.html"));
        } else {
            HttpResponses.html(exchange, 401, resourceLoader.text("account/anonymous/login-invalid.html"));
        }
    }

    private void logOut(HttpExchange exchange) throws IOException {
        AuthCookies.clearAuthenticatedUser(exchange);
        HttpResponses.html(exchange, 200, resourceLoader.text("welcome/anonymous/index.html"));
    }

    private void securePage(HttpExchange exchange, String resourceName) throws IOException {
        if (!AuthCookies.isAuthenticated(exchange)) {
            HttpResponses.html(exchange, 401, resourceLoader.text("welcome/anonymous/index.html"));
            return;
        }
        HttpResponses.html(exchange, 200, resourceLoader.text(resourceName));
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

    private String registrationPage(String userName, String message) {
        var feedback = message.isBlank()
            ? ""
            : "<p class=\"account-feedback\">" + escapeHtml(message) + "</p>";
        return resourceLoader.text("account/anonymous/register.html")
            .replace("{{REGISTRATION_FEEDBACK}}", feedback)
            .replace("{{USER_NAME_VALUE}}", escapeHtml(userName));
    }

    private static String escapeHtml(String value) {
        return value
            .replace("&", "&amp;")
            .replace("\"", "&quot;")
            .replace("<", "&lt;")
            .replace(">", "&gt;");
    }

}
