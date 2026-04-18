package com.evolutionnext.http;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public final class FormParser {
    private FormParser() {
    }

    public static Map<String, String> parse(String body) {
        if (body == null || body.isBlank()) {
            return Map.of();
        }
        return Arrays.stream(body.split("&"))
            .map(part -> part.split("=", 2))
            .filter(parts -> parts.length == 2)
            .collect(Collectors.toMap(parts -> decode(parts[0]), parts -> decode(parts[1]), (left, right) -> right));
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
