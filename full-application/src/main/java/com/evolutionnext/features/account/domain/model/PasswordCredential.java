package com.evolutionnext.features.account.domain.model;

public record PasswordCredential(String value) {
    public PasswordCredential {
        if (value == null || value.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
    }

    public boolean matches(String candidate) {
        return value.equals(candidate);
    }
}
