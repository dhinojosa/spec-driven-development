package com.evolutionnext.features.account.domain.events;

import com.evolutionnext.features.account.domain.model.AccountId;
import com.evolutionnext.features.account.domain.model.UserName;

import java.time.Instant;
import java.util.Objects;

public record AccountLoggedIn(AccountId accountId, UserName userName, Instant occurredAt) implements AccountEvent {
    public AccountLoggedIn {
        Objects.requireNonNull(accountId, "accountId");
        Objects.requireNonNull(userName, "userName");
        Objects.requireNonNull(occurredAt, "occurredAt");
    }
}
