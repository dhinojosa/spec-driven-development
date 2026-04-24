package com.evolutionnext.http;

import com.sun.net.httpserver.HttpExchange;

import java.util.Arrays;

public final class AuthCookies {
    private AuthCookies() {
    }

    public static void rememberAuthenticatedUser(HttpExchange exchange, String userName) {
        exchange.getResponseHeaders().add("Set-Cookie",
            "account_user=" + userName + "; Path=/; HttpOnly; SameSite=Lax");
    }

    public static void clearAuthenticatedUser(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Set-Cookie",
            "account_user=; Path=/; Max-Age=0; HttpOnly; SameSite=Lax");
    }

    public static boolean isAuthenticated(HttpExchange exchange) {
        return exchange.getRequestHeaders().getOrDefault("Cookie", java.util.List.of()).stream()
            .flatMap(header -> Arrays.stream(header.split(";")))
            .map(String::trim)
            .anyMatch(cookie -> cookie.startsWith("account_user=") && !cookie.equals("account_user="));
    }
}
