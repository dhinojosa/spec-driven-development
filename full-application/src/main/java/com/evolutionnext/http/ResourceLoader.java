package com.evolutionnext.http;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class ResourceLoader {
    public String text(String resourceName) {
        try (var inputStream = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource not found: " + resourceName);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load resource: " + resourceName, exception);
        }
    }

    public byte[] bytes(String resourceName) {
        try (var inputStream = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                return null;
            }
            return inputStream.readAllBytes();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load resource: " + resourceName, exception);
        }
    }
}
