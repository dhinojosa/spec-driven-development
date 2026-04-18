package com.evolutionnext.features.account.domain.events;

import com.evolutionnext.features.account.domain.model.AccountId;

import java.time.Instant;

public sealed interface AccountEvent permits AccountRegistered, AccountLoggedIn {
    AccountId accountId();

    Instant occurredAt();
}
