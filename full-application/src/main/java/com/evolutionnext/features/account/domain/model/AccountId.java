package com.evolutionnext.features.account.domain.model;

import java.util.Objects;
import java.util.UUID;

public record AccountId(UUID value) {
    public AccountId {
        Objects.requireNonNull(value, "value");
    }

    public static AccountId newId() {
        return new AccountId(UUID.randomUUID());
    }
}
