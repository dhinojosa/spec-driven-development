package com.evolutionnext.features.account.domain.model;

public record UserName(String value) {
    public UserName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("User name is required");
        }
    }
}
